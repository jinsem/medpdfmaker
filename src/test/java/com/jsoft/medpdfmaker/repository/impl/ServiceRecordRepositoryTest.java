package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(new ServiceRecordKey("key", BigDecimal.ZERO), null));
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(null, null));
    }

    @Test
    void putAndGetKeys() {
        Set<ServiceRecordKey> expected = new HashSet<>(Arrays.asList(
                new ServiceRecordKey("k1", BigDecimal.ZERO),
                new ServiceRecordKey("k2", BigDecimal.ZERO),
                new ServiceRecordKey("k3", BigDecimal.ZERO)
        )
        );
        for (ServiceRecordKey anExpected : expected) {
            serviceRecordRepository.put(anExpected, new ServiceRecord());
        }
        final Set<ServiceRecordKey> actual = serviceRecordRepository.getKeys();
        assertEquals(expected, actual);
    }

    @Test
    void putAndGetGroupByKey() {
        Set<ServiceRecordKey> groupsNames = new HashSet<>(Arrays.asList(
                new ServiceRecordKey("k1", BigDecimal.ZERO),
                new ServiceRecordKey("k2", BigDecimal.ZERO),
                new ServiceRecordKey("k3", BigDecimal.ZERO)
        )
        );
        for (ServiceRecordKey groupsName : groupsNames) {
            for (int i = 0; i < 3; i++) {
                final ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.setMemberId("Member of " + groupsName);
                serviceRecordRepository.put(groupsName, serviceRecord);
            }
        }
        assertNull(serviceRecordRepository.getGroupByKey(new ServiceRecordKey("I am invalid key", BigDecimal.ZERO)));
        for (ServiceRecordKey groupsName : groupsNames) {
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
        serviceRecordRepository.put(new ServiceRecordKey("some-key", BigDecimal.ZERO), new ServiceRecord());
        assertFalse(serviceRecordRepository.isEmpty());
        serviceRecordRepository.clean();
        assertTrue(serviceRecordRepository.isEmpty());
    }
}