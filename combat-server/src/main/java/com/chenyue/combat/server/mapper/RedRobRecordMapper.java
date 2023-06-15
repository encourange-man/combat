package com.chenyue.combat.server.mapper;

import com.chenyue.combat.server.domain.RedRobRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.chenyue.combat.server.domain.RedRobRecord
 */
@Mapper
public interface RedRobRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RedRobRecord record);

    int insertSelective(RedRobRecord record);

    RedRobRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedRobRecord record);

    int updateByPrimaryKey(RedRobRecord record);

}




