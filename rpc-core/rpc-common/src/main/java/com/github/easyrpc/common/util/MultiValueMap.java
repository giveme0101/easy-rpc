package com.github.easyrpc.common.util;

import java.util.List;
import java.util.Map;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name MultiValueMap
 * @Date 2020/10/30 9:25
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    V getFirst(K key);

    void add(K key, V value);

    void addAll(K key, List<V> valueList);

    void addAll(MultiValueMap<K, V> values);

    Map<K, V> toSingleValueMap();

}
