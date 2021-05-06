package com.redheads.arla.entities;

import java.util.ArrayList;
import java.util.List;

public class DashboardConfig extends Entity {

    private String name;
    private int refreshRate;

    private List<DashboardCell> cells = new ArrayList<>();

    public DashboardConfig(String name, int refreshRate) {
        this.name = name;
        this.refreshRate = refreshRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public List<DashboardCell> getCells() {
        return cells;
    }

    @Override
    public String toString() {
        return name;
    }
}
