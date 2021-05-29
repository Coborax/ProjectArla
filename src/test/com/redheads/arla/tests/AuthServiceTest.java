package com.redheads.arla.tests;

import com.password4j.Password;
import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.business.repo.UserRepo;
import com.redheads.arla.entities.User;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthServiceTest {

    @Test
    void hashPassword () {
        //Test data
        AuthService authService = new AuthService();
        String password = "AVerySecurePassword";
        String hashedPassword = authService.hashPassword(password);

        //Check if password is hashed correct
        Assertions.assertTrue(Password.check(password, hashedPassword).withBCrypt());
    }

    @Test
    void authenticateUser() throws DataAccessError {
        //Test data
        AuthService authService = new AuthService();
        UserRepo userRepo = RepoFacade.getInstance().getUserRepo();
        String username = "Bob";
        String password = "AVerySecurePassword";
        User u = new User(username, authService.hashPassword(password), false, 1);

        //Add user to repo and check if we can authenticate the user with the password
        userRepo.add(u);
        Assertions.assertTrue(authService.authenticateUser(username, password));

        //Cleanup
        userRepo.remove(u);
    }

}
