package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordGroupKey;
import com.jsoft.medpdfmaker.repository.EntityGroupRepository;

import java.util.*;

public class ServiceRecordRepository implements EntityGroupRepository<ServiceRecordGroupKey, ServiceRecord> {

    private final Map<ServiceRecordGroupKey, List<ServiceRecord>> repository = new TreeMap<>();

    @Override
    public void put(ServiceRecordGroupKey key, ServiceRecord value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Both key and value cannot be null");
        }
        repository.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @Override
    public List<ServiceRecord> getGroupByKey(ServiceRecordGroupKey key) {
        return repository.get(key);
    }

    @Override
    public boolean isEmpty() {
        return repository.isEmpty();
    }

    @Override
    public Set<ServiceRecordGroupKey> getKeys() {
        return repository.keySet();
	}

    @Override
    public void clean() {
        repository.clear();
    }
}