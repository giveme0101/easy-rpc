package com.github.easyrpc.provider;

import com.github.easyrpc.core.provider.context.NettyServerContext;

import java.util.concurrent.TimeUnit;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Provider
 * @Date 2020/09/14 13:58
 */
public class Provider {

    public static void main(String[] args) {

        NettyServerContext context = new NettyServerContext();
        context.run();

        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.close();
    }

}
