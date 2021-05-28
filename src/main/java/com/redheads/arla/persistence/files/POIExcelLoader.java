package com.redheads.arla.persistence.files;

import com.redheads.arla.util.exceptions.persistence.ExcelReadError;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class POIExcelLoader implements IExcelLoader {
    @Override
    public List<String[]> readData(String path) throws ExcelReadError {
        List<String[]> result = new ArrayList<>();

        OPCPackage pkg = null;
        try {
            pkg = OPCPackage.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcelReadError("Cannot find or read Excel file: " + path, e);
        }

        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(pkg);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExcelReadError("Cannot find or read Excel file: " + path, e);
        }

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }

        for(int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if(row != null) {
                String[] rowArr = new String[cols];
                for(int c = 0; c < cols; c++) {
                    cell = row.getCell((short)c);
                    if(cell != null) {
                        switch (cell.getCellType()) {
                            case BLANK -> rowArr[c] = "";
                            case STRING -> rowArr[c] = cell.getStringCellValue();
                            case NUMERIC -> rowArr[c] = cell.getRawValue();
                        }
                    }
                }
                result.add(rowArr);
            }
        }

        try {
            pkg.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExcelReadError("Cannot find or read Excel file: " + path, e);
        }

        return result;
    }
}
