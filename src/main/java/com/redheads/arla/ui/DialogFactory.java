package com.redheads.arla.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class DialogFactory {

    public static Dialog createErrorAlert(Throwable e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("An error occurred");
        a.setContentText(e.getMessage());

        e.printStackTrace();
        return a;
    }

    public static Dialog createInfoAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(msg);
        a.setContentText(msg);
        return a;
    }

    public static Dialog<User> createUserDialog() {
        Dialog<User> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        JFXTextField username = new JFXTextField();
        username.setPromptText("Username");
        JFXPasswordField password = new JFXPasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new User(username.getText(), password.getText(), false, -1);
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<DashboardConfig> createConfigDialog() {
        Dialog<DashboardConfig> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        JFXTextField name = new JFXTextField();
        name.setPromptText("Name");
        Spinner<Integer> refreshRate = new Spinner<>();

        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1);

        refreshRate.setValueFactory(valueFactory);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Refresh rate:"), 0, 1);
        grid.add(refreshRate, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> name.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DashboardConfig(name.getText(), refreshRate.getValue());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<DashboardCell> createCellDialog() {
        Dialog<DashboardCell> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //TODO: Change to spinner perhaps
        JFXTextField column = new JFXTextField();
        column.setPromptText("Column");
        JFXTextField row = new JFXTextField();
        row.setPromptText("Row");
        JFXTextField columnSpan = new JFXTextField();
        columnSpan.setPromptText("Column Span");
        JFXTextField rowSpan = new JFXTextField();
        rowSpan.setPromptText("Row Span");

        //TODO: Add file select
        JFXTextField contentPath = new JFXTextField();
        contentPath.setPromptText("Content Path");

        grid.add(new Label("Column:"), 0, 0);
        grid.add(column, 1, 0);
        grid.add(new Label("Row:"), 0, 1);
        grid.add(row, 1, 1);
        grid.add(new Label("Column Span:"), 0, 2);
        grid.add(columnSpan, 1, 2);
        grid.add(new Label("Row Span:"), 0, 3);
        grid.add(rowSpan, 1, 3);
        grid.add(new Label("Content Path:"), 0, 4);
        grid.add(contentPath, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DashboardCell(
                        Integer.parseInt(column.getText()),
                        Integer.parseInt(row.getText()),
                        Integer.parseInt(columnSpan.getText()),
                        Integer.parseInt(rowSpan.getText()),
                        contentPath.getText());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Boolean> createEditCellDialog(DashboardCell cell) {
        Dialog<Boolean> dialog = new Dialog();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //TODO: Change to spinner perhaps
        JFXTextField column = new JFXTextField();
        column.setPromptText("Column");
        column.textProperty().set(Integer.toString(cell.getColumn()));
        JFXTextField row = new JFXTextField();
        row.setPromptText("Row");
        row.textProperty().set(Integer.toString(cell.getRow()));
        JFXTextField columnSpan = new JFXTextField();
        columnSpan.setPromptText("Column Span");
        columnSpan.textProperty().set(Integer.toString(cell.getColSpan()));
        JFXTextField rowSpan = new JFXTextField();
        rowSpan.setPromptText("Row Span");
        rowSpan.textProperty().set(Integer.toString(cell.getRowSpan()));

        //TODO: Add file select
        JFXTextField contentPath = new JFXTextField();
        contentPath.setPromptText("Content Path");
        contentPath.textProperty().set(cell.getContentPath());

        grid.add(new Label("Column:"), 0, 0);
        grid.add(column, 1, 0);
        grid.add(new Label("Row:"), 0, 1);
        grid.add(row, 1, 1);
        grid.add(new Label("Column Span:"), 0, 2);
        grid.add(columnSpan, 1, 2);
        grid.add(new Label("Row Span:"), 0, 3);
        grid.add(rowSpan, 1, 3);
        grid.add(new Label("Content Path:"), 0, 4);
        grid.add(contentPath, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                cell.setColumn(Integer.parseInt(column.getText()));
                cell.setRow(Integer.parseInt(row.getText()));
                cell.setColSpan(Integer.parseInt(columnSpan.getText()));
                cell.setRowSpan(Integer.parseInt(rowSpan.getText()));
                cell.setContentPath(contentPath.getText());
                return true;
            }
            return false;
        });

        return dialog;
    }

}
