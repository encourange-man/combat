package com.chenyue.combat.server.mapper;

import com.chenyue.combat.server.domain.UserAccountRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.chenyue.combat.server.domain.UserAccountRecord
 */
@Mapper
public interface UserAccountRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserAccountRecord record);

    int insertSelective(UserAccountRecord record);

    UserAccountRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAccountRecord record);

    int updateByPrimaryKey(UserAccountRecord record);

}




