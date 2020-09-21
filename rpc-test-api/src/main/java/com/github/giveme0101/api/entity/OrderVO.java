package com.github.giveme0101.api.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderVO
 * @Date 2020/09/15 17:16
 */
@Data
@Builder
public class OrderVO implements Serializable {

    private String orderNo;

    private Double orderAmount;

    private Date createTime;

}
