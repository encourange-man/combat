package com.chenyue.combat.web.exception;

import com.chenyue.combat.server.enums.StatusCode;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
public class BusinessException extends RuntimeException{
    private int errorCode;
    private String msg;

    public BusinessException(StatusCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode = StatusCode.Fail.getCode();
        this.msg = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
