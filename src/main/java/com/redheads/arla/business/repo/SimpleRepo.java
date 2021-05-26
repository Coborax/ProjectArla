package com.redheads.arla.business.repo;

import com.redheads.arla.entities.Entity;
import com.redheads.arla.persistence.IDataAccess;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleRepo<T extends Entity> extends ObservableRepo<T> {

    private List<T> entities = new ArrayList<>();
    private List<T> newEntities = new ArrayList<>();
    private List<T> deletedEntities = new ArrayList<>();

    private IDataAccess<T> dataAccess;
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Override
    public List<T> getAll() {
        return entities;
    }

    @Override
    public T get(int id) {
        for (T entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void add(Entity toAdd) {
        entities.add((T) toAdd);
        System.out.println(toAdd.getId());
        if (toAdd.getId() == -1) {
            newEntities.add((T) toAdd);
        }
        notifyRepoChange();
    }

    @Override
    public void remove(Entity toRemove) {
        entities.remove((T) toRemove);
        if (toRemove.getId() != -1) {
            deletedEntities.add((T) toRemove);
        }
        notifyRepoChange();
    }

    @Override
    public void saveAllChanges() throws DataAccessError {
        for (Entity entity : entities) {
            if (!newEntities.contains(entity) && !deletedEntities.contains(entity) && entity.getLastUpdated().isAfter(lastUpdated)) {
                dataAccess.update((T) entity);
            }
        }
        lastUpdated = LocalDateTime.now();

        for (Entity entity : newEntities) {
            dataAccess.create((T) entity);
        }
        for (Entity entity : deletedEntities) {
            dataAccess.delete((T) entity);
        }
        newEntities.clear();
        deletedEntities.clear();
        notifyRepoChange();
    }

    public boolean hasChanges() {
        if (!newEntities.isEmpty() || !deletedEntities.isEmpty()) {
            System.out.println("lol 1");
            return true;
        }

        for (Entity entity : entities) {
            if (entity.getLastUpdated().isAfter(lastUpdated)) {
                System.out.println("lol 2");
                return true;
            }
        }
        return false;
    }

    public IDataAccess<T> getDataAccess() {
        return dataAccess;
    }

    public void setDataAccess(IDataAccess<T> dataAccess) {
        this.dataAccess = dataAccess;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public List<T> getNewEntities() {
        return newEntities;
    }

    public void setNewEntities(List<T> newEntities) {
        this.newEntities = newEntities;
    }

    public List<T> getDeletedEntities() {
        return deletedEntities;
    }

    public void setDeletedEntities(List<T> deletedEntities) {
        this.deletedEntities = deletedEntities;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
