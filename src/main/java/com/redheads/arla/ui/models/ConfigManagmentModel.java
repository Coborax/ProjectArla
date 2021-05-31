package com.redheads.arla.ui.models;

import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.scene.control.ButtonType;
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

    /**
     * Creates a new dashboard config and adds it to the config repo
     */
    public void newConfig() {
        Optional<DashboardConfig> config = DialogFactory.createConfigDialog().showAndWait();
        if (config.isPresent()) {
            repoFacade.getConfigRepo().add(config.get());
        }
    }


    /**
     * Deletes the selected config from the repo
     */
    public void deleteConfig() {
        Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Confirm delete", "Are you sure you want to delete this dashboard?").showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
            repoFacade.getConfigRepo().remove(getSelectedItem());
        }
    }


    /**
     * Will show a popup to make a new dashboard cell, and adds it to the repo
     */
    public void addContent() {
        Optional<DashboardCell> cell = DialogFactory.createCellDialog().showAndWait();
        if (cell.isPresent()) {
            getSelectedItem().addCell(cell.get());
        }
    }

    /**
     * Will show a popup to edit a dashboard cell
     */
    public void editContent() {
        DialogFactory.createEditCellDialog(selectionModelCell.getSelectedItem()).showAndWait();
    }

    /**
     * Will remove the selected dasboard cell from the selected dashboard
     */
    public void removeContent() {
        Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Confirm delete", "Are you sure you want to delete this content?").showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
            getSelectedItem().removeCell(selectionModelCell.getSelectedItem());
        }
    }

    /**
     * Will make the program go into preview mode
     */
    public void preview() {
        try {
            UserSession.getInstance().getCurrentUser().setConfigID(getSelectedItem().getId());
            WindowManager.pushScene("userView", 1280, 720);
        } catch (IOException e) {
            DialogFactory.createErrorAlert(e).showAndWait();
        }
    }

    /**
     * Will show a popup to make a new dashboard message, and adds it to the repo
     */
    public void addMessage() {
        Optional<DashboardMessage> message = DialogFactory.createMessageDialog(getSelectedItem().getId(), null).showAndWait();
        if (message.isPresent()) {
            repoFacade.getMessageRepo().add(message.get());
        }
    }

    /**
     * Will show a popup to edit a dashboard message
     */
    public void editMessage() {
        DialogFactory.createMessageDialog(getSelectedItem().getId(), selectionModelMessages.getSelectedItem()).showAndWait();
    }

    /**
     * Will remove the selected dasboard message from the message repo
     */
    public void removeMessage() {
        Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Confirm delete", "Are you sure you want to delete this message?").showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
            repoFacade.getMessageRepo().remove(selectionModelMessages.getSelectedItem());
        }
    }
}
