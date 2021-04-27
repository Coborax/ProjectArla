package com.redheads.arla.ui.models;

import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserManagementModel {

    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private ReadOnlyObjectProperty<User> selectedUser;

    private RepoFacade repoFacade = RepoFacade.getInstance();
    private AuthService authService = new AuthService();

    public UserManagementModel(ReadOnlyObjectProperty<User> selectedUser) {
        this.selectedUser = selectedUser;
        selectedUser.addListener((observable, oldValue, newValue) -> {
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
        User newUser = new User("New User", "password", false);
        repoFacade.getUserRepo().add(newUser);
    }

    /**
     * Updates user object, and saves all changes
     */
    public void saveUser() {
        selectedUser.get().setUsername(username.get());
        if (!password.get().isEmpty() && !password.get().isBlank()) {
            selectedUser.get().setPassword(authService.hashPassword(password.get()));
        }
        repoFacade.saveChanges();
    }

    /**
     * Removes user from repo, and saves all changes
     */
    public void deleteUser() {
        repoFacade.getUserRepo().remove(selectedUser.get());
        repoFacade.saveChanges();
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
}
