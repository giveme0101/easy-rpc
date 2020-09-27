package com.github.giveme0101.rpc.core.common.loadbalance;

import java.util.Collection;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name LoadBalance
 * @Date 2020/09/22 9:32
 */
public interface LoadBalance {

    <T> T select(Collection<T> list);

}
