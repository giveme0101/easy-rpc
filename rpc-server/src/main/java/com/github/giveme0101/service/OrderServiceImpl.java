package com.github.giveme0101.service;

import com.github.giveme0101.api.IOrderService;
import com.github.giveme0101.rpc.RpcService;
import com.github.giveme0101.api.entity.OrderVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderServiceImpl
 * @Date 2020/09/14 15:53
 */
@Slf4j
@RpcService
public class OrderServiceImpl implements IOrderService {

    @Override
    public OrderVO getOrder(String orderNo) {
        log.info("get order info：{}", orderNo);
        return OrderVO.builder()
                .orderNo(orderNo)
                .orderAmount(10.5)
                .createTime(new Date())
                .build();
    }

}
