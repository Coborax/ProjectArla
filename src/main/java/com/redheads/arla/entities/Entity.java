package com.redheads.arla.entities;

import java.time.LocalDateTime;

public class Entity {

    private int id = -1;
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Updates the lastUpdated variable (To be called whenever a change is made to the entity)
     */
    public void entityChanged() {
        lastUpdated = LocalDateTime.now();
    }
}
