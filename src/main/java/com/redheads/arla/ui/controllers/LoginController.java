package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    private AuthService authService = new AuthService();

    public void login(ActionEvent actionEvent) {
        try {
            if (authService.authenticateUser(usernameField.getText(), passwordField.getText())) {
                boolean isAdmin = RepoFacade.getInstance().getUserRepo().get(usernameField.getText()).isAdmin();
                if (isAdmin) {
                    WindowManager.pushScene("adminView", 1280, 720);
                } else {
                    WindowManager.pushScene("userView", 1280, 720);
            }
            }
        } catch (DataAccessError | IOException dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }
}
