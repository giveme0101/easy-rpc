package com.github.giveme0101.rpc.core.consumer.context;

public interface IFactory {

    <T> T getClient(Class<T> clazz);

}
