package com.redheads.arla.business.repo;

import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.persistence.DashboardConfigDataAccess;
import com.redheads.arla.persistence.IDataAccess;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardConfigRepo extends ObservableRepo<DashboardConfig> {

    private List<DashboardConfig> dashboardConfigs = new ArrayList<>();
    private List<DashboardConfig> newConfigs = new ArrayList<>();
    private List<DashboardConfig> deletedConfigs = new ArrayList<>();

    private IDataAccess<DashboardConfig> configDataAccess = new DashboardConfigDataAccess();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public DashboardConfigRepo() throws DataAccessError {
        dashboardConfigs.addAll(configDataAccess.readAll());
    }

    @Override
    public List<DashboardConfig> getAll() {
        return dashboardConfigs;
    }

    @Override
    public DashboardConfig get(int id) {
        for (DashboardConfig config : dashboardConfigs) {
            if (config.getId() == id) {
                return config;
            }
        }
        return null;
    }

    @Override
    public void add(DashboardConfig toAdd) {
        dashboardConfigs.add(toAdd);
        if (toAdd.getId() == -1) {
            newConfigs.add(toAdd);
        }
        notifyRepoChange();
    }

    @Override
    public void remove(DashboardConfig toRemove) {
        dashboardConfigs.remove(toRemove);
        if (toRemove.getId() != -1) {
            deletedConfigs.add(toRemove);
        }
        notifyRepoChange();
    }

    @Override
    public void saveAllChanges() throws DataAccessError {
        try {
            for (DashboardConfig config : dashboardConfigs) {
                if (config.getLastUpdated().isAfter(lastUpdated)) {
                    configDataAccess.update(config);
                }
            }
            lastUpdated = LocalDateTime.now();

            for (DashboardConfig config : newConfigs) {
                configDataAccess.create(config);
            }
            for (DashboardConfig config : deletedConfigs) {
                configDataAccess.delete(config);
            }
            newConfigs.clear();
            deletedConfigs.clear();
        } catch (DataAccessError e) {
            throw e;
        }
        notifyRepoChange();
    }
}
