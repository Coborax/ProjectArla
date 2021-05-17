package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.CSVReadError;

import java.util.HashMap;
import java.util.List;

public interface ICsvLoader {

    List<String[]> readData(String path) throws CSVReadError;


}
