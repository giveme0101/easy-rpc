package com.github.easyrpc.controller;

import com.github.easyrpc.entity.OrderVO;
import com.github.easyrpc.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderController
 * @Date 2020/10/29 10:43
 */
@RestController
@AllArgsConstructor
public class OrderController {

    private IOrderService orderService;

    @GetMapping(value = "/order")
    public OrderVO getOrder(@RequestParam(value = "orderNo") String orderNo){
        return orderService.getOrder(orderNo);
    }

}
