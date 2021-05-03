package com.redheads.arla.business.auth;

import com.redheads.arla.entities.User;

public class UserSession {

    private static UserSession instance;

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
