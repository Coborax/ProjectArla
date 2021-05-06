package com.redheads.arla.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.concurrent.TimeUnit;

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

    public static Dialog<DashboardConfig> createConfigDialog(DashboardConfig config) {

        Dialog<DashboardConfig> dialog = new Dialog<>();

        System.out.println("Config name: " + config.getName());
        System.out.println("Refresh rate: " + config.getRefreshRate());

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        JFXTextField configName = new JFXTextField();
        configName.setText(config.getName());
        Spinner<Integer> spinner = new Spinner<Integer>();

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,999999, Math.toIntExact(TimeUnit.MILLISECONDS.toSeconds(config.getRefreshRate())));

        spinner.setValueFactory(valueFactory);

        spinner.getValueFactory().setValue(config.getRefreshRate());

        grid.add(new Label("Config name:"), 0, 0);
        grid.add(configName, 1, 0);
        grid.add(new Label("Content refresh rate (seconds):"), 0, 1);
        grid.add(spinner, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        configName.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        Platform.runLater(configName::requestFocus);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == loginButtonType) {
                config.setName(configName.getText());
                config.setRefreshRate(spinner.getValue());
                return config;
            }
            return null;
        });

        return dialog;
    }

}
