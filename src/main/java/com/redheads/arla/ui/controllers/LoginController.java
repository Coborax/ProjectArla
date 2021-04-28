package com.redheads.arla.ui.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.App;
import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.User;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    private AuthService authService = new AuthService();

    public void login(ActionEvent actionEvent) {
        try {
            if (authService.authenticateUser(usernameField.getText(), passwordField.getText())) {
                App.setRoot("adminView");
            }
        } catch (DataAccessError | IOException dataAccessError) {
            dataAccessError.printStackTrace();
        }
    }
}
