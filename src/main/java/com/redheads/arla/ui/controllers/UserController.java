package com.redheads.arla.ui.controllers;

import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.entities.MessageType;
import com.redheads.arla.ui.CellFactory;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.util.exceptions.persistence.CSVReadError;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import com.redheads.arla.util.exceptions.persistence.ExcelReadError;
import com.redheads.arla.util.exceptions.persistence.PDFReadError;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class UserController implements Initializable {

    private RepoFacade repoFacade;
    {
        try {
            repoFacade = RepoFacade.getInstance();
        } catch (DataAccessError dataAccessError) {
            DialogFactory.createErrorAlert(dataAccessError).showAndWait();
        }
    }

    private DashboardConfig config;
    private Timer timer = new Timer();

    private boolean isFullscreen = false;
    private DashboardCell fullscreenCell = null;

    @FXML
    private VBox fullscreenContainer;
    @FXML
    private Label messageField;
    @FXML
    private VBox messageContainer;
    @FXML
    private GridPane tileGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fullscreenContainer.setVisible(false);
        fullscreenContainer.setManaged(false);

        // Timer task that updates the UI.
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> {
                    refresh();
                });
            }
        };

        Platform.runLater(() -> {
            config = repoFacade.getConfigRepo().get(UserSession.getInstance().getCurrentUser().getConfigID());
            setupGrid();
            setupMessage();

            // Schedule the timer to update the ui at with the refresh rate set by the admin
            timer.scheduleAtFixedRate(task, 0, config.getRefreshRate() * 1000);
        });
    }

    /**
     * Wil setup the main grid
     */
    private void setupGrid() {
        tileGrid.getChildren().clear();
        
        for (DashboardCell cell : config.getCells()) {
            Node node = null;
            try {
                node = createCellNode(cell);
            } catch (Exception e) {
                DialogFactory.createErrorAlert(e).showAndWait();
            }
            tileGrid.add(node, cell.getColumn(), cell.getRow(), cell.getColSpan(), cell.getRowSpan());
        }

        int rowCount = tileGrid.getRowCount();
        int columnCount = tileGrid.getColumnCount();

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);

        tileGrid.getRowConstraints().clear();
        for (int i = 0; i < rowCount; i++) {
            tileGrid.getRowConstraints().add(rc);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / columnCount);


        tileGrid.getColumnConstraints().clear();
        for (int i = 0; i < columnCount; i++) {
            tileGrid.getColumnConstraints().add(cc);
        }
    }

    /**
     * Gets a node for a cell from a DashboardCell object (Created by the CellFactory)
     * @param cell The DashboardCell object
     * @return A node that has data from the DashboardCell object.
     * @throws ExcelReadError If there is an error reading excel files
     * @throws FileNotFoundException If an image file is not found
     * @throws PDFReadError If there is an error reading pdf files
     * @throws CSVReadError If there is an error reading csv files
     */
    private Node createCellNode(DashboardCell cell) throws ExcelReadError, FileNotFoundException, PDFReadError, CSVReadError {
        Node node;
        node = CellFactory.createCell(cell);
        Node finalNode = node;
        node.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                toggleFullscreenCell(finalNode, cell);
                e.consume();
            }
        });
        return node;
    }

    /**
     * Setups the messages
     */
    private void setupMessage() {
        DashboardMessage m = repoFacade.getMessageRepo().getCurrentMessage(UserSession.getInstance().getCurrentUser().getConfigID());
        if (m != null) {
            messageContainer.setVisible(true);
            messageContainer.setManaged(true);
            messageField.setText(m.getMsg());
            toggleMessageClasses(m.getType());
        } else {
            messageContainer.setVisible(false);
            messageContainer.setManaged(false);
        }
    }

    /**
     * Setup the UI to fullscreen mode
     * @param cell The DashboardCell object
     */
    private void setupFullscreen(DashboardCell cell) {
        setupFullscreen(null, cell);
    }

    /**
     * Setup the UI to fullscreen mode
     * @param node The UI node to fullscreen
     * @param cell The corrosponding DashboardCell object
     */
    private void setupFullscreen(Node node, DashboardCell cell) {
        Node nodeToUse = node;
        if (nodeToUse == null) {
            try {
                nodeToUse = createCellNode(cell);
            } catch (Exception e) {
            }
        }
        fullscreenContainer.getChildren().clear();
        fullscreenContainer.getChildren().add(nodeToUse);
        fullscreenContainer.setVgrow(nodeToUse, Priority.ALWAYS);
    }

    /**
     * Toggles css classes for messages, depending on the severity
     * @param type The severity level
     */
    private void toggleMessageClasses(MessageType type) {
        ObservableList<String> classes = messageContainer.getStyleClass();
        if (classes.contains("info")) {
            classes.remove("info");
        }
        if (classes.contains("minor")) {
            classes.remove("minor");
        }
        if (classes.contains("major")) {
            classes.remove("major");
        }
        if (classes.contains("critical")) {
            classes.remove("critical");
        }

        switch (type) {
            case INFO -> classes.add("info");
            case MINOR -> classes.add("minor");
            case MAJOR -> classes.add("major");
            case CRITICAL -> classes.add("critical");
        }
    }

    public void refresh(ActionEvent actionEvent) {
        refresh();
    }

    /**
     * Refresh the UI
     */
    public void refresh() {
        if (isFullscreen) {
            setupFullscreen(fullscreenCell);
        }
        setupGrid();
        setupMessage();
    }

    /**
     * Called when the user pressed the logout button. Will log the user out
     * @param actionEvent
     */
    public void logout(ActionEvent actionEvent) {
        WindowManager.popScene();
        // Only remove user session if the user is not admin. As admins will be returned to the configuration screen.
        if (!UserSession.getInstance().getCurrentUser().isAdmin()) {
            UserSession.getInstance().setCurrentUser(null);
        }
    }

    /**
     * Toggles fullscreen mode
     * @param node The node to fullscreen
     * @param cell The corrosponding DashboardCell object
     */
    private void toggleFullscreenCell(Node node, DashboardCell cell) {
        if (isFullscreen) {
            fullscreenContainer.setVisible(false);
            fullscreenContainer.setManaged(false);
            tileGrid.setVisible(true);
            tileGrid.setManaged(true);

            setupGrid();
        } else {
            fullscreenContainer.setVisible(true);
            fullscreenContainer.setManaged(true);
            tileGrid.setVisible(false);
            tileGrid.setManaged(false);

            setupFullscreen(node, cell);
        }
        isFullscreen = !isFullscreen;
        fullscreenCell = cell;
    }

}
