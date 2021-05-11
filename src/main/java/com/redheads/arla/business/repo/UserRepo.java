package com.redheads.arla.business.repo;

import com.redheads.arla.entities.User;
import com.redheads.arla.persistence.IDataAccess;
import com.redheads.arla.persistence.UserDataAccess;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.time.LocalDateTime;
import java.util.List;

public class UserRepo extends SimpleRepo<User> {

    private List<User> users;
    private List<User> newUsers;
    private List<User> deletedUsers;

    private IDataAccess<User> userDataAccess = new UserDataAccess();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public UserRepo() throws DataAccessError {
        users = getEntities();
        newUsers = getNewEntities();
        deletedUsers = getDeletedEntities();
        setDataAccess(userDataAccess);
        users.addAll(userDataAccess.readAll());
    }

    public User get(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
