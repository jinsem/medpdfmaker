package com.jsoft.medpdfmaker.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.util.Objects;

public class MemberIdPriceKey implements ServiceRecordGroupKey {

    /**
     * Patient's member ID
     */
    private final String memberId;

    /**
     * Price of the trip described by the service record.
     */
    private final BigDecimal tripPrice;

    public MemberIdPriceKey(ServiceRecord serviceRecord) {
        this.memberId = serviceRecord.getMemberId();
        this.tripPrice = serviceRecord.getTripPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberIdPriceKey that = (MemberIdPriceKey) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(tripPrice, that.tripPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, tripPrice);
    }

    @Override
    public int compareTo(MemberIdPriceKey o) {
        return new CompareToBuilder()
                .append(memberId, o.memberId)
                .append(tripPrice, o.tripPrice)
                .toComparison();
    }
}
