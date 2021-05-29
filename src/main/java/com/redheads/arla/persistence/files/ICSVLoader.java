package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.CSVReadError;

import java.util.List;

public interface ICSVLoader {

    /**
     * Reads the data from a csv file
     * @param path The path to the file
     * @return A list of String arrays representing the data
     * @throws CSVReadError If there is an error reading from the file
     */
    List<String[]> readData(String path) throws CSVReadError;

}
