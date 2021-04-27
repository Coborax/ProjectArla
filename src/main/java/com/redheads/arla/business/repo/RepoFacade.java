package com.redheads.arla.business.repo;

public class RepoFacade {

    private static RepoFacade instance;

    public static RepoFacade getInstance() {
        if (instance == null) {
            instance = new RepoFacade();
        }
        return instance;
    }

    private UserRepo userRepo = new UserRepo();

    public void saveChanges() {
        userRepo.saveAllChanges();
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }
}
