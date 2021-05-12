package com.redheads.arla.business.files;

import com.opencsv.CSVReader;
import com.redheads.arla.persistence.files.ICsvLoader;
import com.redheads.arla.persistence.files.OpenCsvLoader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FileHelper {

    private static ICsvLoader csvLoader = new OpenCsvLoader();

    public static List<String[]> loadCSVData(String path) throws CSVReadError {
        return csvLoader.readData(path);
    }
}

