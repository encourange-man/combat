package com.chenyue.combat.web.controller;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;
import com.chenyue.combat.server.entity.vo.BaseResponse;
import com.chenyue.combat.web.service.RedPacketService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Objects;

import static com.chenyue.combat.server.enums.StatusCode.Fail;
import static com.chenyue.combat.server.enums.StatusCode.Success;


/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Slf4j
@RestController
@RequestMapping("/red/packet")
public class RedPacketController {

    @Resource
    private RedPacketService redPacketService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发放手气红包
     * @param request
     * @return
     */
    @PostMapping("/hand/out")
    public BaseResponse handOut(@Validated @RequestBody RedPacketDTO request) {
        String redId = redPacketService.handOut(request);
        return new BaseResponse(Success, redId);
    }

    /**
     * 抢红包
     * @param redId
     * @param userId
     * @return
     */
    @GetMapping("/rob")
    public BaseResponse queryRedis(@RequestParam String redId, @RequestParam Integer userId) {
        BigDecimal rob = redPacketService.rob(userId, redId);
        if(Objects.nonNull(rob)) {
            log.info("用户：{}，抢到了红包：{}", userId, rob);
            return new BaseResponse(Success, rob);
        }
        return new BaseResponse(Fail, "抢完了！");
    }
}
