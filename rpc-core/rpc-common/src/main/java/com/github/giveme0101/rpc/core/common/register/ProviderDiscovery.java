package com.github.giveme0101.rpc.core.common.register;

import com.github.giveme0101.rpc.core.common.loadbalance.LoadBalance;

import java.net.InetSocketAddress;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProviderDiscovery
 * @Date 2020/09/21 10:43
 */
public interface ProviderDiscovery {

    InetSocketAddress lookupProvider(String rpcServiceName);

    InetSocketAddress lookupProvider(String rpcServiceName, LoadBalance balance);

}
