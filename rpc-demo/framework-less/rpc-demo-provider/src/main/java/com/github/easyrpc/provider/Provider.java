package com.github.easyrpc.provider;

import com.github.easyrpc.common.entity.RpcServiceReference;
import com.github.easyrpc.common.config.ProviderConfig;
import com.github.easyrpc.core.provider.context.NettyServerContext;
import com.github.easyrpc.provider.service.OrderServiceImpl;
import com.github.easyrpc.provider.service.ProductServiceImpl;
import com.github.giveme0101.api.IOrderRpc;
import com.github.giveme0101.api.IProductRpc;

import java.util.concurrent.TimeUnit;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Provider
 * @Date 2020/09/14 13:58
 */
public class Provider {

    public static void main(String[] args) {

        ProviderConfig config = new ProviderConfig();
        config.setSerializer("kryo");
        config.setRegistryAddress("redis://mypassword@192.168.200.244:6379");
        config.addRpcServiceReference(RpcServiceReference.builder()
                .serviceName(IOrderRpc.class.getName())
                .interfaceClass(IOrderRpc.class)
                .instance(new OrderServiceImpl())
                .build());
        config.addRpcServiceReference(RpcServiceReference.builder()
                .serviceName(IProductRpc.class.getName())
                .interfaceClass(IProductRpc.class)
                .instance(new ProductServiceImpl())
                .build());

        NettyServerContext context = new NettyServerContext(config);
        context.run();

        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.close();
    }

}
