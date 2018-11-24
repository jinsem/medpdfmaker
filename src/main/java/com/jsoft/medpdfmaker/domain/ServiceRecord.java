package com.jsoft.medpdfmaker.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

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
    private Integer totalPassengers;
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

    @ExternalField("REF_ID")
    public void setRefId(String refId) {
        this.refId = refId;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    @ExternalField(value = "CANCELLED", fieldType = FieldType.BOOLEAN)
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLName() {
        return this.lName;
    }

    @ExternalField("LNAME")
    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getFName() {
        return this.fName;
    }

    @ExternalField(value = "FNAME", required = true)
    public void setFName(String fName) {
        this.fName = fName;
    }


    public String getFAndLName() {
        return String.format("%s %s", fName, lName);
    }

    public String getMemberId() {
        return this.memberId;
    }

    @ExternalField("MEMBERID")
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    @ExternalField(value = "DOB", fieldType = FieldType.DATE)
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    @ExternalField(value = "PICKUP_DATE", fieldType = FieldType.DATE)
    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return this.pickupTime;
    }

    @ExternalField(value = "PICKUP_TIME", fieldType = FieldType.TIME)
    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalTime getApptTime() {
        return this.apptTime;
    }

    @ExternalField(value = "APPT_TIME", fieldType = FieldType.TIME)
    public void setApptTime(LocalTime apptTime) {
        this.apptTime = apptTime;
    }

    public String getOrigin() {
        return this.origin;
    }

    @ExternalField("ORIGIN")
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    @ExternalField("DESTINATION")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isWheelChairYesNo() {
        return this.wheelChairYesNo;
    }

    @ExternalField(value = "WHEELCHAIR_YESNO", fieldType = FieldType.BOOLEAN)
    public void setWheelChairYesNo(boolean wheelChairYesNo) {
        this.wheelChairYesNo = wheelChairYesNo;
    }

    public Integer getTotalPassengers() {
        return this.totalPassengers;
    }

    @ExternalField(value = "TOTAL_PASSENGERS", fieldType = FieldType.INTEGER)
    public void setTotalPassengers(Integer totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public String getNotes() {
        return this.notes;
    }

    @ExternalField("NOTES")
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTelephone() {
        return this.telephone;
    }

    @ExternalField("TELEPHONE")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCoordinatorInitials() {
        return this.coordinatorInitials;
    }

    @ExternalField("COORDINATOR_INITIALS")
    public void setCoordinatorInitials(String coordinatorInitials) {
        this.coordinatorInitials = coordinatorInitials;
    }

    public String getCity() {
        return this.city;
    }

    @ExternalField("CITY")
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    @ExternalField("STATE")
    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    @ExternalField("ZIPCODE")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    @ExternalField("AREACODE")
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return this.phone;
    }

    @ExternalField("PHONE")
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