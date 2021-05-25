package com.redheads.arla.persistence.files;

import com.opencsv.CSVReader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OpenCsvLoader implements ICSVLoader {

    @Override
    public List<String[]> readData(String path) throws CSVReadError {
        List<String[]> result = new ArrayList<>();

        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVReadError("Could not read file: " + path);
        }
        CSVReader csvReader = new CSVReader(reader);


        try {
            result.addAll(csvReader.readAll());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVReadError("Could not read csv data in file: " + path);
        }

        return result;
    }

}
