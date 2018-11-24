package com.jsoft.medpdfmaker.repository;

import java.util.List;
import java.util.Set;

public interface EntityGroupRepository<K, V> {
    
    void put(K key, V value);

    List<V> getGroupByKey(K key);

    boolean isEmpty();

    Set<String> getKeys();

    void clean();
}