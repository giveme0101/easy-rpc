package com.github.easyrpc.common.register;

import com.github.easyrpc.common.loadbalance.LoadBalance;

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

    InetSocketAddress lookupProvider(String rpcServiceName);

    InetSocketAddress lookupProvider(String rpcServiceName, LoadBalance balance);

}
