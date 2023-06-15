package com.chenyue.combat.server.mapper;


import com.chenyue.combat.server.domain.RedDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity .domain.RedDetail
 */
@Mapper
public interface RedDetailMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RedDetail record);

    int insertSelective(RedDetail record);

    RedDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedDetail record);

    int updateByPrimaryKey(RedDetail record);

}




