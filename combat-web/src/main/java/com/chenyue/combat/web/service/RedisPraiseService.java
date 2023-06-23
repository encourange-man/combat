package com.chenyue.combat.web.service;

import com.chenyue.combat.server.entity.dto.PraiseRankDTO;

import java.util.List;

public interface RedisPraiseService {

    /**
     * 缓存当前用户点赞博客的记录-包括正常点赞、取消点赞
     * @param blogId
     * @param uId
     * @param status
     * @throws Exception
     */
    void cachePraiseBlog(Integer blogId, Integer uId, Integer status);

    /**
     * 获取当前博客总的点赞数
     * @param blogId
     * @return
     * @throws Exception
     */
    Long getCacheTotalBlog(Integer blogId);

    /**
     * 触发博客点赞总数排行榜
     * @throws Exception
     */
    void rankBlogPraise();

    /**
     * 获得博客点赞排行榜
     * @return
     * @throws Exception
     */
    List<PraiseRankDTO> getBlogPraiseRank();
}
