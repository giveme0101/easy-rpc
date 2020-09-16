package com.github.giveme0101.rpc.client.context;

import com.github.giveme0101.rpc.IContext;
import com.github.giveme0101.rpc.client.client.RpcClient;
import com.github.giveme0101.rpc.util.PropertiesUtil;

import java.util.Properties;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ClientContext
 * @Date 2020/09/14 10:23
 */
public class ClientContext extends AbstractFactory implements IContext {

    private int port;
    private String host;
    private RpcClient rpcClient;

    public ClientContext(){
        init();
        start();
    }

    @Override
    public void init() {
        Properties properties = PropertiesUtil.load("application.properties");
        host = properties.getProperty("server.host", "localhost");
        port = Integer.valueOf(properties.getProperty("server.port", "6699"));
    }

    @Override
    public void start() {
        rpcClient = new RpcClient(host, port);
        rpcClient.start();
    }

    @Override
    public void stop() {
        rpcClient.stop();
    }

}