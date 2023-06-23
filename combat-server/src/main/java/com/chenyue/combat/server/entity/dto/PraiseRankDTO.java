package com.chenyue.combat.server.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author chenyue
 * @Date 2023/6/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraiseRankDTO implements Serializable {
    /**
     * 博客ID
     */
    private Integer blogId;
    /**
     * 点赞总数
     */
    private Long total;
}
