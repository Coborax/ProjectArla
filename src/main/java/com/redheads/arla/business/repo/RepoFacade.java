package com.redheads.arla.business.repo;

import com.redheads.arla.util.exceptions.persistence.DataAccessError;

public class RepoFacade {

    private static RepoFacade instance;

    public RepoFacade() throws DataAccessError {
    }

    public static RepoFacade getInstance() throws DataAccessError {
        if (instance == null) {
            instance = new RepoFacade();
        }
        return instance;
    }

    private UserRepo userRepo = new UserRepo();
    private DashboardConfigRepo configRepo = new DashboardConfigRepo();

    /**
     * Saves all changes in all repos
     */
    public void saveChanges() throws DataAccessError {
        userRepo.saveAllChanges();
        configRepo.saveAllChanges();
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public DashboardConfigRepo getConfigRepo() {
        return configRepo;
    }
}
