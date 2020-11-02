package com.github.easyrpc.common.entity;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ContextEvent
 * @Date 2020/09/21 15:02
 */
public enum ContextEvent {

    STARTING(1),
    SERVER_STARTING(2),
    PRE_CLOSE(-1);

    private int status;

    ContextEvent(int status) {
        this.status = status;
    }

    public boolean equals(ContextEvent another){
        return this.status == another.status;
    }
}
