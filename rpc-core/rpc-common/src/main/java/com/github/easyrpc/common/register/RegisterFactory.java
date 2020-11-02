package com.github.easyrpc.common.register;

import com.github.easyrpc.common.spi.Named;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegisterFactory
 * @Date 2020/10/29 16:34
 */
public interface RegisterFactory extends Named {

    ProviderRegister getInstance(RegisterProperties properties);

}
