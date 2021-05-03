package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.ui.tasks.AuthenticateUserTask;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController implements Initializable {

    @FXML
    private JFXSpinner loadingSpinner;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;

    private AuthService authService = new AuthService();

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            loadingSpinner.setManaged(false);
            loadingSpinner.setVisible(false);
        });
    }

    public void login(ActionEvent actionEvent) {
        AuthenticateUserTask task = new AuthenticateUserTask(usernameField.getText(), passwordField.getText());

        task.setOnRunning(workerStateEvent -> {
            loadingSpinner.setManaged(true);
            loadingSpinner.setVisible(true);
        });

        // Notify user of error
        task.setOnFailed(workerStateEvent -> {
            loadingSpinner.setManaged(false);
            loadingSpinner.setVisible(false);
            DialogFactory.createErrorAlert(workerStateEvent.getSource().getException()).showAndWait();
        });

        task.setOnSucceeded(workerStateEvent -> {
            try {
                boolean authenticated = task.getValue();
                if (authenticated) {
                    User user = RepoFacade.getInstance().getUserRepo().get(usernameField.getText());
                    boolean isAdmin = user.isAdmin();
                    if (isAdmin) {
                        WindowManager.pushScene("adminView", 1280, 720);
                    } else {
                        WindowManager.pushScene("userView", 1280, 720);
                    }
                    UserSession.getInstance().setCurrentUser(user);
                } else {
                    DialogFactory.createInfoAlert("Username or password was incorrect");
                }
            } catch (IOException | DataAccessError e) {
                DialogFactory.createErrorAlert(e).showAndWait();
                e.printStackTrace();
            }
        });

        executorService.execute(task);
    }
}
