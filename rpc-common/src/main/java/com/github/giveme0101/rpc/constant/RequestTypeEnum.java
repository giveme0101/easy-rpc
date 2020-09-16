package com.github.giveme0101.rpc.constant;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RequestTypeEnum
 * @Date 2020/09/16 16:54
 */
public enum  RequestTypeEnum {

    HEART_BEAT((byte) 0),
        MESSAGE((byte) 1);

    private byte type;

    RequestTypeEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
