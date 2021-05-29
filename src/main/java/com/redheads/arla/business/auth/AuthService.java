package com.redheads.arla.business.auth;

import com.password4j.*;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.User;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

public class AuthService {

    /**
     * Hashes a given password, using the BCrypt algorithm
     * @param password The password to be hashed
     * @return The hash of the password
     */
    public String hashPassword(String password) {
        Hash hash = Password.hash(password).withBCrypt();
        return hash.getResult();
    }

    /**
     * Authenticates a user with a given username and password
     * @param username The username of the user we want to authenticate
     * @param password The password of the user we want to authenticate
     * @return True if the authentication succeeds, and False if it fails or there is no user with the corrosponding username
     * @throws DataAccessError If there is a problem connection to the database through the repositories
     */
    public boolean authenticateUser(String username, String password) throws DataAccessError {
        RepoFacade repoFacade = RepoFacade.getInstance();
        User user = repoFacade.getUserRepo().get(username);
        if (user != null) {
            return Password.check(password, user.getPassword()).withBCrypt();
        }
        return false;
    }

}
