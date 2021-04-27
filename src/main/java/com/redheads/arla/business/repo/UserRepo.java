package com.redheads.arla.business.repo;

import com.redheads.arla.business.events.IRepoListener;
import com.redheads.arla.entities.User;
import com.redheads.arla.persistence.IDataAccess;
import com.redheads.arla.persistence.UserDataAccess;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepo implements IRepo<User> {

    private List<User> users = new ArrayList<>();
    private List<User> newUsers = new ArrayList<>();
    private List<User> deletedUsers = new ArrayList<>();

    private IDataAccess<User> userDataAccess = new UserDataAccess();

    private List<IRepoListener> listeners = new ArrayList<>();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public UserRepo() {
        users.addAll(userDataAccess.readAll());
    }

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
        if (toAdd.getId() == -1) {
            newUsers.add(toAdd);
        }
        notifyUserRepoChange();
    }

    @Override
    public void remove(User toRemove) {
        users.remove(toRemove);
        if (toRemove.getId() != -1) {
            deletedUsers.add(toRemove);
        }
        notifyUserRepoChange();
    }

    @Override
    public void saveAllChanges() {
        for (User u : users) {
            if (u.getLastUpdated().isAfter(lastUpdated)) {
                userDataAccess.update(u);
            }
        }
        lastUpdated = LocalDateTime.now();

        for (User u : newUsers) {
            userDataAccess.create(u);
        }
        for (User u : deletedUsers) {
            userDataAccess.delete(u);
        }

        newUsers.clear();
        deletedUsers.clear();
        notifyUserRepoChange();
    }

    /**
     * Subscribe to listen for Repo events
     * @param listener The listener to subscribe
     */
    public void subscribe(IRepoListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the repo has changed
     */
    private void notifyUserRepoChange() {
        for (IRepoListener l : listeners) {
            l.userRepoChanged(this);
        }
    }
}
