package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.events.IRepoListener;
import com.redheads.arla.business.repo.IRepo;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.business.repo.UserRepo;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.models.UserManagementModel;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable, IRepoListener {

    @FXML
    private JFXListView<User> userList;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField usernameField;

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }

    private UserManagementModel userManagementModel;
    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repoFacade.getUserRepo().subscribe(this);

        userObservableList.addAll(repoFacade.getUserRepo().getAll());
        Platform.runLater(() -> {
            userManagementModel = new UserManagementModel(userList.getSelectionModel());
            usernameField.textProperty().bindBidirectional(userManagementModel.usernameProperty());
            passwordField.textProperty().bindBidirectional(userManagementModel.passwordProperty());

            userList.setItems(userObservableList);
        });
    }

    public void newUser(ActionEvent event) {
        userManagementModel.newUser();
    }

    public void saveUser(ActionEvent event) {
        userManagementModel.saveUser();
    }

    public void deleteUser(ActionEvent event) {
        userManagementModel.deleteUser();
    }

    @Override
    public void userRepoChanged(IRepo repo) {
        if (repo instanceof UserRepo) {
            userObservableList.clear();
            userObservableList.addAll(repo.getAll());
        }
    }
}
