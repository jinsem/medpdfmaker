package com.jsoft.medpdfmaker.repository;

import java.util.List;

public interface EntityGroupRepository<K, V> {
    
    void put(K key, V value);

    List<V> getGroupByKey(K key);
}