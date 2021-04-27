package com.redheads.arla.business.repo;

import com.redheads.arla.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepo implements IRepo<User> {

    private List<User> users = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User get(int id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void add(User toAdd) {
        users.add(toAdd);
    }

    @Override
    public void remove(User toRemove) {
        users.remove(toRemove);
    }
}
