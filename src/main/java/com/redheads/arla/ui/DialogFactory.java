package com.redheads.arla.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.entities.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class DialogFactory {

    public static Dialog createErrorAlert(Exception e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("An error occurred");
        a.setContentText(e.getMessage());
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
                return new User(username.getText(), password.getText(), false);
            }
            return null;
        });

        return dialog;
    }

}
