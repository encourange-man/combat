package com.chenyue.combat.server.mapper;

import com.chenyue.combat.server.domain.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.chenyue.combat.server.domain.UserAccount
 */
@Mapper
public interface UserAccountMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    UserAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAccount record);

    int updateByPrimaryKey(UserAccount record);

    UserAccount selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询-用于悲观锁
     * @param userId
     * @return
     */
    UserAccount selectByUserIdLock(@Param("userId") Long userId);

    int updateAmount(@Param("money") Double money, @Param("id") Integer id);

    /**
     * 根据id和version更新余额，基于数据的乐观锁
     * @param money
     * @param id
     * @param version
     */
    int updateAmountByVersion(@Param("money") Double money, @Param("id") Integer id, @Param("version") Integer version);

    /**
     * 根据主键id更新账户余额-悲观锁的方式
     * @param money
     * @param id
     * @return
     */
    int updateAmountLock(@Param("money") Double money, @Param("id") Integer id);
}




