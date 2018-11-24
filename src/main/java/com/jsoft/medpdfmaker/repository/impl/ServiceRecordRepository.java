package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.repository.EntityGroupRepository;

import java.util.*;

public class ServiceRecordRepository implements EntityGroupRepository<String, ServiceRecord> {

    private final Map<String, List<ServiceRecord>> repository = new TreeMap<>();

    @Override
    public void put(String key, ServiceRecord value) {
        repository.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @Override
    public List<ServiceRecord> getGroupByKey(String key) {
        return repository.get(key);
    }

    @Override
    public boolean isEmpty() {
        return repository.isEmpty();
    }

    @Override
    public Set<String> getKeys() {
        return repository.keySet();
	}
}