package com.jsoft.medpdfmaker.parser;

import java.io.File;

public interface TableFileParser<T> {

    void parse(File srcFile, RowCallback<T> rowCallBack);

}