package com.github.easyrpc.common.register;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RegisterFactory
 * @Date 2020/10/29 16:34
 */
public interface RegisterFactory {

    ProviderRegister getInstance(String address);

    String getProtocol();

}
