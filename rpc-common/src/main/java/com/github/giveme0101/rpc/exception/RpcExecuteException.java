package com.github.giveme0101.rpc.exception;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcExecuteException
 * @Date 2020/09/16 10:19
 */
public class RpcExecuteException extends RuntimeException {

    public RpcExecuteException() {
    }

    public RpcExecuteException(String message) {
        super(message);
    }

    public RpcExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcExecuteException(Throwable cause) {
        super(cause);
    }

    public RpcExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
