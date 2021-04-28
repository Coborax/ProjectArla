module com.redheads {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.jfoenix;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;
    requires password4j;
    requires eu.hansolo.tilesfx;

    opens com.redheads.arla to javafx.fxml, javafx.graphics;
    opens com.redheads.arla.ui.controllers to javafx.fxml, javafx.graphics;
    exports com.redheads.arla.ui.controllers;
    exports com.redheads.arla;
}