package com.github.easyrpc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderVO
 * @Date 2020/10/29 10:50
 */
@Data
@Builder
@NoArgsConstructor
public class OrderVO {

    private String orderNo;

    private Double orderAmount;

    private Date createTime;

}
