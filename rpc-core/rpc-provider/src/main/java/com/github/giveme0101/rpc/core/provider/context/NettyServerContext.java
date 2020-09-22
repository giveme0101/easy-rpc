package com.github.giveme0101.rpc.core.provider.context;

import com.github.giveme0101.rpc.core.common.config.RegistryConfig;
import com.github.giveme0101.rpc.core.common.util.PropertiesUtil;
import com.github.giveme0101.rpc.core.provider.server.NettyServer;
import com.github.giveme0101.rpc.core.provider.RedisRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ServerContext
 * @Date 2020/09/16 14:48
 */
@Slf4j
@Getter
public class NettyServerContext extends AbstractContext {

    private NettyServer server;
    private RedisRegistry redisRegistry;

    public NettyServerContext(){
        super();
    }

    @Override
    protected void startServer() {
        server = new NettyServer(this);
        new Thread(server).start();
    }

    @Override
    protected void stopServer() {
        server.stop();
    }

    @Override
    protected void afterPropertiesRead(Properties properties) {
        RegistryConfig registryConfig = PropertiesUtil.parseRegistryConfig(properties);
        redisRegistry = new RedisRegistry(registryConfig);
        this.addObserver(redisRegistry);
    }
}