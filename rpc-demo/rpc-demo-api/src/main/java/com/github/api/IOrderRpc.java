package com.github.api;

import com.github.api.entity.OrderDTO;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name IOrderRpc
 * @Date 2020/09/14 10:18
 */
public interface IOrderRpc {

    OrderDTO getOrder(String orderNo);

}
