package com.redheads.arla.ui.controllers;

import com.redheads.arla.business.auth.UserSession;
import com.redheads.arla.business.repo.RepoFacade;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.ui.CellFactory;
import com.redheads.arla.ui.DialogFactory;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

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

    @FXML
    private GridPane tileGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            config = repoFacade.getConfigRepo().get(UserSession.getInstance().getCurrentUser().getConfigID());
            setupGrid();
        });
    }

    private void setupGrid() {
        for (DashboardCell cell : config.getCells()) {
            Node node = CellFactory.createCell(cell);
            tileGrid.add(node, cell.getColumn(), cell.getRow(), cell.getColSpan(), cell.getRowSpan());
        }

        int rowCount = tileGrid.getRowCount();
        int columnCount = tileGrid.getColumnCount();

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);

        for (int i = 0; i < rowCount; i++) {
            tileGrid.getRowConstraints().add(rc);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / columnCount);

        for (int i = 0; i < columnCount; i++) {
            tileGrid.getColumnConstraints().add(cc);
        }
    }
}
