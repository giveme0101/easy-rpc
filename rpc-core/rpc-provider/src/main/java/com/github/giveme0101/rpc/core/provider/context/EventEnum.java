package com.github.giveme0101.rpc.core.provider.context;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name EventEnum
 * @Date 2020/09/21 15:02
 */
public enum  EventEnum {

    STOP(0),
    STARTING(1),
    SERVER_STARTING(2);

    private int status;

    EventEnum(int status) {
        this.status = status;
    }
}
