package com.entermoor.polyfiter.utils;

import java.util.Map;

public class OrdinaryEntry<K, V> implements Map.Entry<K, V> {
    public K key;
    public V value;

    public OrdinaryEntry(K k, V v) {
        key = k;
        value = v;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        return this.value = value;
    }
}