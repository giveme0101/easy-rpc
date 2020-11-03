package com.github.easyrpc.core.provider.context;

import com.github.easyrpc.common.context.AbstractContext;
import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.common.config.ProviderConfig;
import com.github.easyrpc.core.provider.client.NettyClient;
import com.github.easyrpc.core.provider.factory.DefaultProxyFactory;
import com.github.easyrpc.core.provider.factory.IClientFactory;
import com.github.easyrpc.core.provider.server.NettyServer;
import com.github.easyrpc.core.provider.server.ServiceProvider;
import com.github.easyrpc.core.provider.server.ServiceProviderImpl;
import com.github.easyrpc.core.provider.util.LocalAddressHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name NettyServerContext
 * @Date 2020/10/29 13:21
 */
@Slf4j
@Getter
public class NettyServerContext extends AbstractContext implements Observer, IClientFactory {

    private Thread daemonThead;
    private NettyServer httpServer;
    private ServiceProvider serviceProvider;

    private NettyClient httpClient;
    private DefaultProxyFactory proxyFactory;

    public NettyServerContext(ProviderConfig config){
        super(config);
    }

    @Override
    protected void onRefresh() {
        initServer();
        initClient();
    }

    @Override
    protected void startServer() {
        // server
        daemonThead = new Thread(httpServer);
        daemonThead.setDaemon(true);
        daemonThead.start();
        // client
        httpClient.start();
    }

    @Override
    protected void stopServer() {
        if (null != daemonThead && daemonThead.isAlive()){
            httpServer.stop();
//            daemonThead.interrupt();
        }
        httpClient.stop();
    }

    @Override
    public <T> T getClient(Class<T> clazz) {
        return proxyFactory.getClient(clazz);
    }

    @Override
    public <T> T getClient(Class<T> clazz, String serviceName, String version) {
        return proxyFactory.getClient(clazz, serviceName, version);
    }

    private void initClient(){
        httpClient = new NettyClient(this);
        httpClient.setSerializer(serializer);
        proxyFactory = new DefaultProxyFactory(providerRegister, httpClient);
    }

    private void initServer(){

        serviceProvider = new ServiceProviderImpl();
        httpServer = new NettyServer(this);
        httpServer.setSerializer(serializer);
        httpServer.setServiceProvider(serviceProvider);

        this.deleteListener(this);
        this.addListener(this);

        List<RpcServiceReference> rpcServiceReferences = appConfig.getRpcServiceReferences();
        if (null != rpcServiceReferences && rpcServiceReferences.size() > 0){
            for (final RpcServiceReference rpcServiceReference : rpcServiceReferences) {
                serviceProvider.addService(rpcServiceReference.getInstance(), rpcServiceReference);
                log.info("load impl {} -> {}", rpcServiceReference.getServiceName(), rpcServiceReference.getInstance().getClass().getName());
            }
        }
    }

    @Override
    public void update(Observable o, Object event) {

        ContextEvent contextEvent = (ContextEvent) event;

        if (contextEvent.equals(ContextEvent.SERVER_STARTING)) {
            Iterator<Map.Entry<String, Object>> iterator = serviceProvider.iterator();
            InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                providerRegister.registerProvider(next.getKey(), socketAddress);
                log.debug("register rpc: {}", next.getKey());
            }
        }

        if (contextEvent.equals(ContextEvent.PRE_CLOSE)){
            Iterator<Map.Entry<String, Object>> iterator = serviceProvider.iterator();
            InetSocketAddress socketAddress = new InetSocketAddress(LocalAddressHolder.HOST, LocalAddressHolder.PORT);
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                providerRegister.unregisterProvider(next.getKey(), socketAddress);
                log.debug("unregister rpc: {}", next.getKey());
            }
        }
    }
}
