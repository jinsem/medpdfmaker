package com.jsoft.medpdfmaker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRecordTest {

    private ServiceRecord serviceRecord;

    @BeforeEach
    void setUp() {
        serviceRecord = new ServiceRecord();
    }

    @Test
    void getRefId() {
        assertNull(serviceRecord.getRefId());
        String val = "123";
        serviceRecord.setRefId(val);
        assertEquals(val, serviceRecord.getRefId());
    }

    @Test
    void isCancelled() {
        assertFalse(serviceRecord.isCancelled());
        serviceRecord.setCancelled(true);
        assertTrue(serviceRecord.isCancelled());
    }

    @Test
    void getLName() {
        assertNull(serviceRecord.getLName());
        String val = "lname";
        serviceRecord.setLName(val);
        assertEquals(val, serviceRecord.getLName());
    }

    @Test
    void getFName() {
        assertNull(serviceRecord.getFName());
        String val = "fname";
        serviceRecord.setFName(val);
        assertEquals(val, serviceRecord.getFName());
    }

    @Test
    void getFAndLName() {
        final String valF = "fname";
        final String valL = "lname";
        assertNull(serviceRecord.getFAndLName());
        serviceRecord.setFName(valF);
        assertEquals(valF, serviceRecord.getFAndLName());
        serviceRecord.setFName(null);
        serviceRecord.setLName(valL);
        assertEquals(valL, serviceRecord.getFAndLName());
        serviceRecord.setFName(valF);
        assertEquals(valF + " " + valL, serviceRecord.getFAndLName());
    }

    @Test
    void getMemberId() {
        assertNull(serviceRecord.getMemberId());
        String val = "555";
        serviceRecord.setMemberId(val);
        assertEquals(val, serviceRecord.getMemberId());
    }

    @Test
    void getDayOfBirth() {
        assertNull(serviceRecord.getDayOfBirth());
        LocalDate val = LocalDate.now();
        serviceRecord.setDayOfBirth(val);
        assertEquals(val, serviceRecord.getDayOfBirth());
    }

    @Test
    void getPickupDate() {
        assertNull(serviceRecord.getPickupDate());
        LocalDate val = LocalDate.now();
        serviceRecord.setPickupDate(val);
        assertEquals(val, serviceRecord.getPickupDate());
    }

    @Test
    void getPickupTime() {
        assertNull(serviceRecord.getPickupTime());
        LocalTime val = LocalTime.now();
        serviceRecord.setPickupTime(val);
        assertEquals(val, serviceRecord.getPickupTime());
    }

    @Test
    void getApptTime() {
        assertNull(serviceRecord.getApptTime());
        LocalTime val = LocalTime.now();
        serviceRecord.setApptTime(val);
        assertEquals(val, serviceRecord.getApptTime());
    }

    @Test
    void getOrigin() {
        assertNull(serviceRecord.getOrigin());
        String val = "555";
        serviceRecord.setOrigin(val);
        assertEquals(val, serviceRecord.getOrigin());
    }

    @Test
    void getDestination() {
        assertNull(serviceRecord.getDestination());
        String val = "555";
        serviceRecord.setDestination(val);
        assertEquals(val, serviceRecord.getDestination());
    }

    @Test
    void isWheelChairYesNo() {
        assertFalse(serviceRecord.isWheelChairYesNo());
        serviceRecord.setWheelChairYesNo(true);
        assertTrue(serviceRecord.isWheelChairYesNo());
    }

    @Test
    void getTotalPassengers() {
        assertNull(serviceRecord.getTotalPassengers());
        Integer val = 12;
        serviceRecord.setTotalPassengers(val);
        assertEquals(val, serviceRecord.getTotalPassengers());
    }

    @Test
    void getNotes() {
        assertNull(serviceRecord.getNotes());
        String val = "abc";
        serviceRecord.setNotes(val);
        assertEquals(val, serviceRecord.getNotes());
    }

    @Test
    void getTelephone() {
        assertNull(serviceRecord.getTelephone());
        String val = "abc";
        serviceRecord.setTelephone(val);
        assertEquals(val, serviceRecord.getTelephone());
    }

    @Test
    void getCoordinatorInitials() {
        assertNull(serviceRecord.getCoordinatorInitials());
        String val = "abc";
        serviceRecord.setCoordinatorInitials(val);
        assertEquals(val, serviceRecord.getCoordinatorInitials());
    }

    @Test
    void getCity() {
        assertNull(serviceRecord.getCity());
        String val = "abc";
        serviceRecord.setCity(val);
        assertEquals(val, serviceRecord.getCity());
    }

    @Test
    void getState() {
        assertNull(serviceRecord.getState());
        String val = "abc";
        serviceRecord.setState(val);
        assertEquals(val, serviceRecord.getState());
    }

    @Test
    void getZipCode() {
        assertNull(serviceRecord.getZipCode());
        String val = "abc";
        serviceRecord.setZipCode(val);
        assertEquals(val, serviceRecord.getZipCode());
    }

    @Test
    void getAreaCode() {
        assertNull(serviceRecord.getAreaCode());
        String val = "abc";
        serviceRecord.setAreaCode(val);
        assertEquals(val, serviceRecord.getAreaCode());
    }

    @Test
    void getPhone() {
        assertNull(serviceRecord.getPhone());
        String val = "abc";
        serviceRecord.setPhone(val);
        assertEquals(val, serviceRecord.getPhone());
    }

    @Test
    void compareToEqualsHashCode() {
        ServiceRecord r1 = new ServiceRecord();
        ServiceRecord r2 = new ServiceRecord();
        assertEquals(r1, r1);
        assertNotEquals(r1, "different classes");

        makeEmEqual(r1, r2);
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(0, r1.compareTo(r2));
        assertEquals(0, r2.compareTo(r1));

        r1.setRefId("a");
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setFName("a");
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setLName("a");
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setMemberId("a");
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setDayOfBirth(LocalDate.MIN);
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setPickupDate(LocalDate.MIN);
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());

        makeEmEqual(r1, r2);
        r1.setPickupTime(LocalTime.MIN);
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
        assertNotEquals(r1, r2);
        assertNotEquals(r2, r1);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    private void makeEmEqual(ServiceRecord r1, ServiceRecord r2) {
        r1.setRefId("refid");
        r2.setRefId("refid");
        r1.setFName("fname");
        r2.setFName("fname");
        r1.setLName("lname");
        r2.setLName("lname");
        r1.setMemberId("setMemberId");
        r2.setMemberId("setMemberId");
        LocalDate dob = LocalDate.of(2010, 4, 2);
        r1.setDayOfBirth(dob);
        r2.setDayOfBirth(dob);
        LocalDate pd = LocalDate.of(2010, 4, 2);
        r1.setPickupDate(pd);
        r2.setPickupDate(pd);
        LocalTime pt = LocalTime.of(10, 30);
        r1.setPickupTime(pt);
        r2.setPickupTime(pt);
    }
}