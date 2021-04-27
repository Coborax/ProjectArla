package com.redheads.arla.business.repo;

import java.util.List;

public interface IRepo<T> {

    /**
     * Retrieves a list of all entities of the repo
     * @return
     */
    List<T> getAll();

    /**
     * Retrieves the entity with a given id
     * @param id
     * @return
     */
    T get(int id);

    /**
     * Adds a specific entity to the repo
     * @param toAdd
     */
    void add(T toAdd);

    /**
     * Removes a specific entity to the repo
     * @param toRemove
     */
    void remove(T toRemove);

}
