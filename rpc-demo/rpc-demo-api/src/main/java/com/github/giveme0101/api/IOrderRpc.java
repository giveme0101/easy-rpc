package com.github.giveme0101.api;

import com.github.giveme0101.api.entity.OrderDTO;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name IOrderRpc
 * @Date 2020/09/14 10:18
 */
public interface IOrderRpc {

    OrderDTO getOrder(String orderNo);

}
