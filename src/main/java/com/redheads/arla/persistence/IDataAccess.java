package com.redheads.arla.persistence;

import java.util.List;

public interface IDataAccess<T> {

    /**
     * Creates a new T in data storage
     * @param toCreate The object to create
     */
    void create(T toCreate);

    /**
     * Gets a list of all objects from data storage
     * @return The list of object from data storage
     */
    List<T> readAll();

    /**
     * Gets one object with a given id from data storage
     * @param id The objects id
     * @return The found object (Null if nothing was found)
     */
    T read(int id);

    /**
     * Updates all objects in a given list in data storage
     * @param toUpdate The list of objects to update
     */
    void updateAll(List<T> toUpdate);

    /**
     * Updates a given object in data storage
     * @param toUpdate The object to update
     */
    void update(T toUpdate);

    /**
     * Deletes an object from data storage
     * @param toDelete The object to delete
     */
    void delete(T toDelete);

}
