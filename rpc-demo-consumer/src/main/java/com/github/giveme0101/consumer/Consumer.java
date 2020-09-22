package com.github.giveme0101.consumer;

import com.github.giveme0101.api.IOrderService;
import com.github.giveme0101.api.IProductService;
import com.github.giveme0101.api.entity.OrderVO;
import com.github.giveme0101.api.entity.ProductVO;
import com.github.giveme0101.rpc.core.consumer.context.NettyClientContext;
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

        NettyClientContext context = new NettyClientContext();

        IOrderService orderClient = context.getClient(IOrderService.class);
        OrderVO orderInfo = orderClient.getOrder("I001");
        log.info("{}", orderInfo);

        IProductService productClient = context.getClient(IProductService.class);
        Boolean save = productClient.save(ProductVO.builder()
                .productNo("P-001")
                .productName("产品1")
                .amount(10.1)
                .build());
        log.info("{}", save);
    }

}
