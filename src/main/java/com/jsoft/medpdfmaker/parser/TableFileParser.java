package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.DomainEntity;

import java.io.File;
import java.io.IOException;

public interface TableFileParser<T extends DomainEntity> {

    ParsingResult parse(File srcFile, int sheetIdx, RowCallback<T> rowCallBack) throws IOException;

}