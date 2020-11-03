package com.github.easyrpc.core.provider.context;

import com.github.easyrpc.common.context.AbstractContext;
import com.github.easyrpc.common.entity.ContextEvent;
import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.core.provider.ProviderConfig;
import com.github.easyrpc.core.provider.server.NettyServer;
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
public class NettyServerContext extends AbstractContext implements Observer {

    private Thread daemonThead;
    private NettyServer httpServer;
    private ServiceProvider serviceProvider;

    public NettyServerContext(ProviderConfig config){
        super(config);
    }

    @Override
    protected void onRefresh() {
        this.prepare();
        this.registerEventListener();
        this.loadRpcProviderService();
        this.initNettyServer();
    }

    @Override
    protected void startServer() {
        daemonThead = new Thread(httpServer);
        daemonThead.setDaemon(true);
        daemonThead.start();
    }

    @Override
    protected void stopServer() {
        if (null != daemonThead && daemonThead.isAlive()){
            httpServer.stop();
            daemonThead.interrupt();
        }
    }

    protected void prepare(){
        serviceProvider = new ServiceProviderImpl();
    }

    protected void registerEventListener(){
        this.deleteListener(this);
        this.addListener(this);
    }

    protected void loadRpcProviderService(){
        ProviderConfig providerConfig = (ProviderConfig) appConfig;
        List<RpcServiceReference> rpcServiceReferences = providerConfig.getRpcServiceReferences();
        if (null != rpcServiceReferences && rpcServiceReferences.size() > 0){
            for (final RpcServiceReference rpcServiceReference : rpcServiceReferences) {
                serviceProvider.addService(rpcServiceReference.getInstance(), rpcServiceReference);
                log.info("load impl {} -> {}", rpcServiceReference.getServiceName(), rpcServiceReference.getInstance().getClass().getName());
            }
        }
    }

    protected void initNettyServer(){
        httpServer = new NettyServer(this);
        httpServer.setSerializer(serializer);
        httpServer.setServiceProvider(serviceProvider);
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
