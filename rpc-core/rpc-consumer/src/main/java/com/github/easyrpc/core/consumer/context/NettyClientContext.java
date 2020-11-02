package com.github.easyrpc.core.consumer.context;

import com.github.easyrpc.common.context.AbstractContext;
import com.github.easyrpc.core.consumer.client.NettyClient;
import com.github.easyrpc.core.consumer.factory.DefaultProxyFactory;
import com.github.easyrpc.core.consumer.factory.IClientFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ClientContext
 * @Date 2020/09/14 10:23
 */
@Slf4j
public class NettyClientContext extends AbstractContext implements IClientFactory {

    private NettyClient httpServer;
    private DefaultProxyFactory proxyFactory;

    public NettyClientContext(){
        super(null);
    }

    @Override
    protected void onRefresh() {
        httpServer = new NettyClient(this);
        httpServer.setSerializer(serializer);
        proxyFactory = new DefaultProxyFactory(providerRegister, httpServer);
    }

    @Override
    protected void onPostRefresh() {
        // 自动运行
        super.run();
    }

    @Override
    public void startServer() {
        httpServer.start();
    }

    @Override
    public void stopServer() {
        httpServer.stop();
    }

    @Override
    public <T> T getClient(Class<T> clazz) {
        return proxyFactory.getClient(clazz);
    }

}