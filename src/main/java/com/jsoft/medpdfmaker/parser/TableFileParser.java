package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.DomainEntity;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public interface TableFileParser<T extends DomainEntity> {

    Result parse(File srcFile, int sheetIdx, Consumer<T> rowCallBack) throws IOException;

}