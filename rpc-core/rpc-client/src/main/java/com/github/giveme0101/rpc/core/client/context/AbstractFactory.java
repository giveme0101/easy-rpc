package com.github.giveme0101.rpc.core.client.context;

import com.github.giveme0101.rpc.core.common.codec.RpcRequest;
import com.github.giveme0101.rpc.core.client.util.ResponseFutureHolder;
import com.github.giveme0101.rpc.core.client.util.SocketChannelHolder;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name AbstractFactory
 * @Date 2020/09/16 14:36
 */
public abstract class AbstractFactory implements IFactory {

    @Override
    public <T> T getClient(Class<T> clazz){

        Object instance = Proxy.newProxyInstance(AbstractFactory.class.getClassLoader(), new Class[]{clazz}, (Object proxy, Method method, Object[] args) -> {

            if (Object.class == method.getDeclaringClass()) {
                String name = method.getName();
                if ("equals".equals(name)) {
                    return proxy == args[0];
                }

                if ("hashCode".equals(name)) {
                    return System.identityHashCode(proxy);
                }

                if ("toString".equals(name)) {
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
                }

                throw new IllegalStateException(String.valueOf(method));
            }

            RpcRequest request = RpcRequest.builder()
                    .requestId(UUID.randomUUID().toString())
                    .className(clazz.getName())
                    .methodName(method.getName())
                    .parameterTypes(method.getParameterTypes())
                    .parameters(args)
                    .build();

            SocketChannelHolder.get().writeAndFlush(request);
            return ResponseFutureHolder.get(request).get();
        });

        return (T) instance;
    }

}
