package com.github.giveme0101.provider;

import com.github.giveme0101.rpc.core.provider.context.NettyServerContext;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Provider
 * @Date 2020/09/14 13:58
 */
public class Provider {

    public static void main(String[] args) {

        NettyServerContext context = new NettyServerContext();

        // 获取项目配置
        String host = context.getProperty("registry.host");
        int port = Integer.valueOf(context.getProperty("registry.port"));
        String password = context.getProperty("registry.password");

        // 配置注册中心
        context.addEventListener(new RedisRegistry(host, password, port));

        // 启动服务
        context.start();

    }

}
