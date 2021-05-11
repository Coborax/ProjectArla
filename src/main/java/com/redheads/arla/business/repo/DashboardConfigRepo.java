package com.redheads.arla.business.repo;

import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.persistence.DashboardConfigDataAccess;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardConfigRepo extends SimpleRepo<DashboardConfig> {

    private List<DashboardConfig> dashboardConfigs;
    private List<DashboardConfig> newConfigs;
    private List<DashboardConfig> deletedConfigs;

    private DashboardConfigDataAccess dataAccess = new DashboardConfigDataAccess();

    public DashboardConfigRepo() throws DataAccessError {
        dashboardConfigs = getEntities();
        newConfigs = getNewEntities();
        deletedConfigs = getDeletedEntities();
        setDataAccess(dataAccess);
        dashboardConfigs.addAll(dataAccess.readAll());
    }

    @Override
    public void saveAllChanges() throws DataAccessError {
        for (DashboardConfig config : newConfigs) {
            dataAccess.create(config);
        }
        for (DashboardConfig config : deletedConfigs) {
            dataAccess.delete(config);
        }

        for (DashboardConfig config : dashboardConfigs) {
            if (config.getLastUpdated().isAfter(getLastUpdated())) {
                for (DashboardCell cell : config.getNewCells()) {
                    dataAccess.createCell(config.getId(), cell);
                }
                for (DashboardCell cell : config.getDeletedCells()) {
                    dataAccess.deleteCell(config.getId(), cell);
                }
                dataAccess.update(config);
            }
            for (DashboardCell cell : config.getCells()) {
                if (!config.getNewCells().contains(cell)) {
                    dataAccess.updateCell(config.getId(), cell);
                }
            }
            config.getNewCells().clear();
        }

        setLastUpdated(LocalDateTime.now());
        newConfigs.clear();
        deletedConfigs.clear();
        notifyRepoChange();
    }
}
