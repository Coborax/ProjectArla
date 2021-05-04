package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.events.IRepoListener;
import com.redheads.arla.business.repo.DashboardConfigRepo;
import com.redheads.arla.business.repo.IRepo;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.business.repo.UserRepo;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.models.ConfigManagmentModel;
import com.redheads.arla.ui.models.UserManagementModel;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable, IRepoListener {

    @FXML
    private JFXTextField configNameField;
    @FXML
    private GridPane tileGrid;
    @FXML
    private JFXListView<User> userList;
    @FXML
    private JFXListView<DashboardConfig> configList;
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
    private ConfigManagmentModel configManagmentModel;

    private ObservableList<User> userObservableList = FXCollections.observableArrayList();
    private ObservableList<DashboardConfig> configObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repoFacade.getUserRepo().subscribe(this);
        repoFacade.getConfigRepo().subscribe(this);

        userObservableList.addAll(repoFacade.getUserRepo().getAll());
        configObservableList.addAll(repoFacade.getConfigRepo().getAll());
        Platform.runLater(() -> {
            userManagementModel = new UserManagementModel(userList.getSelectionModel());
            configManagmentModel = new ConfigManagmentModel(configList.getSelectionModel());
            usernameField.textProperty().bindBidirectional(userManagementModel.usernameProperty());
            passwordField.textProperty().bindBidirectional(userManagementModel.passwordProperty());

            userList.setItems(userObservableList);
            configList.setItems(configObservableList);
        });

        configList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DashboardConfig>() {
            @Override
            public void changed(ObservableValue<? extends DashboardConfig> observableValue, DashboardConfig dashboardConfig, DashboardConfig t1) {
                if (t1 != null) {
                    configNameField.setText(t1.getName());
                }
            }
        });

        configNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                configList.getSelectionModel().getSelectedItem().setName(t1);
            }
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
        } else if (repo instanceof DashboardConfigRepo) {
            configObservableList.clear();
            configObservableList.addAll(repo.getAll());
        }
    }

    public void newConfig(ActionEvent event) {
        configManagmentModel.newConfig();
    }

    public void saveConfig(ActionEvent event) {
        configManagmentModel.saveConfig();
    }

    public void deleteConfig(ActionEvent event) {
        configManagmentModel.deleteConfig();
    }
}
