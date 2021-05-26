package com.redheads.arla.ui;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import com.redheads.arla.business.files.FileHelper;
import com.redheads.arla.entities.ContentType;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import com.redheads.arla.util.exceptions.persistence.ExcelReadError;
import com.redheads.arla.util.exceptions.persistence.PDFReadError;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    //TODO: Change file not found to custom exception
    public static Node createCell(DashboardCell cell) throws CSVReadError, PDFReadError, FileNotFoundException, ExcelReadError {
        Node node = null;
        switch (cell.getContentType()) {
            case WEB -> node = createWebCell(cell);
            case CSV_RAW, EXCEL_RAW -> node = createRawCell(cell);
            case CSV_BAR_CHART, EXCEL_BAR_CHART -> node = createBarCell(cell);
            case PDF -> node = createPDFCell(cell);
            case CSV_PIE_CHART, EXCEL_PIE_CHART -> node = createPieChartCell(cell);
            case CSV_LINE_CHART, EXCEL_LINE_CHART -> node = createLineChartCell(cell);
            case IMAGE -> node = createImageCell(cell);
            default -> node = null;
        }
        return node;
    }

    private static Node createPDFCell(DashboardCell cell) throws PDFReadError {
        PDFDisplayer pdfView = new PDFDisplayer();
        try {
            pdfView.loadPDF(new File(cell.getContentPath()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new PDFReadError("Could not find PDF", e);
        }
        return pdfView.toNode();
    }

    private static Node createWebCell(DashboardCell cell) {
        WebView webView = new WebView();
        webView.getEngine().load("file://" + cell.getContentPath());
        return webView;
    }

    private static Node createBarCell(DashboardCell cell) throws CSVReadError, ExcelReadError {
        List<String[]> data = getSheetData(cell);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        List<XYChart.Series<String, Number>> series = new ArrayList<>();

        for (int i = 1; i < data.get(0).length; i++) {
            XYChart.Series newSeries = new XYChart.Series();
            newSeries.setName(data.get(0)[i]);
            series.add(newSeries);
        }

        for (int x = 1; x < data.size(); x++) {
            for (int y = 1; y <= series.size(); y++) {
                series.get(y - 1).getData().add(new XYChart.Data(data.get(x)[0], Integer.parseInt(data.get(x)[y])));
            }
        }
        
        chart.getData().addAll(series);
      
        return chart;
    }

    private static List<String[]> getSheetData(DashboardCell cell) throws ExcelReadError, CSVReadError {
        List<String[]> data;
        if (cell.getContentType() == ContentType.EXCEL_RAW ||
                cell.getContentType() == ContentType.EXCEL_LINE_CHART ||
                cell.getContentType() == ContentType.EXCEL_BAR_CHART ||
                cell.getContentType() == ContentType.EXCEL_PIE_CHART) {
            data = FileHelper.loadExcelData(cell.getContentPath());
        } else {
            data = FileHelper.loadCSVData(cell.getContentPath());
        }
        return data;
    }

    private static Node createPieChartCell(DashboardCell cell) throws CSVReadError, ExcelReadError {
        List<String[]> data = getSheetData(cell);

        PieChart piechart = new PieChart();
        List<List<String>> dataByColumn = new ArrayList<>();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
      
        // Loops through list of data and adds values by column to dataByColumn nested list
        for (int x = 0; x < data.get(0).length; x++) {
            List<String> tempList = new ArrayList<>();
            for (String[] d: data) {
                tempList.add(d[x]);
            }
            dataByColumn.add(tempList);
        }

        // Adds data to pieChartData
        for (List<String> entry : dataByColumn) {

            // Sets the first value as header
            String header = entry.get(0);
            Double value = 0.0;

            // Adds the remaining values as values for the pieChart data
            for (int i = 1; i < entry.size(); i++) {
                value = Double.sum(value, Double.parseDouble(entry.get(i)));
            }

            // Creates new pieChart data and adds it to the observable list of data
            pieChartData.add(new PieChart.Data(header, value));
        }
        piechart.setData(pieChartData);

        return piechart;
    }

    private static Node createLineChartCell(DashboardCell cell) throws CSVReadError, ExcelReadError {
        List<String[]> data = getSheetData(cell);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        List<XYChart.Series<String, Number>> series = new ArrayList<>();

        for (int i = 1; i < data.get(0).length; i++) {
            XYChart.Series newSeries = new XYChart.Series();
            newSeries.setName(data.get(0)[i]);
            series.add(newSeries);
        }

        for (int x = 1; x < data.size(); x++) {
            for (int y = 1; y <= series.size(); y++) {
                series.get(y - 1).getData().add(new XYChart.Data(data.get(x)[0], Integer.parseInt(data.get(x)[y])));
            }
        }

        lineChart.getData().addAll(series);

        return lineChart;
    }

    private static Node createRawCell(DashboardCell cell) throws CSVReadError, ExcelReadError {
        List<String[]> data = getSheetData(cell);
        TableView<ObservableList<String>> tableView = new TableView<>();

        for (int i = 0; i < data.get(0).length; i++) {
            final int finalIdx = i;

            TableColumn<ObservableList<String>, String> column = new TableColumn<>(data.get(0)[i]);

            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
            tableView.getColumns().add(column);
        }

        for (String[] d: data) {
            ObservableList<String> values = FXCollections.observableArrayList();

            values.addAll(d);
            tableView.getItems().add(values);
        }

        tableView.getItems().remove(0);

        return tableView;
    }

    private static Node createImageCell(DashboardCell cell) throws FileNotFoundException {
        BorderPane pane = new BorderPane();
        pane.setCenter(new ImageView(FileHelper.loadImage(cell.getContentPath())));
        return pane;
    }

}
