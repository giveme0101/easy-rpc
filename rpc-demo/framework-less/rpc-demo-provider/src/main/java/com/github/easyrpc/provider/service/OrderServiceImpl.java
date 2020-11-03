package com.github.easyrpc.provider.service;

import com.github.api.IOrderRpc;
import com.github.api.entity.OrderDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderServiceImpl
 * @Date 2020/09/14 15:53
 */
@Slf4j
public class OrderServiceImpl implements IOrderRpc {

    @Override
    public OrderDTO getOrder(String orderNo) {
        log.info("get order info：{}", orderNo);
        return OrderDTO.builder()
                .orderNo(orderNo)
                .orderAmount(10.5)
                .createTime(new Date())
                .build();
    }

}
