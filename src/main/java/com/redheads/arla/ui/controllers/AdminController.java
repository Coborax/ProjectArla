package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.redheads.arla.business.events.IRepoListener;
import com.redheads.arla.business.repo.DashboardConfigRepo;
import com.redheads.arla.business.repo.IRepo;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.business.repo.UserRepo;
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
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminController implements Initializable, IRepoListener {

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

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }

    private UserManagementModel userManagementModel;
    private ConfigManagmentModel configManagmentModel;

    private ObservableList<User> userObservableList = FXCollections.observableArrayList();
    private ObservableList<DashboardConfig> configObservableList = FXCollections.observableArrayList();
    private ObservableList<DashboardCell> selectedDashboardCells = FXCollections.observableArrayList();
    private ObservableList<DashboardMessage> selectedDashboardMessages = FXCollections.observableArrayList();

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

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

        //TODO: Move this into ConfigManagmentModel (Probably as a property, so we can bind it)
        configList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DashboardConfig>() {
            @Override
            public void changed(ObservableValue<? extends DashboardConfig> observableValue, DashboardConfig dashboardConfig, DashboardConfig t1) {
                if (t1 != null) {
                    selectedDashboardCells.clear();
                    selectedDashboardCells.addAll(t1.getCells());
                    selectedDashboardMessages.clear();
                    selectedDashboardMessages.addAll(repoFacade.getMessageRepo().getMessagesWithConfigID(t1.getId()));
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

    @Override
    public void userRepoChanged(IRepo repo) {
        // This might be called from another thread, so we do this to ensure we run it on the ui thread
        Platform.runLater(() -> {
            if (repo instanceof UserRepo) {
                userObservableList.clear();
                userObservableList.addAll(repo.getAll());
            } else if (repo instanceof DashboardConfigRepo) {
                configObservableList.clear();
                configObservableList.addAll(repo.getAll());
            }
        });
    }

    public void newConfig(ActionEvent event) {
        configManagmentModel.newConfig();
    }

    public void saveAllChanges(ActionEvent event) {
        saveAllChanges();
    }

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

    //TODO: Move to model
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

    public void logout(ActionEvent event) {
        if (repoFacade.hasChanges()) {
            Optional<ButtonType> res = DialogFactory.createConfirmationAlert("Unsaved changes!", "You have unsaved changes, do you want to save?").showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.OK)) {
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

    public void updatePass(ActionEvent event) {
        userManagementModel.savePassword();
        saveAllChanges();
    }
}
