package com.github.easyrpc.common.entity;

import lombok.Getter;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ResponseCodeEnum
 * @Date 2020/09/22 11:08
 */
@Getter
public enum  ResponseCodeEnum {

    SUCCESS("200", "请求成功"),
    FAIL("400", "RPC请求失败"),
    INTERNAL_ERROR("500", "系统内部错误");

    private String code;
    private String message;

    ResponseCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

}
