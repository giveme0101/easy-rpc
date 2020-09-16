package com.github.giveme0101.rpc.codec;

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

    private R result;

    private Throwable error;

    public boolean isError(){
        return null != error;
    }
}
