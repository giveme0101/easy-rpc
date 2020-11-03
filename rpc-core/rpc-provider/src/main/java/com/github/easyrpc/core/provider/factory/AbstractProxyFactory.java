package com.github.easyrpc.core.provider.factory;

import com.github.easyrpc.common.entity.RpcRequest;
import com.github.easyrpc.common.entity.RpcResponse;
import com.github.easyrpc.common.exception.RpcMessageChecker;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractProxyFactory
 * @Date 2020/09/16 14:36
 */
public abstract class AbstractProxyFactory implements IClientFactory {

    @Override
    public <T> T getClient(Class<T> clazz, String serviceName, String version) {
        Object instance = Proxy.newProxyInstance(AbstractProxyFactory.class.getClassLoader(), new Class[]{clazz}, (Object proxy, Method method, Object[] args) -> {

            if (Object.class == method.getDeclaringClass()) {
                String name = method.getName();
                if ("equals".equals(name)) {
                    return proxy == args[0];
                }

                if ("hashCode".equals(name)) {
                    return System.identityHashCode(proxy);
                }

                if ("toString".equals(name)) {
                    return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) + ", with InvocationHandler " + this;
                }

                throw new IllegalStateException(String.valueOf(method));
            }

            RpcRequest rpcRequest = RpcRequest.builder()
                    .requestId(UUID.randomUUID().toString())
                    .className(StringUtils.isEmpty(serviceName) ? clazz.getName() : serviceName)
                    .version(version)
                    .methodName(method.getName())
                    .parameterTypes(method.getParameterTypes())
                    .parameters(args)
                    .build();

            RpcResponse rpcResponse = sendRequest(rpcRequest).get();
            RpcMessageChecker.check(rpcResponse, rpcRequest);
            return rpcResponse.getResult();
        });

        return (T) instance;
    }

    @Override
    public  <T> T getClient(Class<T> clazz){
        return getClient(clazz, null, null);
    }

   abstract Future<RpcResponse> sendRequest(RpcRequest request);

}
