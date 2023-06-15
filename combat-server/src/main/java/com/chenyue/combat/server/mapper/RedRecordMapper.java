package com.chenyue.combat.server.mapper;

import com.chenyue.combat.server.domain.RedRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.chenyue.combat.server.domain.RedRecord
 */
@Mapper
public interface RedRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RedRecord record);

    int insertSelective( RedRecord record);

    RedRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedRecord record);

    int updateByPrimaryKey(RedRecord record);

}




