package com.github.easyrpc.core.consumer.factory;

public interface IClientFactory {

    <T> T getClient(Class<T> clazz);

    <T> T getClient(Class<T> clazz, String serviceName, String version);

}
