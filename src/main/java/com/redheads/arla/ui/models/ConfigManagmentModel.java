package com.redheads.arla.ui.models;

import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.scene.control.MultipleSelectionModel;

import java.util.Optional;

public class ConfigManagmentModel extends ListSelectionModel<DashboardConfig> {

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }

    public ConfigManagmentModel(MultipleSelectionModel<DashboardConfig> selectionModel) {
        super(selectionModel);
    }

    public void newConfig() {
        Optional<DashboardConfig> config = DialogFactory.createConfigDialog().showAndWait();
        if (config.isPresent()) {
            repoFacade.getConfigRepo().add(config.get());
        }
    }

    public void saveConfig() {
        try {
            repoFacade.saveChanges();
        } catch (DataAccessError dataAccessError) {
            dataAccessError.printStackTrace();
            DialogFactory.createErrorAlert(dataAccessError);
        }
    }

    public void deleteConfig() {
        repoFacade.getConfigRepo().remove(getSelectedItem());
    }



}
