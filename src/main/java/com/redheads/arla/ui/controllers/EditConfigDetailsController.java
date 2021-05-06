/**
 * @author kjell
 */

package com.redheads.arla.ui.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditConfigDetailsController implements Initializable {

    @FXML
    private JFXTextField configNameTextField;
    @FXML
    private Spinner refreshSpinner;
    @FXML
    private VBox mainNode;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configNameTextField.onActionProperty().addListener(new ChangeListener<EventHandler<ActionEvent>>() {
            @Override
            public void changed(ObservableValue<? extends EventHandler<ActionEvent>> observableValue, EventHandler<ActionEvent> actionEventEventHandler, EventHandler<ActionEvent> t1) {
                AdminController.configName = t1.toString();
            }
        });


        refreshSpinner.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                AdminController.refreshRate = Integer.parseInt(t1.toString());
            }
        });

    }

    public void okAction(ActionEvent actionEvent) {

        // Close window
        Stage stage = (Stage) mainNode.getScene().getWindow();
        stage.close();
    }
}
