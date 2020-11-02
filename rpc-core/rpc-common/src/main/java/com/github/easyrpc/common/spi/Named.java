package com.github.easyrpc.common.spi;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 *
 * 如果使用Dubbo风格的SPI就不用实现此接口了
 *
 * @name Named
 * @Date 2020/11/02 12:15
 */
public interface Named {

   default String getName(){
       return this.getClass().getSimpleName();
   }

}
