package com.redheads.arla.ui.controllers;

import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.ui.CellFactory;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.ui.WindowManager;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.web.WebView;

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
    Timer timer = new Timer();

    @FXML
    private GridPane tileGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Timer task that updates the UI.
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> setupGrid());
            }
        };

        Platform.runLater(() -> {
            config = repoFacade.getConfigRepo().get(UserSession.getInstance().getCurrentUser().getConfigID());
            setupGrid();
            System.out.println(config.getRefreshRate());

            // Schedule the timer to update the ui at with the refresh rate set by the admin
            timer.scheduleAtFixedRate(task, 0, config.getRefreshRate());
        });
    }

    private void setupGrid() {
        tileGrid.getChildren().clear();

        for (DashboardCell cell : config.getCells()) {
            Node node = CellFactory.createCell(cell);
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

    public void refresh(ActionEvent actionEvent) {
        setupGrid();
    }

    public void logout(ActionEvent actionEvent) {
        WindowManager.popScene();
        UserSession.getInstance().setCurrentUser(null);
    }
}
