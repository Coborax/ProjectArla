package com.redheads.arla.business.events;

import com.redheads.arla.business.repo.IRepo;

public interface IRepoListener {

    /**
     * Called when the repo subscribed to has changes
     * @param repo The repo that was changed
     */
    void userRepoChanged(IRepo repo);

}
