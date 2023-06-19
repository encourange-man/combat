package com.chenyue.combat.web.service.impl;

import com.chenyue.combat.server.domain.UserAccount;
import com.chenyue.combat.server.domain.UserAccountRecord;
import com.chenyue.combat.server.entity.dto.UserCountDTO;
import com.chenyue.combat.server.mapper.UserAccountMapper;
import com.chenyue.combat.server.mapper.UserAccountRecordMapper;
import com.chenyue.combat.web.exception.BusinessException;
import com.chenyue.combat.web.service.DataBaseLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author chenyue
 * @Date 2023/6/17
 */
@Slf4j
@Service
public class DataBaseLockServiceImpl implements DataBaseLockService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserAccountRecordMapper userAccountRecordMapper;


    @Override
    public void takeMoney(UserCountDTO dto) {
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());

        if(Objects.nonNull(userAccount) && userAccount.getAmount().doubleValue() >= dto.getAmount()) {
            int res = userAccountMapper.updateAmount(dto.getAmount(), userAccount.getId());

            if(res > 0) {
                UserAccountRecord userAccountRecord = new UserAccountRecord();
                userAccountRecord.setAccountId(userAccount.getId());
                userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
                userAccountRecordMapper.insertSelective(userAccountRecord);
                log.info("账户余额为：{}, 当前提取金额为：{}, ", userAccount.getAmount(), dto.getAmount());
            }else {
                throw new BusinessException("账户不存在或者账户余额不足！");
            }
        }
    }

    /**
     * 提现-基于数据库的乐观锁
     * @param dto
     */
    @Override
    public void takeMoneyWithOptimisticLock(UserCountDTO dto) {
        //查询用户的账户信息
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());

        //账户余额是否够，如果够，进行提现
        if(Objects.nonNull(userAccount) && userAccount.getAmount().doubleValue() >= dto.getAmount()) {
            //基于乐观锁更新账户余额，保证查询和更新的原子操作
            int res = userAccountMapper.updateAmountByVersion(dto.getAmount(), userAccount.getId(), userAccount.getVersion());

            if(res > 0) {
                //插入提交申请提现记录
                UserAccountRecord userAccountRecord = new UserAccountRecord();
                userAccountRecord.setAccountId(userAccount.getId());
                userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
                userAccountRecordMapper.insertSelective(userAccountRecord);
                log.info("账户余额为：{}, 当前提取金额为：{}, ", userAccount.getAmount(), dto.getAmount());
            }else {
                throw new BusinessException("账户不存在或者账户余额不足！");
            }
        }
    }

    /**
     * 提现-基于数据库的乐观锁
     * @param dto
     */
    @Override
    public void takeMoneyWithPessimisticLock(UserCountDTO dto) {
        //查询用户的账户信息- select for update
        UserAccount userAccount = userAccountMapper.selectByUserIdLock(dto.getUserId());

        //账户余额是否够，如果够，进行提现 TODO: 悲观锁还是没有理解，上一步骤，不添加for update，测试也是OK的
        if(Objects.nonNull(userAccount) && userAccount.getAmount().doubleValue() >= dto.getAmount()) {
            int res = userAccountMapper.updateAmountLock(dto.getAmount(), userAccount.getId());

            if(res > 0) {
                //插入提交申请提现记录
                UserAccountRecord userAccountRecord = new UserAccountRecord();
                userAccountRecord.setAccountId(userAccount.getId());
                userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
                userAccountRecordMapper.insertSelective(userAccountRecord);
                log.info("账户余额为：{}, 当前提取金额为：{}, ", userAccount.getAmount(), dto.getAmount());
            }else {
                throw new BusinessException("账户不存在或者账户余额不足！");
            }
        }
    }
}
