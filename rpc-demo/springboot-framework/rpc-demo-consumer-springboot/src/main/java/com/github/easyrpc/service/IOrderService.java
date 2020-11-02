package com.github.easyrpc.service;

import com.github.easyrpc.entity.OrderVO;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name IOrderService
 * @Date 2020/10/29 10:44
 */
public interface IOrderService {

    OrderVO getOrder(String orderNo);

}
