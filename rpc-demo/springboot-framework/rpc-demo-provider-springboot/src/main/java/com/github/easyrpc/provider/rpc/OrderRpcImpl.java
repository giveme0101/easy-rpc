package com.github.easyrpc.provider.rpc;

import com.github.giveme0101.api.IOrderRpc;
import com.github.giveme0101.api.entity.OrderDTO;
import com.github.easyrpc.core.provider.RpcService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderRpcImpl
 * @Date 2020/10/29 11:01
 */
@Slf4j
@RpcService
public class OrderRpcImpl implements IOrderRpc {

    @Override
    public OrderDTO getOrder(String orderNo) {

        log.info("orderNo: {}", orderNo);
        OrderDTO orderDTO = OrderDTO.builder()
                .orderNo(orderNo)
                .orderAmount(10.1)
                .createTime(new Date())
                .build();

        return orderDTO;
    }

}
