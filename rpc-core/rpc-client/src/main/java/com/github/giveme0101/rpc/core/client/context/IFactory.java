package com.github.giveme0101.rpc.core.client.context;

public interface IFactory {

    <T> T getClient(Class<T> clazz);

}
