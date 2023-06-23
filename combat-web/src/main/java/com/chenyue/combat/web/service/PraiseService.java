package com.chenyue.combat.web.service;

import com.chenyue.combat.server.entity.dto.PraiseDTO;
import com.chenyue.combat.server.entity.dto.PraiseRankDTO;

import java.util.List;

/**
 * @Author chenyue
 * @Date 2023/6/20
 */
public interface PraiseService {

    /**
     * 点赞博客
     * @param request
     * @return
     */
    boolean addPraise(PraiseDTO request);

    /**
     * 点赞博客-带锁
     * @param request
     * @return
     */
    boolean addPraiseWithLock(PraiseDTO request);

    /**
     * 取消博客
     * @param request
     * @return
     */
    boolean cancelPraise(PraiseDTO request);

    /**
     * 获取博客点赞总数
     * @param blogId
     * @return
     */
    Long getBlogPraiseTotal(Integer blogId);

    /**
     * 获取博客点赞总数排行榜-采用缓存
     * @return
     */
    List<PraiseRankDTO> getRankWithRedisson();


    /**
     * 获取博客点赞总数排行榜-不采用缓存
     * @return
     */
    List<PraiseRankDTO> getRankNoRedisson();
}
