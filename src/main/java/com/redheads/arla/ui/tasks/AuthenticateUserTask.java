package com.redheads.arla.ui.tasks;

import com.redheads.arla.business.auth.AuthService;
import javafx.concurrent.Task;

public class AuthenticateUserTask extends Task<Boolean> {

    private AuthService authService = new AuthService();
    private String username;
    private String password;

    public AuthenticateUserTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Will authenticate the user of this task
     * @return The result of the authentication
     * @throws Exception If there is an error authenticating (Most likely a data access error)
     */
    @Override
    protected Boolean call() throws Exception {
        return authService.authenticateUser(username, password);
    }
}
