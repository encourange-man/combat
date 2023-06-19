package com.chenyue.combat.server.entity.dto;

import lombok.Data;

/**
 * @Author chenyue
 * @Date 2023/6/17
 */
@Data
public class UserCountDTO {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 提现的金额
     */
    private Double amount;
}
