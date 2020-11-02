package com.github.easyrpc.common.serialize;

import com.github.easyrpc.common.spi.Named;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Serializer
 * @Date 2020/09/15 9:32
 */
public interface Serializer extends Named {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

}
