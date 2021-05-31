package com.redheads.arla.ui.models;

import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;

import java.util.Optional;

public class UserManagementModel extends ListSelectionModel<User> {

    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private SingleSelectionModel<DashboardConfig> configSingleSelectionModel;

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            showError(dataAccessError);
        }
    }

    private AuthService authService = new AuthService();

    public UserManagementModel(MultipleSelectionModel<User> selectedUser, SingleSelectionModel<DashboardConfig> selectionModel) {
        super(selectedUser);
        configSingleSelectionModel = selectionModel;

        this.username.addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                saveUsername();
            }
        });
        this.configSingleSelectionModel.selectedItemProperty().addListener((observableValue, dashboardConfig, t1) -> {
            if (t1 != null) {
                saveConfig();
            }
        });

        this.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                username.set("");
            } else {
                username.set(newValue.getUsername());
                configSingleSelectionModel.select(repoFacade.getConfigRepo().get(newValue.getConfigID()));
            }
            password.set("");
        });
    }

    /**
     * Adds a new user to repo
     */
    public void newUser() {
        Optional<User> result = DialogFactory.createUserDialog().showAndWait();
        if (result.isPresent()) {
            repoFacade.getUserRepo().add(result.get());
        }
    }

    /**
     * Updates the username field of the selected user
     */
    public void saveUsername() {
        User u = getSelectedItem();
        u.setUsername(username.get());
        getSelectionModel().select(u);
    }

    /**
     * Updates the config field of the selected user
     */
    public void saveConfig() {
        User u = getSelectedItem();
        u.setConfigID(configSingleSelectionModel.getSelectedItem().getId());
        getSelectionModel().select(u);
    }

    /**
     * Updates the password field of the selected user (Will also hash the typed password)
     */
    public void savePassword() {
        User u = getSelectedItem();
        if (!password.get().isEmpty() && !password.get().isBlank()) {
            getSelectedItem().setPassword(authService.hashPassword(password.get()));
        }
        getSelectionModel().select(u);
    }


    /**
     * Removes user from repo
     */
    public void deleteUser() {
        Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Confirm delete", "Are you sure you want to delete this user?").showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
            repoFacade.getUserRepo().remove(getSelectedItem());
        }
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    private void showError(Exception e) {
        DialogFactory.createErrorAlert(e).showAndWait();
    }
}
