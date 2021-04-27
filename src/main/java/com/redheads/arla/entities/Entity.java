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

    public void entityChanged() {
        lastUpdated = LocalDateTime.now();
    }
}
