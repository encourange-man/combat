package com.chenyue.combat.web.service.impl;

import com.chenyue.combat.server.entity.dto.PraiseRankDTO;
import com.chenyue.combat.server.mapper.PraiseMapper;
import com.chenyue.combat.web.service.RedisPraiseService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 博客点赞处理服务
 *
 * @Author chenyue
 * @Date 2023/6/20
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisPraiseServiceImpl implements RedisPraiseService {
    //缓存中记录用户点赞记录key
    private static final String keyBlog = "RedisBlogPraiseMap";
    //定义用于缓存排行榜的Key
    final static String praiseRankKey = "praiseRankListKey";

    private final RedissonClient redissonClient;
    private final PraiseMapper praiseMapper;


    /**
     * 缓存当前用户点赞博客的记录-包括正常点赞、取消点赞
     *
     * @param blogId
     * @param userId
     * @param status
     * @throws Exception
     */
    @Override
    public void cachePraiseBlog(Integer blogId, Integer userId, Integer status) {
        //定义Redisson的RMap实例，记录哪些用户都点赞了
        RMap<String, Integer> rmap = redissonClient.getMap(keyBlog);

        //获取一次性分布式锁实例
        final String lockKey = "RedisBlogPraiseAddLock-" + blogId + "-" + userId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            //尝试获取分布式锁（可重入）
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                if (Objects.nonNull(blogId) && Objects.nonNull(userId) && Objects.nonNull(status)) {
                    final String key = blogId + ":" + userId;

                    if (1 == status) {
                        //点赞操作，需要将用户ID添加到RMap中
                        rmap.putIfAbsent(key, userId);
                    } else if (0 == status) {
                        //取消点赞操作，将唯一的key从RMap中删除
                        rmap.remove(key);
                    }
                }
            }

        } catch (Exception e) {
            log.info("cachePraiseBlog failed, error:", e);
        } finally {
            //释放锁
            if (null != lock) {
                lock.forceUnlock();
            }
        }
    }

    /**
     * 获取当前博客总的点赞数
     *
     * @param blogId
     * @return
     * @throws Exception
     */
    @Override
    public Long getCacheTotalBlog(Integer blogId) {
        Long result = 0L;
        if (Objects.isNull(blogId)) {
            return result;
        }

        //获取缓存中所有用户的点赞记录
        RMap<String, Integer> priseMap = redissonClient.getMap(keyBlog);
        Map<String, Integer> dataMap = priseMap.readAllMap();

        if (CollectionUtils.isEmpty(dataMap) || CollectionUtils.isEmpty(dataMap.keySet())) {
            return result;
        }

        Integer bId;
        for (String key : dataMap.keySet()) {
            if (Objects.nonNull(key)) {
                //key 为 “博客id:用户id”
                String[] arr = key.split(":");
                if (arr.length > 0) {
                    bId = Integer.valueOf(arr[0]);
                    //判断当前取出的 键 对应的 博客id 是否跟当前待比较的 博客id 相等，
                    //如果是，代表有一条点赞记录，则结果需要加1
                    if (blogId.equals(bId)) {
                        result += 1;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 触发博客点赞总数排行榜
     *
     * @throws Exception
     */
    @Override
    public void rankBlogPraise() {
        //根据数据库查询语句得到已经排好序的博客实体对象列表
        List<PraiseRankDTO> list = praiseMapper.getPraiseRank();

        //判断列表中是否有数据
        if (list != null && list.size() > 0) {
            //获取Redisson的列表组件RList实例
            RList<PraiseRankDTO> rList = redissonClient.getList(praiseRankKey);
            //先清空缓存中的列表数据
            rList.clear();
            //将得到的最新的排行榜更新至缓存中
            rList.addAll(list);
        }
    }

    /**
     * 获得博客点赞排行榜
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<PraiseRankDTO> getBlogPraiseRank() {
        //获取Redisson的列表组件RList实例
        RList<PraiseRankDTO> rList = redissonClient.getList(praiseRankKey);
        //获取缓存中最新的排行榜
        return rList.readAll();
    }
}
