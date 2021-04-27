package com.redheads.arla.business.repo;

public class RepoFacade {

    private RepoFacade instance;

    public RepoFacade getInstance() {
        if (instance == null) {
            instance = new RepoFacade();
        }
        return instance;
    }

    private UserRepo userRepo = new UserRepo();

    public void saveChanges() {
        //TODO: Add Persistance saving
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }
}
