package com.chenyue.combat.server.service;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
public interface RedService {
    /**
     * 记录红包发放记录
     * @param redPacketDTO
     * @param redId
     * @param redAmountList
     */
    void recordRedPacket(RedPacketDTO redPacketDTO, String redId, List<Integer> redAmountList);

    /**
     * 记录用户抢红包记录
     * @param userId
     * @param redId
     * @param amount
     */
    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount);
}
