package com.chenyue.combat.server.service.impl;

import com.chenyue.combat.server.domain.RedDetail;
import com.chenyue.combat.server.domain.RedRecord;
import com.chenyue.combat.server.domain.RedRobRecord;
import com.chenyue.combat.server.entity.dto.RedPacketDTO;
import com.chenyue.combat.server.mapper.RedDetailMapper;
import com.chenyue.combat.server.mapper.RedRecordMapper;
import com.chenyue.combat.server.mapper.RedRobRecordMapper;
import com.chenyue.combat.server.service.RedService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Service
@AllArgsConstructor
public class RedServiceImpl implements RedService {
    private final RedDetailMapper redDetailMapper;
    private final RedRecordMapper redRecordMapper;
    private final RedRobRecordMapper redRobRecordMapper;


    /**
     * 记录红包发放记录， TODO: 事务保证？
     * @param redPacketDTO
     * @param redId
     * @param redAmountList
     */
    @Override
    public void recordRedPacket(RedPacketDTO redPacketDTO, String redId, List<Integer> redAmountList) {
        //记录红包发送记录
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(redPacketDTO.getUserId());
        redRecord.setTotal(redPacketDTO.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(redPacketDTO.getAmount()));
        redRecord.setCreateTime(new Date());
        redRecord.setUpdateTime(new Date());
        redRecord.setRedPacket(redId);
        redRecordMapper.insertSelective(redRecord);


        //记录红包详情记录
        RedDetail redDetail;
        for (Integer amountDetail : redAmountList) {
            redDetail = new RedDetail();
            redDetail.setRecordId(redRecord.getId());
            redDetail.setAmount(BigDecimal.valueOf(amountDetail));
            redDetail.setCreateTime(new Date());
            redDetail.setUpdateTime(new Date());
            redDetailMapper.insertSelective(redDetail);
        }
    }

    /**
     * 记录用户抢红包记录
     * @param userId
     * @param redId
     * @param amount
     */
    @Override
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());
        redRobRecordMapper.insertSelective(redRobRecord);
    }
}
