package com.github.giveme0101.rpc.core.common.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcResponse
 * @Date 2020/09/15 9:30
 */
@Data
@Builder
public class RpcResponse <R> implements Serializable {

    private static final long serialVersionUID = 5928178081675778399L;

    private String requestId;

    private String code;

    private String message;

    private R result;

    public static RpcResponse builderSuccess(String requestId, Object result){
        return RpcResponse.builder()
                .requestId(requestId)
                .result(result)
                .code(ResponseCodeEnum.SUCCESS.getCode())
                .message(ResponseCodeEnum.SUCCESS.getMessage())
                .build();
    }

    public static RpcResponse builderFail(String requestId, ResponseCodeEnum code){
        return builderFail(requestId, code.getCode(), code.getMessage());
    }

    public static RpcResponse builderFail(String requestId){
        return builderFail(requestId, ResponseCodeEnum.INTERNAL_ERROR.getCode(), ResponseCodeEnum.INTERNAL_ERROR.getMessage());
    }

    public static RpcResponse builderFail(String requestId, String code, String message){
        return RpcResponse.builder()
                .requestId(requestId)
                .code(code)
                .message(message)
                .build();
    }

}
