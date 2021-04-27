package com.redheads.arla.business.auth;

import com.password4j.*;

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

}
