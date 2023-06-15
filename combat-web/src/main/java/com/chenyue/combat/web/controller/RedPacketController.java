package com.chenyue.combat.web.controller;

import com.chenyue.combat.server.entity.dto.RedPacketDTO;
import com.chenyue.combat.server.entity.vo.BaseResponse;
import com.chenyue.combat.web.service.RedPacketService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.chenyue.combat.server.enums.StatusCode.Success;


/**
 * @Author chenyue
 * @Date 2023/6/14
 */
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

    @GetMapping("/queryRedis")
    public BaseResponse queryRedis(@RequestParam String key) {
        return new BaseResponse(Success, redisTemplate.opsForValue().get(key));
    }
}
