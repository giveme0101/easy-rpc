package com.github.giveme0101.rpc.core.common;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RpcService
 * @Date 2020/09/14 16:30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = TYPE)
public @interface RpcService {

    String version() default "";

}
