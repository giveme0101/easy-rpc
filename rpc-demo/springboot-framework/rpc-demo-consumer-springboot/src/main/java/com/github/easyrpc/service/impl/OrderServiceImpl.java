package com.github.easyrpc.service.impl;

import com.github.easyrpc.entity.OrderVO;
import com.github.easyrpc.service.IOrderService;
import com.github.easyrpc.starter.RpcReference;
import com.github.giveme0101.api.IOrderRpc;
import com.github.giveme0101.api.entity.OrderDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderServiceImpl
 * @Date 2020/10/29 10:48
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @RpcReference(serviceName = "orderRpc", version = "1.0")
    private IOrderRpc orderRpc;

    @Override
    public OrderVO getOrder(String orderNo) {

        OrderDTO order = orderRpc.getOrder(orderNo);
        if (null == order){
            return null;
        }

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        return vo;
    }

}
