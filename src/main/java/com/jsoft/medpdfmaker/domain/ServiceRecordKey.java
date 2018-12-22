package com.jsoft.medpdfmaker.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.util.Objects;

public class ServiceRecordKey implements Comparable<ServiceRecordKey> {

    /**
     * Patient's member ID
     */
    private final String memberId;

    /**
     * Price of the trip described bu the service record.
     */
    private final BigDecimal tripPrice;

    public ServiceRecordKey(String memberId, BigDecimal tripPrice) {
        this.memberId = memberId;
        this.tripPrice = tripPrice;
    }

    public String getMemberId() {
        return memberId;
    }

    public BigDecimal getTripPrice() {
        return tripPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRecordKey that = (ServiceRecordKey) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(tripPrice, that.tripPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, tripPrice);
    }

    @Override
    public int compareTo(ServiceRecordKey o) {
        return new CompareToBuilder()
                .append(memberId, o.memberId)
                .append(tripPrice, o.tripPrice)
                .toComparison();
    }
}
