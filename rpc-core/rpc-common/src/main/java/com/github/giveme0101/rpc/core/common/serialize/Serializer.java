package com.github.giveme0101.rpc.core.common.serialize;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Serializer
 * @Date 2020/09/15 9:32
 */
public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

}
