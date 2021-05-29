package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.ExcelReadError;

import java.util.List;

public interface IExcelLoader {

    /**
     * Reads the data from a .xlsx file
     * @param path The path to the file
     * @return A list of String arrays representing the data
     * @throws ExcelReadError If there is an error reading from the file
     */
    List<String[]> readData(String path) throws ExcelReadError;

}
