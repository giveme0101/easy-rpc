package com.github.easyrpc.common.loadbalance;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Random
 * @Date 2020/09/22 9:33
 */
public class Random implements LoadBalance  {

    @Override
    public <T> T select(Collection<T> collection) {
        int num = new java.util.Random().nextInt(collection.size());
        return new ArrayList<>(collection).get(num);
    }

}
