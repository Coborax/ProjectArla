package com.redheads.arla.tests;

import com.opencsv.CSVWriter;
import com.redheads.arla.persistence.files.ICSVLoader;
import com.redheads.arla.persistence.files.OpenCsvLoader;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoaderTest {

    @Test
    void readData() throws IOException, CSVReadError {
        //Test data
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Year", "Marquis", "Saga"});
        data.add(new String[]{"2012", "50", "20"});
        data.add(new String[]{"2013", "52", "40"});
        data.add(new String[]{"2014", "62", "45"});
        data.add(new String[]{"2015", "10", "10"});

        CSVWriter writer = new CSVWriter(new FileWriter("./testFile.csv"));
        writer.writeAll(data);
        writer.close();

        ICSVLoader loader = new OpenCsvLoader();
        Assertions.assertArrayEquals(loader.readData("./testFile.csv").toArray(), data.toArray());

        //Cleanup
        File f = new File("./testFile.csv");
        f.delete();
    }

}
