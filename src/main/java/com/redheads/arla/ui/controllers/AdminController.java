package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.redheads.arla.business.events.IUserRepoListener;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.business.repo.UserRepo;
import com.redheads.arla.entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable, IUserRepoListener {

    @FXML
    private JFXListView<User> userList;

    private RepoFacade repoFacade = RepoFacade.getInstance();

    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repoFacade.getUserRepo().subscribe(this);

        userObservableList.addAll(repoFacade.getUserRepo().getAll());
        Platform.runLater(() -> {
            userList.setItems(userObservableList);
        });
    }

    public void newUser(ActionEvent event) {
        User newUser = new User("New User", "123", false);
        repoFacade.getUserRepo().add(newUser);
        userList.getSelectionModel().select(newUser);
    }

    public void saveUser(ActionEvent event) {
        repoFacade.saveChanges();
    }

    public void deleteUser(ActionEvent event) {
        repoFacade.getUserRepo().remove(userList.getSelectionModel().getSelectedItem());
        repoFacade.saveChanges();
    }

    @Override
    public void userRepoChanged(UserRepo repo) {
        userObservableList.clear();
        userObservableList.addAll(repoFacade.getUserRepo().getAll());
    }
}
