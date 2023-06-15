package com.chenyue.combat.server.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Data
public class RedPacketDTO {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 红包个数
     */
    @NotNull(message = "红包个数不能为空")
    private Integer total;

    /**
     * 红包总金额，单位为分
     */
    @NotNull(message = "红包总金额不能为空")
    private Integer amount;
}
