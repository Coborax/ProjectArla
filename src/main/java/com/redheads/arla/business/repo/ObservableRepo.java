package com.redheads.arla.business.repo;

import com.redheads.arla.business.events.IRepoListener;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableRepo<T> implements IRepo<T> {

    private List<IRepoListener> listeners = new ArrayList<>();

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
    public void notifyRepoChange() {
        for (IRepoListener l : listeners) {
            l.userRepoChanged(this);
        }
    }

}
