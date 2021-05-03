package com.redheads.arla.entities;

public class User extends Entity {

    private String username;
    private String password;
    private boolean isAdmin;
    private int configID;

    public User(String username, String password, boolean isAdmin, int configID) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.configID = configID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        entityChanged();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        entityChanged();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        entityChanged();
    }

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
        entityChanged();
    }

    @Override
    public String toString() {
        return getId() + " - " + username;
    }
}
