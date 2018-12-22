package com.jsoft.medpdfmaker.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Domain entity for medical service record
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ServiceRecord implements Comparable<ServiceRecord>, DomainEntity {

    /**
     * Service record reference ID
     */
    private String refId;

    /**
     * Flag that marks cancelled trips that must not be included in the final PDF file.
     */
    private boolean cancelled;

    /**
     * Patient's last name
     */
    private String lName;

    /**
     * Patient's first name
     */
    private String fName;

    /**
     * Patient's member ID
     */
    private String memberId;

    /**
     * Patient's day of birth
     */
    private LocalDate dayOfBirth;

    /**
     * Date when patient was picked up
     */
    private LocalDate pickupDate;

    /**
     * Time when patient was picked up
     */
    private LocalTime pickupTime;

    /**
     * Time of the patient's appointment
     */
    private LocalTime apptTime;

    /**
     * Patient's trip origin
     */
    private String origin;

    /**
     * Patient's trip destination
     */
    private String destination;

    /**
     * Flag that marks services where wheel chair was used.
     */
    private boolean wheelChairYesNo;

    /**
     * Total number of passengers in this service trip
     */
    private Integer totalPassengers;

    /**
     * Some notes about the service trip
     */
    private String notes;

    /**
     * Patient's phone
     */
    private String telephone;

    /**
     * Initial of the service trip coordinator.
     */
    private String coordinatorInitials;

    /**
     * City where trip was performed
     */
    private String city;

    /**
     * State where trip was performed
     */
    private String state;

    /**
     * Zipcode where trip was performed
     */
    private String zipCode;

    /**
     * Areacode where trip was performed
     */
    private String areaCode;

    /**
     * Phone number. Needs to be verified what is it.
     */
    private String phone;

    /**
     * Price of the trip described bu the service record.
     */
    private BigDecimal tripPrice;

    public String getRefId() {
        return this.refId;
    }

    @ExternalField(value = "REF_ID", required = true)
    public void setRefId(String refId) {
        this.refId = refId;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @ExternalField(value = "CANCELLED", fieldType = FieldType.BOOLEAN)
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLName() {
        return this.lName;
    }

    @ExternalField(value = "LNAME", required = true)
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
        if (fName == null && lName == null) {
            return null;
        }
        if (fName == null) {
            return lName;
        } else if (lName == null) {
            return fName;
        } else {
            return String.format("%s %s", fName, lName);
        }
    }

    public String getMemberId() {
        return this.memberId;
    }

    @ExternalField(value = "MEMBERID", required = true)
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDayOfBirth() {
        return this.dayOfBirth;
    }

    @ExternalField(value = "DOB", required = true, fieldType = FieldType.DATE)
    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    @ExternalField(value = "PICKUP_DATE", required = true, fieldType = FieldType.DATE)
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
        return wheelChairYesNo;
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

    public BigDecimal getTripPrice() {
        return tripPrice;
    }

    @ExternalField(value = "TRIP_PRICE", fieldType = FieldType.DECIMAL_MONEY)
    public void setTripPrice(BigDecimal tripPrice) {
        this.tripPrice = tripPrice;
    }

    public boolean requiredFieldsAreEmpty() {
        return StringUtils.isBlank(refId) &&
               StringUtils.isBlank(lName) &&
               StringUtils.isBlank(fName) &&
               StringUtils.isBlank(memberId) &&
               dayOfBirth == null &&
               pickupDate == null &&
               pickupTime == null;
    }

    public boolean allFieldsAreEmpty() {
        // Boolean values are excluded because it is hard to understand, if empty cell value means
        // False or it means tha there is no value because whole row is empty
        return requiredFieldsAreEmpty() &&
                apptTime == null &&
                StringUtils.isBlank(origin) &&
                StringUtils.isBlank(destination) &&
                totalPassengers == null &&
                StringUtils.isBlank(notes) &&
                StringUtils.isBlank(telephone) &&
                StringUtils.isBlank(coordinatorInitials) &&
                StringUtils.isBlank(city) &&
                StringUtils.isBlank(state) &&
                StringUtils.isBlank(zipCode) &&
                StringUtils.isBlank(areaCode) &&
                StringUtils.isBlank(phone) &&
                tripPrice == null;
    }

    @Override
    public int compareTo(ServiceRecord o) {
        return new CompareToBuilder()
            .append(refId, o.refId)
            .append(lName, o.lName)
            .append(fName, o.fName)
            .append(memberId, o.memberId)
            .append(dayOfBirth, o.dayOfBirth)
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
               && Objects.equals(dayOfBirth, serviceRecord.dayOfBirth)
               && Objects.equals(pickupDate, serviceRecord.pickupDate) 
               && Objects.equals(pickupTime, serviceRecord.pickupTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId, lName, fName, memberId, dayOfBirth, pickupDate, pickupTime);
    }
}