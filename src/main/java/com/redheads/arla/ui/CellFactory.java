package com.redheads.arla.ui;

import com.redheads.arla.business.files.FileHelper;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.web.WebView;

import java.util.*;

public class CellFactory {

    public static Node createCell(DashboardCell cell) throws CSVReadError {
        Node node = null;
        switch (cell.getContentType()) {
            case WEB -> node = createWebCell(cell);
            case CSV_RAW -> { }
            case CSV_BAR_CHART -> node = createCSVBarCell(cell);
            case CSV_PIE_CHART -> node = createCSVPiechartCell(cell);
            default -> node = null;
        }
        return node;
    }

    private static Node createWebCell(DashboardCell cell) {
        WebView webView = new WebView();
        webView.getEngine().load(cell.getContentPath());
        return webView;
    }

    private static Node createCSVBarCell(DashboardCell cell) throws CSVReadError {
        List<String[]> data = FileHelper.loadCSVData(cell.getContentPath());

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

    private static Node createCSVPiechartCell(DashboardCell cell) throws CSVReadError {
        List<String[]> data = FileHelper.loadCSVData(cell.getContentPath());

        PieChart piechart = new PieChart();
        List<List<String>> dataByColumn = new ArrayList<>();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Loops through list of data and adds values by column to dataByColumn
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
            Double value = null;

            // Adds the remaining values as values for the piechart data
            for (int i = 0; i < entry.size(); i++) {
                if (i > 0) {
                    if(value == null) {
                        value = Double.valueOf(entry.get(i));
                    } else {
                        value = Double.sum(value, Double.parseDouble(entry.get(i)));
                    }
                }
            }

            // Creates new piechart data and adds it to the observable list of data
            pieChartData.add(new PieChart.Data(header, value));
        }
        piechart.setData(pieChartData);

        return piechart;
    }

}
