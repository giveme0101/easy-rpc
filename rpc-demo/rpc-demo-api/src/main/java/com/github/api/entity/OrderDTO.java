package com.github.api.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name OrderDTO
 * @Date 2020/09/15 17:16
 */
@Data
@Builder
public class OrderDTO implements Serializable {

    private String orderNo;

    private Double orderAmount;

    private Date createTime;

}
