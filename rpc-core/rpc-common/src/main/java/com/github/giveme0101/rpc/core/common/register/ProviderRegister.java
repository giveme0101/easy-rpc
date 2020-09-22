package com.github.giveme0101.rpc.core.common.register;

import java.net.InetSocketAddress;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProviderRegister
 * @Date 2020/09/21 10:43
 */
public interface ProviderRegister {

    void registerProvider(String rpcServiceName, InetSocketAddress socketAddress);

    void unregisterProvider(String rpcServiceName, InetSocketAddress socketAddress);

}
