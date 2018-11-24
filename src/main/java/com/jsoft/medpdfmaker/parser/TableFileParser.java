package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.DomainEntity;

import java.io.File;

public interface TableFileParser<T extends DomainEntity> {

    void parse(File srcFile, int sheetIdx, RowCallback<T> rowCallBack);

}