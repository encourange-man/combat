package com.chenyue.combat.web.service;

import com.chenyue.combat.server.entity.dto.UserCountDTO;

public interface DataBaseLockService {

    void takeMoney(UserCountDTO userCountDTO);

    /**
     * 提现-基于数据库的乐观锁
     * @param userCountDTO
     */
    void takeMoneyWithOptimisticLock(UserCountDTO userCountDTO);

    /**
     * 提现-基于数据库的悲观锁
     * @param userCountDTO
     */
    void takeMoneyWithPessimisticLock(UserCountDTO userCountDTO);
}
