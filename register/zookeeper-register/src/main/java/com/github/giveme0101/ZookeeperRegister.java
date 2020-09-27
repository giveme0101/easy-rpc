package com.github.giveme0101;

import com.github.giveme0101.rpc.core.common.loadbalance.LoadBalance;
import com.github.giveme0101.rpc.core.common.register.ProviderDiscovery;
import com.github.giveme0101.rpc.core.common.register.ProviderRegister;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ZookeeperRegister
 * @Date 2020/09/27 16:24
 */
@Slf4j
public class ZookeeperRegister implements ProviderRegister, ProviderDiscovery {

    @Override
    public InetSocketAddress lookupProvider(String rpcServiceName) {
        return null;
    }

    @Override
    public InetSocketAddress lookupProvider(String rpcServiceName, LoadBalance balance) {
        return null;
    }

    @Override
    public void registerProvider(String rpcServiceName, InetSocketAddress socketAddress) {

    }

    @Override
    public void unregisterProvider(String rpcServiceName, InetSocketAddress socketAddress) {

    }
}
