package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.DomainEntity;

public interface RowCallback<T extends DomainEntity> {

    void onRow(T rowObj);
}