package com.github.easyrpc.provider.rpc;

import com.github.easyrpc.starter.provider.RpcService;
import com.github.giveme0101.api.IOrderRpc;
import com.github.giveme0101.api.entity.OrderDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderRpcImpl
 * @Date 2020/10/29 11:01
 */
@Slf4j
@RpcService(serviceName = "orderRpc", version = "2.0")
public class OrderRpcImplV2 implements IOrderRpc {

    @Override
    public OrderDTO getOrder(String orderNo) {

        log.info("orderNo: {}", orderNo);
        OrderDTO orderDTO = OrderDTO.builder()
                .orderNo("V2 -- " + orderNo)
                .orderAmount(22.2)
                .createTime(new Date())
                .build();

        return orderDTO;
    }

}
