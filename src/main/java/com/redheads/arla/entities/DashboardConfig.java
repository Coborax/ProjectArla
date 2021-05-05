package com.redheads.arla.entities;

import java.util.ArrayList;
import java.util.List;

public class DashboardConfig extends Entity {

    private String name;
    private int refreshRate;

    private List<DashboardCell> cells = new ArrayList<>();
    private List<DashboardCell> newCells = new ArrayList<>();
    private List<DashboardCell> deletedCells = new ArrayList<>();

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

    public int getRefreshRate() {
        return refreshRate;
    }

    public List<DashboardCell> getCells() {
        return cells;
    }

    public void addCell(DashboardCell cell) {
        cells.add(cell);
        newCells.add(cell);
        entityChanged();
    }

    public void removeCell(DashboardCell cell) {
        deletedCells.add(cell);
        cells.remove(cell);
        entityChanged();
    }

    public List<DashboardCell> getNewCells() {
        return newCells;
    }

    public List<DashboardCell> getDeletedCells() {
        return deletedCells;
    }

    @Override
    public String toString() {
        return name;
    }
}
