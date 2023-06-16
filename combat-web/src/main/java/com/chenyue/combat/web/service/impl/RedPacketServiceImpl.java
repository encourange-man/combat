package com.chenyue.combat.web.service.impl;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;
import com.chenyue.combat.server.service.RedService;
import com.chenyue.combat.server.utils.RedPacketUtil;
import com.chenyue.combat.web.exception.BusinessException;
import com.chenyue.combat.web.service.RedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Slf4j
@Service
public class RedPacketServiceImpl implements RedPacketService {
    public static final String keyPreFix = "redis:red:packet";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String, Object> redisValueOperate;

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

        //将红包个数缓存到redis中
        String redTotalKey = redId + ":total";
        redisValueOperate.set(redTotalKey, request.getTotal());
        log.info("redId:{}, redis total value:{}", redId, redisValueOperate.get(redTotalKey));


        //TODO: 异步记录红包记录到DB
        redService.recordRedPacket(request, redId, redPacketLists);
        return redId;
    }

    /**
     * 抢红包，分为两步：抢红包 和 拆红包
     *
     * @param userId
     * @param redId
     * @return 抢到红包金额
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) {
        String userRobKey = redId + userId + ":rob";
        String redTotalKey = redId + ":total";

        //用户如果已经抢过红包，直接返回红包金额
        BigDecimal rob = (BigDecimal) redisValueOperate.get(userRobKey);
        if (Objects.nonNull(rob)) {
            return rob;
        }

        //点红包逻辑-主要判断缓存中所剩的红包是否大于0（即是否还有红包）
        boolean haveRedPacket = clickRedPacket(redId);

        if (haveRedPacket) {
            //从预计算的随机红包列表中，拿出一个红包
            Object packetValue =  redisTemplate.opsForList().rightPop(redId);
            //packetValue !=null，表示当前的红包还有钱
            if (Objects.nonNull(packetValue)) {
                //更新缓存中的红包个数-1
                Integer currTotal = redisValueOperate.get(redTotalKey) != null ? (Integer) redisValueOperate.get(redTotalKey) : 0;
                redisValueOperate.set(redTotalKey, currTotal - 1);

                //将抢到红包时用户的账号信息及抢到的金额等信息记入数据库
                BigDecimal result = new BigDecimal(packetValue.toString()).divide(new BigDecimal(100));
                redService.recordRobRedPacket(userId, redId, new BigDecimal(packetValue.toString()));

                //将当前抢到红包的用户设置进缓存系统中，用于表示当前用户已经抢过红包了
                redisValueOperate.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                log.info("当前用户抢到红包了：userId={} key={} 金额={} ", userId, redId, result);
                return result;
            }
        }
        //表示没有抢到红包
        return null;
    }


    /**
     * 点红包逻辑-缓存中是否还有红包
     *
     * @param redId
     * @return true-有，false-无
     */
    private boolean clickRedPacket(String redId) {
        Integer residueRedPacket = (Integer) redisValueOperate.get(redId + ":total");
        return Objects.nonNull(residueRedPacket) && residueRedPacket > 0;
    }
}
