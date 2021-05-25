package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.ExcelReadError;

import java.util.List;

public interface IExcelLoader {

    List<String[]> readData(String path) throws ExcelReadError;

}
