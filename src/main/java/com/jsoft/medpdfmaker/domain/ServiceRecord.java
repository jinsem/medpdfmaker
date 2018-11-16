package com.jsoft.medpdfmaker.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Domain entity for medial service record loaded from input file.
 */
public class ServiceRecord implements Comparable<ServiceRecord> {
    
    private String refId;
    private boolean cancelled;
    private String lName;
    private String fName;
    private String memberId;
    private LocalDate dob;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private LocalTime apptTime;
    private String origin;
    private String destination;
    private boolean wheelChairYesNo;
    private int totalPassengers;
    private String notes;
    private String telephone;
    private String coordinatorInitials;
    private String city;
    private String state;
    private String zipCode;
    private String areaCode;
    private String phone;

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLName() {
        return this.lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getFName() {
        return this.fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return this.pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalTime getApptTime() {
        return this.apptTime;
    }

    public void setApptTime(LocalTime apptTime) {
        this.apptTime = apptTime;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isWheelChairYesNo() {
        return this.wheelChairYesNo;
    }

    public void setWheelChairYesNo(boolean wheelChairYesNo) {
        this.wheelChairYesNo = wheelChairYesNo;
    }

    public int getTotalPassengers() {
        return this.totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCoordinatorInitials() {
        return this.coordinatorInitials;
    }

    public void setCoordinatorInitials(String coordinatorInitials) {
        this.coordinatorInitials = coordinatorInitials;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int compareTo(ServiceRecord o) {
        return new CompareToBuilder()
            .append(refId, o.refId)
            .append(lName, o.lName)
            .append(fName, o.fName)
            .append(memberId, o.memberId)
            .append(dob, o.dob)
            .append(pickupDate, o.pickupDate)
            .append(pickupTime, o.pickupTime)
            .toComparison();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ServiceRecord)) {
            return false;
        }
        ServiceRecord serviceRecord = (ServiceRecord) o;
        return Objects.equals(refId, serviceRecord.refId) 
               && Objects.equals(lName, serviceRecord.lName) 
               && Objects.equals(fName, serviceRecord.fName) 
               && Objects.equals(memberId, serviceRecord.memberId) 
               && Objects.equals(dob, serviceRecord.dob) 
               && Objects.equals(pickupDate, serviceRecord.pickupDate) 
               && Objects.equals(pickupTime, serviceRecord.pickupTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId, lName, fName, memberId, dob, pickupDate, pickupTime);
    }
}