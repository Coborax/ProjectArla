package com.redheads.arla.ui.models;

import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.scene.control.MultipleSelectionModel;

import java.io.IOException;
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

    MultipleSelectionModel<DashboardCell> selectionModelCell;
    MultipleSelectionModel<DashboardMessage> selectionModelMessages;

    public ConfigManagmentModel(MultipleSelectionModel<DashboardConfig> selectionModel,
                                MultipleSelectionModel<DashboardCell> selectionModelCell,
                                MultipleSelectionModel<DashboardMessage> selectionModelMessages) {
        super(selectionModel);
        this.selectionModelCell = selectionModelCell;
        this.selectionModelMessages = selectionModelMessages;
    }

    public void newConfig() {
        Optional<DashboardConfig> config = DialogFactory.createConfigDialog().showAndWait();
        if (config.isPresent()) {
            repoFacade.getConfigRepo().add(config.get());
        }
    }

    public void saveConfig() {
        DashboardConfig config = getSelectedItem();
        try {
            repoFacade.saveChanges();
        } catch (DataAccessError dataAccessError) {
            dataAccessError.printStackTrace();
            DialogFactory.createErrorAlert(dataAccessError);
        }
        getSelectionModel().select(config);
    }

    public void deleteConfig() {
        repoFacade.getConfigRepo().remove(getSelectedItem());
    }


    public void addContent() {
        Optional<DashboardCell> cell = DialogFactory.createCellDialog().showAndWait();
        if (cell.isPresent()) {
            getSelectedItem().addCell(cell.get());
        }
    }

    public void editContent() {
        DialogFactory.createEditCellDialog(selectionModelCell.getSelectedItem()).showAndWait();
    }

    public void removeContent() {
        getSelectedItem().removeCell(selectionModelCell.getSelectedItem());
    }

    public void preview() {
        try {
            UserSession.getInstance().getCurrentUser().setConfigID(getSelectedItem().getId());
            WindowManager.pushScene("userView", 1280, 720);
        } catch (IOException e) {
            DialogFactory.createErrorAlert(e).showAndWait();
        }
    }

    public void addMessage() {
    }

    public void editMessage() {
    }

    public void removeMessage() {
    }
}
