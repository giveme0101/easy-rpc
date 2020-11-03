package com.github.easyrpc.provider.service;

import com.github.api.IProductRpc;
import com.github.api.entity.ProductDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProductServiceImpl
 * @Date 2020/09/15 17:53
 */
@Slf4j
public class ProductServiceImpl implements IProductRpc {

    @Override
    public Boolean save(ProductDTO productVO) {
        log.info("save product info: {}", productVO);
        if (null == productVO || productVO.getProductNo() == null){
            throw new NullPointerException("invalid product info");
        }
        return true;
    }

}
