package com.github.easyrpc.common.util;

import java.util.*;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name MultiValueMap
 * @Date 2020/10/30 9:20
 */
public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V> {

    private final Map<K, List<V>> targetMap;

    public LinkedMultiValueMap() {
        this.targetMap = new LinkedHashMap<>();
    }

    @Override
    public V getFirst(K key) {
        List<V> vs = targetMap.get(key);
        return vs == null || vs.size() == 0 ? null : vs.get(0);
    }

    @Override
    public void add(K key, V value) {
        List<V> vs = targetMap.computeIfAbsent(key, v -> new LinkedList<>());
        vs.add(value);
    }

    @Override
    public void addAll(K key, List<V> valueList) {
        List<V> vs = targetMap.computeIfAbsent(key, v -> new LinkedList<>());
        vs.addAll(valueList);
    }

    @Override
    public void addAll(MultiValueMap<K, V> values) {
        for (Entry<K, List<V>> entry : values.entrySet()) {
            addAll(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.targetMap.size());
        this.targetMap.forEach((key, values) -> {
            if (values != null && !values.isEmpty()) {
                singleValueMap.put(key, values.get(0));
            }
        });
        return singleValueMap;
    }

    @Override
    public int size() {
        return targetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return targetMap.containsValue(value);
    }

    @Override
    public List<V> get(Object key) {
        return targetMap.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return targetMap.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        targetMap.putAll(m);
    }

    @Override
    public void clear() {
        targetMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return targetMap.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return targetMap.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return targetMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return targetMap.equals(o);
    }

    @Override
    public int hashCode() {
        return targetMap.hashCode();
    }

    @Override
    public String toString() {
        return targetMap.toString();
    }
}
