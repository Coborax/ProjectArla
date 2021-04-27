module com.redheads {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;

    opens com.redheads.arla to javafx.fxml;
    exports com.redheads.arla;
}