package com.redheads.arla.ui;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import com.redheads.arla.business.files.FileHelper;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import com.redheads.arla.util.exceptions.persistence.PDFReadError;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    public static Node createCell(DashboardCell cell) throws CSVReadError, PDFReadError {
        Node node = null;
        switch (cell.getContentType()) {
            case WEB -> node = createWebCell(cell);
            case CSV_RAW -> { }
            case CSV_BAR_CHART -> node = createCSVBarCell(cell);
            case PDF -> node = createPDFCell(cell);
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

}
