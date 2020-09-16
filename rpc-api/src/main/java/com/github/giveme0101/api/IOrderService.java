package com.github.giveme0101.api;

import com.github.giveme0101.api.entity.OrderVO;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Test
 * @Date 2020/09/14 10:18
 */
public interface IOrderService {

    OrderVO getOrder(String orderNo);

}
