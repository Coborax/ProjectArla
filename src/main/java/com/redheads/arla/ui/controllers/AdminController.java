package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.events.IRepoListener;
import com.redheads.arla.business.repo.*;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.entities.User;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.ui.models.ConfigManagmentModel;
import com.redheads.arla.ui.models.UserManagementModel;
import com.redheads.arla.ui.tasks.SaveChangesTask;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminController implements Initializable, IRepoListener {

    // UI Elements
    @FXML
    private JFXListView dashboardMessages;
    @FXML
    private JFXComboBox<DashboardConfig> configSelection;
    @FXML
    private JFXListView dashboardCells;
    @FXML
    private GridPane tileGrid;
    @FXML
    private JFXListView<User> userList;
    @FXML
    private JFXListView<DashboardConfig> configList;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField usernameField;

    //Repo facade
    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }

    //View model abstractions
    private UserManagementModel userManagementModel;
    private ConfigManagmentModel configManagmentModel;

    //Different observable lists
    private ObservableList<User> userObservableList = FXCollections.observableArrayList();
    private ObservableList<DashboardConfig> configObservableList = FXCollections.observableArrayList();
    private ObservableList<DashboardCell> selectedDashboardCells = FXCollections.observableArrayList();
    private ObservableList<DashboardMessage> selectedDashboardMessages = FXCollections.observableArrayList();

    //Task executor service
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * Does the initialization of the different ui elements
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repoFacade.getUserRepo().subscribe(this);
        repoFacade.getConfigRepo().subscribe(this);
        repoFacade.getMessageRepo().subscribe(this);

        userObservableList.addAll(repoFacade.getUserRepo().getAll());
        configObservableList.addAll(repoFacade.getConfigRepo().getAll());

        Platform.runLater(() -> {
            userManagementModel = new UserManagementModel(userList.getSelectionModel(), configSelection.getSelectionModel());
            configManagmentModel = new ConfigManagmentModel(configList.getSelectionModel(), dashboardCells.getSelectionModel(), dashboardMessages.getSelectionModel());
            usernameField.textProperty().bindBidirectional(userManagementModel.usernameProperty());
            passwordField.textProperty().bindBidirectional(userManagementModel.passwordProperty());

            userList.setItems(userObservableList);
            configList.setItems(configObservableList);
            configSelection.setItems(configObservableList);
            dashboardCells.setItems(selectedDashboardCells);
            dashboardMessages.setItems(selectedDashboardMessages);
        });

        configList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DashboardConfig>() {
            @Override
            public void changed(ObservableValue<? extends DashboardConfig> observableValue, DashboardConfig dashboardConfig, DashboardConfig t1) {
                if (t1 != null) {
                    updateSelectionModel(dashboardCells.getSelectionModel(), selectedDashboardCells, t1.getCells());
                    updateSelectionModel(dashboardMessages.getSelectionModel(), selectedDashboardMessages, repoFacade.getMessageRepo().getMessagesWithConfigID(t1.getId()));
                }
            }
        });

        WindowManager.getMainWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> {
            if (repoFacade.hasChanges()) {
                Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Unsaved changes!", "You have unsaved changes, do you want to save?").showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                    saveAllChanges();
                }
            }
        });
    }

    public void newUser(ActionEvent event) {
        userManagementModel.newUser();
    }

    public void deleteUser(ActionEvent event) {
        userManagementModel.deleteUser();
    }

    /**
     * Called by the different repos that we subscibe to
     * @param repo The repo that was changed
     */
    @Override
    public void userRepoChanged(IRepo repo) {
        // This might be called from another thread, so we do this to ensure we run it on the ui thread
        Platform.runLater(() -> {
            if (repo instanceof UserRepo) {
                updateSelectionModel(userList.getSelectionModel(), userObservableList, repo.getAll());
            } else if (repo instanceof DashboardConfigRepo) {
                updateSelectionModel(configList.getSelectionModel(), configObservableList, repo.getAll());
            } else if (repo instanceof MessageRepo) {
                if (configList.getSelectionModel().getSelectedItem() != null) {
                    updateSelectionModel(dashboardMessages.getSelectionModel(), selectedDashboardMessages, repoFacade.getMessageRepo().getMessagesWithConfigID(configList.getSelectionModel().getSelectedItem().getId()));
                }
            }
        });
    }

    public void newConfig(ActionEvent event) {
        configManagmentModel.newConfig();
    }

    public void saveAllChanges(ActionEvent event) {
        saveAllChanges();
    }

    /**
     * Starts a task to save all changes (This will be run on another thread)
     */
    public void saveAllChanges() {
        try {
            SaveChangesTask task = new SaveChangesTask();

            task.setOnRunning(workerStateEvent -> {
                WindowManager.setCursorType(Cursor.WAIT);
            });

            task.setOnSucceeded(workerStateEvent -> {
                WindowManager.setCursorType(Cursor.DEFAULT);
                passwordField.clear();
            });

            task.setOnFailed(workerStateEvent -> {
                WindowManager.setCursorType(Cursor.DEFAULT);
                passwordField.clear();
                DialogFactory.createErrorAlert(workerStateEvent.getSource().getException()).showAndWait();
            });

            executorService.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
            DialogFactory.createErrorAlert(e);
        }
    }

    public void deleteConfig(ActionEvent event) {
        configManagmentModel.deleteConfig();
    }

    public void editConfigDetails(ActionEvent actionEvent) {
        Optional<DashboardConfig> result = DialogFactory.createConfigDialog(configList.getSelectionModel().getSelectedItem()).showAndWait();
    }

    public void addContent(ActionEvent event) {
        configManagmentModel.addContent();
    }

    public void editContent(ActionEvent event) {
        configManagmentModel.editContent();
    }

    public void removeContent(ActionEvent event) {
        configManagmentModel.removeContent();
    }

    public void preview(ActionEvent event) {
        configManagmentModel.preview();
    }

    /**
     * Will start the logout process when the user pressed the logout button
     * @param event
     */
    public void logout(ActionEvent event) {
        // Check if repo has changes
        if (repoFacade.hasChanges()) {
            // Ask if user wants to save
            Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Unsaved changes!", "You have unsaved changes, do you want to save?").showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                //Save
                saveAllChanges();
            }
        }

        WindowManager.popScene();
    }

    public void addMessage(ActionEvent event) {
        configManagmentModel.addMessage();
    }

    public void editMessage(ActionEvent event) {
        configManagmentModel.editMessage();
    }

    public void removeMessage(ActionEvent event) {
        configManagmentModel.removeMessage();
    }

    /**
     * Updates the password of the selected user (Will force changes to be saved, to be sure password is updated)
     * @param event
     */
    public void updatePass(ActionEvent event) {
        userManagementModel.savePassword();
        saveAllChanges();
    }

    /**
     * Updates a selection model with new data (Re-selects the previous selected item)
     * @param model The model to update
     * @param list The observable list binded to the model
     * @param newData the list of refreshed data
     */
    private void updateSelectionModel(SelectionModel model, ObservableList list, List newData) {
        Object selectedItem = model.getSelectedItem();
        list.clear();
        list.addAll(newData);
        model.select(selectedItem);
    }
}
