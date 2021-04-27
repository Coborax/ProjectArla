module com.redheads {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.redheads.arla to javafx.fxml;
    exports com.redheads.arla;
}