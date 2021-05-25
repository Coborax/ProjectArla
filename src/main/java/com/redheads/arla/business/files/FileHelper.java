package com.redheads.arla.business.files;

import com.redheads.arla.persistence.files.ICSVLoader;
import com.redheads.arla.persistence.files.IExcelLoader;
import com.redheads.arla.persistence.files.OpenCsvLoader;
import com.redheads.arla.persistence.files.POIExcelLoader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import com.redheads.arla.util.exceptions.persistence.ExcelReadError;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class FileHelper {

    private static ICSVLoader csvLoader = new OpenCsvLoader();
    private static IExcelLoader excelLoader = new POIExcelLoader();

    public static List<String[]> loadCSVData(String path) throws CSVReadError {
        return csvLoader.readData(path);
    }

    public static List<String[]> loadExcelData(String path) throws ExcelReadError {
        return excelLoader.readData(path);
    }

    public static Image loadImage(String path) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(path);
        return new Image(inputStream);
    }
}

