package com.jsoft.medpdfmaker.parser;

public interface RowCallback<T> {

    void onRow(T rowObj);
}