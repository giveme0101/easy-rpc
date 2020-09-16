package com.github.giveme0101.api.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProductVO
 * @Date 2020/09/15 17:51
 */
@Data
@Builder
public class ProductVO implements Serializable {

    private String productNo;

    private String productName;

    private Double amount;

}
