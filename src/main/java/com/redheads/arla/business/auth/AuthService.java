package com.redheads.arla.business.auth;

import com.password4j.*;

public class AuthService {

    public String hashPassword(String password) {
        Hash hash = Password.hash(password).withBCrypt();
        return hash.getResult();
    }

}
