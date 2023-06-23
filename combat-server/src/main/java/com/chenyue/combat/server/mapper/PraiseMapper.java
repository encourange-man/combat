package com.chenyue.combat.server.mapper;

import com.chenyue.combat.server.domain.Praise;
import com.chenyue.combat.server.entity.dto.PraiseRankDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.chenyue.combat.server.domain.Praise
 */
public interface PraiseMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Praise record);

    int insertSelective(Praise record);

    Praise selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Praise record);

    int updateByPrimaryKey(Praise record);

    /**
     * 根据博客id-用户id查询点赞记录
     * @param blogId
     * @param userId
     * @return
     */
    Praise selectByBlogUserId(@Param("blogId")Integer blogId, @Param("userId") Integer userId);

    /**
     * 根据博客id查询点赞数
     * @param blogId
     * @return
     */
    Long countByBlogId(@Param("blogId") Integer blogId);

    /**
     * 取消点赞博客
     * @param blogId
     * @param userId
     */
    int cancelPraiseBlog(@Param("blogId")Integer blogId, @Param("userId") Integer userId);

    /**
     * 获取博客点赞总数排行榜
     * @return
     */
    List<PraiseRankDTO> getPraiseRank();
}




