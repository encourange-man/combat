package com.chenyue.combat.web.service.impl;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;
import com.chenyue.combat.server.service.RedService;
import com.chenyue.combat.server.utils.RedPacketUtil;
import com.chenyue.combat.web.exception.BusinessException;
import com.chenyue.combat.web.service.RedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Slf4j
@Service
public class RedPacketServiceImpl implements RedPacketService {
    public static final String keyPreFix = "redis:red:packet";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedService redService;

    /**
     * 发放手气红包
     *
     * @param request
     * @return
     */
    @Override
    public String handOut(RedPacketDTO request) {
        if (Objects.isNull(request.getAmount()) || Objects.isNull(request.getTotal())) {
            throw new BusinessException("红包数量或个数不能为空");
        }

        //采用二倍均值法 预生成红包随机金额
        List<Integer> redPacketLists = RedPacketUtil.divideRedPackage(request.getAmount(), request.getTotal());

        //生成红包全局唯一表示串（使用当前时间戳，纳秒）
        String timestamp = String.valueOf(System.nanoTime());

        //将随机金额列表存储redis缓存列表中
        String redId = keyPreFix + request.getUserId() + ":" + timestamp;
        redisTemplate.opsForList().leftPushAll(redId, redPacketLists);

        //将红包金额缓存到redis中
        String redTotalKey = redId + ":total";
        redisTemplate.opsForValue().set(redTotalKey, request.getTotal());
        log.info("redId:{}, redis total value:{}", redId, redisTemplate.opsForValue().get(redTotalKey));


        //TODO: 异步记录红包记录到DB
        redService.recordRedPacket(request, redId, redPacketLists);
        return redId;
    }

    /**
     * 抢红包
     *
     * @param userId
     * @param redId
     * @return
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) {
        return null;
    }
}
