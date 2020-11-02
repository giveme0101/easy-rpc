package com.github.easyrpc.starter.provider;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcScan
 * @Date 2020/10/29 11:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServiceRegistrar.class)
public @interface RpcScan {

    String[] basePackages();

}
