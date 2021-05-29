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

    /**
     * Loads the data from a excel file using a POIExcelLoader
     * @param path The path of the excel file
     * @return A list of String array representing the different rows and columns of the file
     * @throws ExcelReadError If there is an error reading the data (Usual because the file is not found, or could not be read)
     */
    public static List<String[]> loadExcelData(String path) throws ExcelReadError {
        return excelLoader.readData(path);
    }

    /**
     * Loads an image from a file
     * @param path The path of the file we want to load from
     * @return An Image object that is loaded from a FileInputStream
     * @throws FileNotFoundException If the file could not be found
     */
    public static Image loadImage(String path) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(path);
        return new Image(inputStream);
    }
}

