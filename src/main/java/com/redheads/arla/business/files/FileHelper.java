package com.redheads.arla.business.files;

import com.redheads.arla.persistence.files.ICsvLoader;
import com.redheads.arla.persistence.files.OpenCsvLoader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class FileHelper {

    private static ICsvLoader csvLoader = new OpenCsvLoader();

    public static List<String[]> loadCSVData(String path) throws CSVReadError {
        return csvLoader.readData(path);
    }

    public static Image loadImage(String path) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(path);
        return new Image(inputStream);
    }
}

