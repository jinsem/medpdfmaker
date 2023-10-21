package com.jsoft.medpdfmaker.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

/**
 * Domain entity for medical service record
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ServiceRecord implements Comparable<ServiceRecord>, DomainEntity {

    public static final int MAX_MODIFIER_LEN = 4;

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
     * Patient's gender
     */
    private String gender;

    /**
     * Date when patient was picked up
     */
    private LocalDate pickupDate;

    /**
     * Time when patient was picked up
     */
    private LocalTime pickupTime;

    /**
     * Time when patient was picked up
     */
    private LocalTime dropOffTime;

    /**
     * Time of the patient's appointment
     */
    private LocalTime apptTime;

    /**
     * Patient's home address
     */
    private String homeAddress;

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

    /**
     * Code of the procedure for the record. 
     */
    private String procedureCode;

    /**
     * Days or units 
     */
    private String daysOrUnits;

    /**
     * Modifiers that should be displayed in the trips table.
     * is set, they override any modifiers calculated by the application.
     * Modifier should contain up to 4 not empty characters. If more characters
     * are provided, they will be ignoored.
     */
    private String modifiers;

    /**
     * Flag to mark the trip as outside of working hours trip.
     * This flag is calculated automatically if pick up time is outside of working
     * hours. But in some cases it is complicated to calculate if trip was done
     * on a holiday.
     * If value of this flag is True, this value is used as is.
     * If it is not provided in excel file, system calculates this flag
     * automatically.
     */
    private Boolean outsideWorkingHours;

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

    @ExternalField(value = "RIDE CANCELLATION", fieldType = FieldType.BOOLEAN)
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLName() {
        return this.lName;
    }

    @ExternalField(value = "LASTNAME", required = true)
    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getFName() {
        return this.fName;
    }

    @ExternalField(value = "FIRSTNAME", required = true)
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

    public String getFAndLNameReversed() {
        if (fName == null && lName == null) {
            return null;
        }
        if (fName == null) {
            return lName;
        } else if (lName == null) {
            return fName;
        } else {
            return String.format("%s, %s", lName, fName);
        }
    }

    public String getMemberId() {
        return this.memberId;
    }

    @ExternalField(value = "MEMBERNUMBER", required = true)
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDayOfBirth() {
        return this.dayOfBirth;
    }

    @ExternalField(value = "DATE OF BIRTH", required = true, fieldType = FieldType.DATE)
    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getGender() {
        return gender;
    }

    @ExternalField(value = "GENDER", required = false, fieldType = FieldType.STRING)
    public void setGender(String gender) {
        if (gender == null) {
            this.gender = gender;
        } else {
            this.gender = gender.toUpperCase(Locale.ROOT).trim();
        }
    }

    public LocalDate getPickupDate() {
        return this.pickupDate;
    }

    @ExternalField(value = "DATE OF SERVICE", required = true, fieldType = FieldType.DATE)
    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return this.pickupTime;
    }

    @ExternalField(value = "APPOINTMENT PICK-UP TIME", fieldType = FieldType.TIME)
    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalTime getDropOffTime() {
        return dropOffTime;
    }

    @ExternalField(value = "APPOINTMENT DROP-OFF TIME", fieldType = FieldType.TIME)
    public void setDropOffTime(LocalTime dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public LocalTime getApptTime() {
        return this.apptTime;
    }

    @ExternalField(value = "APPOINTMENT SCHEDULED TIME", fieldType = FieldType.TIME)
    public void setApptTime(LocalTime apptTime) {
        this.apptTime = apptTime;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getOrigin() {
        return this.origin;
    }

    @ExternalField("PICK UP LOCATION")
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

    @ExternalField(value = "WHEELCHAIR", fieldType = FieldType.BOOLEAN)
    public void setWheelChairYesNo(boolean wheelChairYesNo) {
        this.wheelChairYesNo = wheelChairYesNo;
    }

    public Integer getTotalPassengers() {
        return this.totalPassengers;
    }

    @ExternalField(value = "NUMBER OF PASSENGERS", fieldType = FieldType.INTEGER)
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

    @ExternalField("PRIMARY CONTACT NUMBER")
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCoordinatorInitials() {
        return this.coordinatorInitials;
    }

    @ExternalField("OPENEDBYNAME")
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

    public String getProcedureCode() {
        return this.procedureCode;
    }

    @ExternalField(value = "PROCEDURE_CODE", fieldType = FieldType.STRING)
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getDaysOrUnits() {
        return this.daysOrUnits;
    }

    @ExternalField(value = "DAYS_OR_UNITS", fieldType = FieldType.STRING)
    public void setDaysOrUnits(String daysOrUnits) {
        this.daysOrUnits = daysOrUnits;
    }

    public String getModifiers() {
        return modifiers;
    }

    @ExternalField(value = "MODIFIERS", fieldType = FieldType.STRING)
    public void setModifiers(String modifiers) {
        if (modifiers == null) {
            this.modifiers = null;
            return;
        }
        this.modifiers = modifiers.replaceAll(" ", "");
        if (this.modifiers.length() > MAX_MODIFIER_LEN) {
            this.modifiers = this.modifiers.substring(0, MAX_MODIFIER_LEN);
        }
    }

    public Boolean getOutsideWorkingHours() {
        return outsideWorkingHours;
    }

    @ExternalField(value = "OUTSIDE WORKING HOURS", fieldType = FieldType.BOOLEAN)
    public void setOutsideWorkingHours(Boolean outsideWorkingHours) {
        this.outsideWorkingHours = outsideWorkingHours;
    }

    public boolean requiredFieldsAreEmpty() {
        return StringUtils.isBlank(refId) &&
               StringUtils.isBlank(lName) &&
               StringUtils.isBlank(fName) &&
               StringUtils.isBlank(memberId) &&
               dayOfBirth == null &&
               pickupDate == null;
    }

    public boolean allFieldsAreEmpty() {
        // Boolean values are excluded because it is hard to understand, if empty cell value means
        // False or it means tha there is no value because whole row is empty
        return  StringUtils.isBlank(refId) &&
                StringUtils.isBlank(lName) &&
                StringUtils.isBlank(fName) &&
                StringUtils.isBlank(memberId) &&
                dayOfBirth == null &&
                pickupDate == null &&
                pickupTime == null &&
                dropOffTime == null &&
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
