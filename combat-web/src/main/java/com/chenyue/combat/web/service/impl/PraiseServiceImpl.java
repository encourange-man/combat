package com.chenyue.combat.web.service.impl;

import com.chenyue.combat.server.domain.Praise;
import com.chenyue.combat.server.entity.dto.PraiseDTO;
import com.chenyue.combat.server.entity.dto.PraiseRankDTO;
import com.chenyue.combat.server.mapper.PraiseMapper;
import com.chenyue.combat.web.service.PraiseService;
import com.chenyue.combat.web.service.RedisPraiseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenyue
 * @Date 2023/6/20
 */
@Slf4j
@Service
@AllArgsConstructor
public class PraiseServiceImpl implements PraiseService {
    private static final String keyAddPraiseLock = "RedisBlogPraiseAddLock-";

    private final RedissonClient redissonClient;
    private final PraiseMapper praiseMapper;
    private final RedisPraiseService redisPraiseService;

    /**
     * 点赞博客
     *
     * @param request
     * @return
     */
    @Override
    public boolean addPraise(PraiseDTO request) {
        try {
            //用户是否点赞过，如果已经点赞过，返回true
            Praise userPraise = praiseMapper.selectByBlogUserId(request.getBlogId(), request.getUserId());
            if (Objects.nonNull(userPraise)) {
                return true;
            }

            //record点赞记录
            Praise praise = new Praise();
            praise.setBlogId(request.getBlogId());
            praise.setUserId(request.getUserId());
            praise.setPraiseTime(new Date());
            praise.setStatus(1);
            praise.setIsActive(1);
            praise.setCreateTime(new Date());
            praise.setUpdateTime(new Date());
            int insert = praiseMapper.insertSelective(praise);

            if (insert > 0) {
                //如果成功插入博客点赞记录，则输出打印相应的信息，并将用户点赞记录添加至缓存中
                log.info("---点赞博客-{}-无锁-插入点赞记录成功---", request.getBlogId());
                redisPraiseService.cachePraiseBlog(request.getBlogId(), request.getUserId(), 1);

                //触发更新缓存中的排行榜
                redisPraiseService.rankBlogPraise();
            }
        } catch (Exception e) {
            log.error("点赞失败！error:", e);
            return false;
        }
        return true;
    }

    /**
     * 点赞博客-带锁
     * (查询用户点赞情况 -> 点赞 这个过程并不是一个原子操作)
     *
     * @param request
     * @return
     */
    @Override
    public boolean addPraiseWithLock(PraiseDTO request) {
        //获取一次性分布式锁实例
        final String key = keyAddPraiseLock + request.getBlogId() + "-" + request.getUserId();
        RLock lock = redissonClient.getLock(key);

        //点赞前上锁
        lock.lock(10, TimeUnit.SECONDS);
        try {
            //用户是否点赞过，如果已经点赞过，返回true
            Praise userPraise = praiseMapper.selectByBlogUserId(request.getBlogId(), request.getUserId());
            if (Objects.nonNull(userPraise)) {
                return true;
            }

            //record点赞记录
            Praise praise = new Praise();
            praise.setBlogId(request.getBlogId());
            praise.setUserId(request.getUserId());
            praise.setPraiseTime(new Date());
            praise.setStatus(1);
            praise.setIsActive(1);
            praise.setCreateTime(new Date());
            praise.setUpdateTime(new Date());
            int insert = praiseMapper.insertSelective(praise);

            if (insert > 0) {
                //如果成功插入博客点赞记录，则输出打印相应的信息，并将用户点赞记录添加至缓存中
                log.info("---点赞博客-{}-无锁-插入点赞记录成功---", request.getBlogId());
                redisPraiseService.cachePraiseBlog(request.getBlogId(), request.getUserId(), 1);

                //触发更新缓存中的排行榜
                redisPraiseService.rankBlogPraise();
            }
        } catch (Exception e) {
            log.error("addPraiseWithLock failed blogId:{}, userId:{}, error", request.getBlogId(), request.getUserId(), e);
            return false;
        } finally {
            //释放锁
            lock.forceUnlockAsync();
        }

        return true;
    }

    /**
     * 取消博客
     *
     * @param request
     * @return
     */
    @Override
    public boolean cancelPraise(PraiseDTO request) {
        //判断当前参数的合法性
        try {
            if (request.getBlogId() != null && request.getUserId() != null) {
                //当前用户取消点赞博客-更新相应的记录信息
                int result = praiseMapper.cancelPraiseBlog(request.getBlogId(), request.getUserId());
                //判断是否更新成功
                if (result > 0) {
                    //result>0表示更新成功，则同时更新缓存中相应博客的用户点赞记录
                    log.info("---取消点赞博客-{}-更新点赞记录成功---", request.getBlogId());
                    redisPraiseService.cachePraiseBlog(request.getBlogId(), request.getUserId(), 0);


                    //触发更新缓存中的排行榜
                    redisPraiseService.rankBlogPraise();
                }
            }
        } catch (Exception e) {
            log.error("cancelPraise failed, error:", e);
            return false;
        }

        return true;
    }

    /**
     * 获取博客点赞总数
     *
     * @param blogId
     * @return
     */
    @Override
    public Long getBlogPraiseTotal(Integer blogId) {
        return redisPraiseService.getCacheTotalBlog(blogId);
    }

    /**
     * 获取博客点赞总数排行榜-采用缓存
     *
     * @return
     */
    @Override
    public List<PraiseRankDTO> getRankWithRedisson() {
        return redisPraiseService.getBlogPraiseRank();
    }

    /**
     * 获取博客点赞总数排行榜-不采用缓存
     *
     * @return
     */
    @Override
    public List<PraiseRankDTO> getRankNoRedisson() {
        return praiseMapper.getPraiseRank();
    }
}
