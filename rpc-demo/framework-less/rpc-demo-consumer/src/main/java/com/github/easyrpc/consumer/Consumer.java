package com.github.easyrpc.consumer;

import com.github.easyrpc.common.config.ProviderConfig;
import com.github.easyrpc.core.provider.context.NettyServerContext;
import com.github.giveme0101.api.IOrderRpc;
import com.github.giveme0101.api.IProductRpc;
import com.github.giveme0101.api.entity.OrderDTO;
import com.github.giveme0101.api.entity.ProductDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Consumer
 * @Date 2020/09/14 10:20
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) {

        ProviderConfig config = new ProviderConfig();
        config.setSerializer("kryo");
        config.setRegistryAddress("redis://mypassword@192.168.200.244:6379");
        NettyServerContext context = new NettyServerContext(config);
        context.run();

        // 获取代理对象进行远程调用
        IOrderRpc orderClient = context.getClient(IOrderRpc.class);
        OrderDTO orderInfo = orderClient.getOrder("I001");
        log.info("{}", orderInfo);

        IProductRpc productClient = context.getClient(IProductRpc.class);
        Boolean save = productClient.save(ProductDTO.builder()
                .productNo("P-001")
                .productName("产品1")
                .amount(10.1)
                .build());
        log.info("{}", save);

        context.close();

    }

}
