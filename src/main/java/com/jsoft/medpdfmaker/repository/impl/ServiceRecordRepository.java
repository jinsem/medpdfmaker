package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordKey;
import com.jsoft.medpdfmaker.repository.EntityGroupRepository;

import java.util.*;

public class ServiceRecordRepository implements EntityGroupRepository<ServiceRecordKey, ServiceRecord> {

    private final Map<ServiceRecordKey, List<ServiceRecord>> repository = new TreeMap<>();

    @Override
    public void put(ServiceRecordKey key, ServiceRecord value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Both key and value cannot be null");
        }
        repository.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @Override
    public List<ServiceRecord> getGroupByKey(ServiceRecordKey key) {
        return repository.get(key);
    }

    @Override
    public boolean isEmpty() {
        return repository.isEmpty();
    }

    @Override
    public Set<ServiceRecordKey> getKeys() {
        return repository.keySet();
	}

    @Override
    public void clean() {
        repository.clear();
    }
}