package com.redheads.arla.ui.tasks;

import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.concurrent.Task;

public class SaveChangesTask extends Task<Boolean> {

    private RepoFacade repoFacade;

    public SaveChangesTask() throws DataAccessError {
        repoFacade = RepoFacade.getInstance();
    }

    /**
     * Will save all changes of all repositories
     * @return True or False depending if the repository has any changes to save
     * @throws Exception If there is an error saving all changes
     */
    @Override
    protected Boolean call() throws Exception {
        Boolean hasChanges = repoFacade.hasChanges();
        repoFacade.saveChanges();
        return hasChanges;
    }


}
