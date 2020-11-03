package com.github.easyrpc.common.util;

import com.github.easyrpc.common.Version;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name BannerPrinter
 * @Date 2020/11/02 9:52
 */
public class BannerPrinter {

    public static void print(){
        System.out.println("┬─┐┬─┐┐─┐┐ ┬  ┬─┐┬─┐┌─┐\n" +
                "├─ │─┤└─┐└┌┘──│┬┘│─┘│  \n" +
                "┴─┘┘ ┆──┘ ┆   ┆└┘┆  └─┘\n");
        System.out.println("version: " + Version.version);
    }

}
