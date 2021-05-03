package com.redheads.arla.ui;

import com.redheads.arla.entities.ContentType;
import com.redheads.arla.entities.DashboardCell;
import javafx.scene.Node;
import javafx.scene.web.WebView;

public class CellFactory {

    public static Node createCell(DashboardCell cell) {
        Node node = null;
        switch (cell.getContentType()) {
            case WEB -> node = createWebCell(cell);
            case CSV_RAW -> {
            }
            default -> node = null;
        }
        return node;
    }

    private static Node createWebCell(DashboardCell cell) {
        WebView webView = new WebView();
        webView.getEngine().load(cell.getContentPath());
        return webView;
    }

}
