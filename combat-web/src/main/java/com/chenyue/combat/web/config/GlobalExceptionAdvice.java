package com.chenyue.combat.web.config;

import com.chenyue.combat.server.entity.vo.BaseResponse;
import com.chenyue.combat.server.enums.StatusCode;
import com.chenyue.combat.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理类
 * @Author chenyue
 * @Date 2023/6/14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 处理参数校验异常
     * @param error
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public BaseResponse bindExceptionHandler(BindException error) {
        log.info("参数校验异常：{}", error.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new BaseResponse(StatusCode.InvalidParams.getCode(), error.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 处理参数校验异常
     * @param error
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException error) {
        log.error("参数错误not valid：{}", error.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new BaseResponse(StatusCode.InvalidParams.getCode(), error.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 处理业务异常
     * @param error
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException error) {
        log.error("businessException：{}", error.getMsg());
        return new BaseResponse(error.getErrorCode(), error.getMessage());
    }

}
