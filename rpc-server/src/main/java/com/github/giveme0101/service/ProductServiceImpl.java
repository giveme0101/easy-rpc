package com.github.giveme0101.service;

import com.github.giveme0101.api.IProductService;
import com.github.giveme0101.rpc.RpcService;
import com.github.giveme0101.api.entity.ProductVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ProductServiceImpl
 * @Date 2020/09/15 17:53
 */
@Slf4j
@RpcService
public class ProductServiceImpl implements IProductService {

    @Override
    public Boolean save(ProductVO productVO) {
        log.info("save product info: {}", productVO);
        if (null == productVO || productVO.getProductNo() == null){
            throw new NullPointerException("invalid product info");
        }
        return true;
    }

}
