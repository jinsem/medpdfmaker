package com.jsoft.medpdfmaker.repository.impl;

import com.jsoft.medpdfmaker.domain.MemberIdPriceKey;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordGroupKey;
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
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(new MemberIdPriceKey(makeSerivceRecord("key", BigDecimal.ZERO)), null));
        assertThrows(IllegalArgumentException.class, () -> serviceRecordRepository.put(null, null));
    }

    private ServiceRecord makeSerivceRecord(String memberId, BigDecimal price) {
        final ServiceRecord result = new ServiceRecord();
        result.setMemberId(memberId);
        result.setTripPrice(price);
        return result;
    }

    @Test
    void putAndGetKeys() {
        Set<MemberIdPriceKey> expected = new HashSet<>(Arrays.asList(
                new MemberIdPriceKey(makeSerivceRecord("k1", BigDecimal.ZERO)),
                new MemberIdPriceKey(makeSerivceRecord("k2", BigDecimal.ZERO)),
                new MemberIdPriceKey(makeSerivceRecord("k3", BigDecimal.ZERO))
        )
        );
        for (MemberIdPriceKey anExpected : expected) {
            serviceRecordRepository.put(anExpected, new ServiceRecord());
        }
        final Set<ServiceRecordGroupKey> actual = serviceRecordRepository.getKeys();
        assertEquals(expected, actual);
    }

    @Test
    void putAndGetGroupByKey() {
        Set<MemberIdPriceKey> groupsNames = new HashSet<>(Arrays.asList(
                new MemberIdPriceKey(makeSerivceRecord("k1", BigDecimal.ZERO)),
                new MemberIdPriceKey(makeSerivceRecord("k2", BigDecimal.ZERO)),
                new MemberIdPriceKey(makeSerivceRecord("k3", BigDecimal.ZERO))
        )
        );
        for (MemberIdPriceKey groupsName : groupsNames) {
            for (int i = 0; i < 3; i++) {
                final ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.setMemberId("Member of " + groupsName);
                serviceRecordRepository.put(groupsName, serviceRecord);
            }
        }
        assertNull(serviceRecordRepository.getGroupByKey(new MemberIdPriceKey(makeSerivceRecord("I am invalid key", BigDecimal.ZERO))));
        for (MemberIdPriceKey groupsName : groupsNames) {
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
        serviceRecordRepository.put(new MemberIdPriceKey(makeSerivceRecord("some-key", BigDecimal.ZERO)), new ServiceRecord());
        assertFalse(serviceRecordRepository.isEmpty());
        serviceRecordRepository.clean();
        assertTrue(serviceRecordRepository.isEmpty());
    }
}