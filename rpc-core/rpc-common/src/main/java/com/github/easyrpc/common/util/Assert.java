package com.github.easyrpc.common.util;

import java.util.Collection;
import java.util.Optional;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Assert
 * @Date 2020/10/30 10:33
 */
public class Assert {

    public static void notNull(Object nullable, String msg){
        Optional.ofNullable(nullable).orElseThrow(() -> new NullPointerException(msg));
    }

    public static void notEmpty(Collection nullable, String msg){
        if (nullable == null || nullable.isEmpty()) {
            throw new RuntimeException(msg);
        }
    }

}
