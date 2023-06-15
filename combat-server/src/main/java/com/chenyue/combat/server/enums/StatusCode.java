package com.chenyue.combat.server.enums;

import lombok.Getter;

/**
 * @Author chenyue
 * @Date 2023/6/14
 */
@Getter
public enum StatusCode {
    Success(0, "成功"),
    Fail(-1, "失败"),
    InvalidParams(201, "非法的参数！"),
    InvalidGrantType(202, "非法的授权类型"),

    ;

    private final Integer code;
    private final String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
