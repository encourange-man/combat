package com.chenyue.combat.web.controller;

import com.chenyue.combat.server.entity.dto.UserCountDTO;
import com.chenyue.combat.server.entity.vo.BaseResponse;
import com.chenyue.combat.server.enums.StatusCode;
import com.chenyue.combat.web.service.DataBaseLockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.chenyue.combat.server.enums.StatusCode.Success;

/**
 * @Author chenyue
 * @Date 2023/6/17
 */
@RestController
public class DataBaseLockController {

    @Resource
    private DataBaseLockService dataBaseLockService;

    /**
     * 用户提现
     * @param userCountDTO
     * @return
     */
    @PostMapping("/money/taskMoney")
    public BaseResponse takMoney(@RequestBody UserCountDTO userCountDTO) {
        dataBaseLockService.takeMoneyWithPessimisticLock(userCountDTO);
        return new BaseResponse(Success);
    }
}
