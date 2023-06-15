package com.chenyue.combat.web.service;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;

import java.math.BigDecimal;

/**
 * 红包业务逻辑处理结果
 * @Author chenyue
 * @Date 2023/6/14
 */
public interface RedPacketService {
    /**
     * 发放手气红包
     * @param redPacketDTO
     * @return
     */
    String handOut(RedPacketDTO redPacketDTO);

    /**
     * 抢红包
     * @param userId
     * @param redId
     * @return
     */
    BigDecimal rob(Integer userId, String redId);
}
