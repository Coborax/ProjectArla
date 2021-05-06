package com.redheads.arla.business.files;

import com.redheads.arla.persistence.files.ICsvLoader;
import com.redheads.arla.persistence.files.OpenCsvLoader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;

import java.util.List;

public class FileHelper {

    private static ICsvLoader csvLoader = new OpenCsvLoader();

    public static List<String[]> loadCSVData(String path) throws CSVReadError {
        return csvLoader.readData(path);
    }

}
