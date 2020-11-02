package com.github.easyrpc.starter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcReference
 * @Date 2020/10/29 10:53
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

}
