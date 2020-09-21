package com.github.giveme0101.rpc.core.common.exception;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcException
 * @Date 2020/09/16 10:24
 */
public class RpcException extends RuntimeException{
    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
