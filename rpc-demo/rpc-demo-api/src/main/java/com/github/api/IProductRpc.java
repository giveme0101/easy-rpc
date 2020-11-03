package com.github.api;

import com.github.api.entity.ProductDTO;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name IProductRpc
 * @Date 2020/09/14 10:18
 */
public interface IProductRpc {

    Boolean save(ProductDTO productVO);

}
