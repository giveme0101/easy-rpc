package com.github.easyrpc.common.loadbalance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name RoundRobin
 * @Date 2020/09/22 10:07
 */
public class RoundRobin implements LoadBalance{

    private static final AtomicInteger POS = new AtomicInteger(0);

    @Override
    public <T> T select(Collection<T> list) {

        if (POS.get() >= list.size()){
            POS.set(0);
        }

        return new ArrayList<>(list).get(POS.getAndAdd(1));
    }
}
