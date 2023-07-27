package com.chenyue.combat.server.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @Author chenyue
 * @Date 2023/6/20
 */
@Data
public class PraiseDTO {
    /**
     * 用户ID
     */
    @NotNull(message = "userId不允许为空！")
    private Integer userId;
    /**
     * 博客ID
     */
    @NotNull(message = "blogId不允许为空")
    private Integer blogId;
}
