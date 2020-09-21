package com.github.giveme0101.rpc.core.server.context;

import com.github.giveme0101.rpc.core.common.IContext;
import com.github.giveme0101.rpc.core.common.serialize.protostuff.ProtoStuffSerializer;
import com.github.giveme0101.rpc.core.server.server.RpcServer;
import com.github.giveme0101.rpc.core.common.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ServerContext
 * @Date 2020/09/16 14:48
 */
@Slf4j
public class ServerContext implements IContext {

    private int port;
    private String implPackage;
    private RpcServer server;

    public ServerContext(){
        init();
        start();
    }

    @Override
    public void init() {
        Properties properties = PropertiesUtil.load("application.properties");
        this.port = Integer.valueOf(properties.getProperty("server.port", "6699"));
        this.implPackage = properties.getProperty("service.impl.package");
    }

    @Override
    public void start() {
        server = new RpcServer(port)
                .setImplPackage(implPackage)
                .setSerializer(new ProtoStuffSerializer());
        server.start();
    }

    @Override
    public void stop() {
        server.stop();
    }
}
