package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRecordRepositoryTest {

    private ServiceRecordRepository serviceRecordRepository;

    @BeforeEach
    void setUp() {
        serviceRecordRepository = new ServiceRecordRepository();
    }

    @Test
    void putInvalid() {
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(null, new ServiceRecord()));
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put("key", null));
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(null, null));
    }

    @Test
    void putAndGetKeys() {
        Set<String> expected = new HashSet<>(Arrays.asList("k1", "k2", "k3"));
        for (String anExpected : expected) {
            serviceRecordRepository.put(anExpected, new ServiceRecord());
        }
        final Set<String> actual = serviceRecordRepository.getKeys();
        assertEquals(expected, actual);
    }

    @Test
    void putAndGetGroupByKey() {
        Set<String> groupsNames = new HashSet<>(Arrays.asList("k1", "k2", "k3"));
        for (String groupsName : groupsNames) {
            for (int i = 0; i < 3; i++) {
                final ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.setMemberId("Member of " + groupsName);
                serviceRecordRepository.put(groupsName, serviceRecord);
            }
        }
        assertNull(serviceRecordRepository.getGroupByKey("I am invalid key"));
        for (String groupsName : groupsNames) {
            List<ServiceRecord> actualRecords = serviceRecordRepository.getGroupByKey(groupsName);
            assertNotNull(actualRecords);
            assertEquals(3, actualRecords.size());
            for (ServiceRecord actualRecord : actualRecords) {
                assertEquals("Member of " + groupsName, actualRecord.getMemberId());
            }
        }
    }

    @Test
    void isEmptyAndClen() {
        assertTrue(serviceRecordRepository.isEmpty());
        serviceRecordRepository.put("some-key", new ServiceRecord());
        assertFalse(serviceRecordRepository.isEmpty());
        serviceRecordRepository.clean();
        assertTrue(serviceRecordRepository.isEmpty());
    }
}