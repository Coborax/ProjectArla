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
    private MessageRepo messageRepo = new MessageRepo();

    /**
     * Saves all changes in all repos
     */
    public void saveChanges() throws DataAccessError {
        configRepo.saveAllChanges();
        userRepo.saveAllChanges();
        messageRepo.saveAllChanges();
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public DashboardConfigRepo getConfigRepo() {
        return configRepo;
    }

    public MessageRepo getMessageRepo() {
        return messageRepo;
    }

    /**
     * Checks all repositories in the system for changes
     * @return
     */
    public Boolean hasChanges() {
        return configRepo.hasChanges() || userRepo.hasChanges() || messageRepo.hasChanges();
    }
}
