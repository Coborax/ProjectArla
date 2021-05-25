package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.CSVReadError;

import java.util.List;

public interface ICSVLoader {

    List<String[]> readData(String path) throws CSVReadError;

}
