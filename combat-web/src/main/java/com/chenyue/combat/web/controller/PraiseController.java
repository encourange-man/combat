package com.chenyue.combat.web.controller;

import com.chenyue.combat.server.entity.dto.PraiseDTO;
import com.chenyue.combat.server.entity.vo.BaseResponse;
import com.chenyue.combat.server.enums.StatusCode;
import com.chenyue.combat.web.service.PraiseService;
import com.chenyue.combat.web.service.impl.PraiseServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.HashMap;

import static com.chenyue.combat.server.enums.StatusCode.Fail;
import static com.chenyue.combat.server.enums.StatusCode.Success;

/**
 * @Author chenyue
 * @Date 2023/6/20
 */
@RestController
public class PraiseController {

    @Resource
    private PraiseService praiseService;

    /**
     * 点赞博客
     * @param request
     * @return
     */
    @PostMapping("/blog/praise/add")
    public BaseResponse addPraise(@Validated @RequestBody PraiseDTO request) {
        boolean result = praiseService.addPraise(request);
        if(!result) {
            return new BaseResponse(Fail);
        }

        //点赞博客，不加分布式锁
        praiseService.addPraise(request);
        //点赞博客，不加分布式锁
//        praiseService.addPraiseWithLock(request);


        //博客点赞总数量
        HashMap<String, Object> map = Maps.newHashMap();
        Long blogPraiseTotal = praiseService.getBlogPraiseTotal(request.getBlogId());
        map.put("total", blogPraiseTotal);
        return new BaseResponse(Success, map);
    }

    /**
     * 取消点赞
     * @param request
     * @return
     */
    @PostMapping("/blog/praise/cancel")
    public BaseResponse cancelPraise(@Validated @RequestBody PraiseDTO request) {
        //取消点赞
        praiseService.cancelPraise(request);

        HashMap<String, Object> map = Maps.newHashMap();
        Long blogPraiseTotal = praiseService.getBlogPraiseTotal(request.getBlogId());
        map.put("total", blogPraiseTotal);
        return new BaseResponse(Success, map);
    }

    @GetMapping("/blog/praise/rank")
    public BaseResponse praiseRank() {
        return new BaseResponse(Success, praiseService.getRankNoRedisson());
    }
}
