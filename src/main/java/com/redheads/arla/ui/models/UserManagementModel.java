package com.redheads.arla.ui.models;

import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MultipleSelectionModel;

import java.util.Optional;

public class UserManagementModel extends ListSelectionModel<User> {

    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            showError(dataAccessError);
        }
    }

    private AuthService authService = new AuthService();

    public UserManagementModel(MultipleSelectionModel<User> selectedUser) {
        super(selectedUser);
        this.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                username.set("");
            } else {
                username.set(newValue.getUsername());
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
     * Updates user object, and saves all changes
     */
    public void saveUser() {
        User u = getSelectedItem();

        u.setUsername(username.get());
        if (!password.get().isEmpty() && !password.get().isBlank()) {
            u.setPassword(authService.hashPassword(password.get()));
        }
        try {
            repoFacade.saveChanges();
        } catch (DataAccessError dataAccessError) {
            showError(dataAccessError);
        }
        getSelectionModel().select(u);
    }

    /**
     * Removes user from repo, and saves all changes
     */
    public void deleteUser() {
        repoFacade.getUserRepo().remove(getSelectedItem());
        try {
            repoFacade.saveChanges();
        } catch (DataAccessError dataAccessError) {
            showError(dataAccessError);
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
