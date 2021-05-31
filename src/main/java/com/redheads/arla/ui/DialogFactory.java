package com.redheads.arla.ui;

import com.jfoenix.controls.*;
import com.redheads.arla.business.auth.AuthService;
import com.redheads.arla.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DialogFactory {

    public static Dialog createConfirmationAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(msg);

        return alert;
    }

    public static Dialog createErrorAlert(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred");
        alert.setContentText(e.getMessage());

        e.printStackTrace();
        return alert;
    }

    public static Dialog createInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(msg);
        alert.setContentText(msg);
        return alert;
    }

    public static Dialog<User> createUserDialog() {
        Dialog<User> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = createGridPane();

        JFXTextField username = new JFXTextField();
        username.setPromptText("Username");
        JFXPasswordField password = new JFXPasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> username.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                AuthService service = new AuthService();
                return new User(username.getText(), service.hashPassword(password.getText()), false, -1);
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<DashboardConfig> createConfigDialog() {
        Dialog<DashboardConfig> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = createGridPane();

        JFXTextField name = new JFXTextField();
        name.setPromptText("Name");
        Spinner<Integer> refreshRate = new Spinner<>();

        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1);

        refreshRate.setValueFactory(valueFactory);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Refresh rate:"), 0, 1);
        grid.add(refreshRate, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> name.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DashboardConfig(name.getText(), refreshRate.getValue());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<DashboardConfig> createConfigDialog(DashboardConfig config) {

        Dialog<DashboardConfig> dialog = new Dialog<>();

        System.out.println("Config name: " + config.getName());
        System.out.println("Refresh rate: " + config.getRefreshRate());

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = createGridPane();

        JFXTextField configName = new JFXTextField();
        configName.setText(config.getName());
        Spinner<Integer> spinner = new Spinner<Integer>();

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,999999, Math.toIntExact(TimeUnit.MILLISECONDS.toSeconds(config.getRefreshRate())));

        spinner.setValueFactory(valueFactory);

        spinner.getValueFactory().setValue(config.getRefreshRate());

        grid.add(new Label("Config name:"), 0, 0);
        grid.add(configName, 1, 0);
        grid.add(new Label("Content refresh rate (seconds):"), 0, 1);
        grid.add(spinner, 1, 1);

        Platform.runLater(configName::requestFocus);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == loginButtonType) {
                config.setName(configName.getText());
                config.setRefreshRate(spinner.getValue());
                return config;
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<DashboardCell> createCellDialog() {
        Dialog<DashboardCell> dialog = new Dialog<>();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = createGridPane();

        JFXTextField column = new JFXTextField();
        column.setPromptText("Column");
        JFXTextField row = new JFXTextField();
        row.setPromptText("Row");
        JFXTextField columnSpan = new JFXTextField();
        columnSpan.setPromptText("Column Span");
        JFXTextField rowSpan = new JFXTextField();
        rowSpan.setPromptText("Row Span");
        JFXComboBox<ContentType> contentType = new JFXComboBox<>();
        contentType.setItems(FXCollections.observableArrayList(ContentType.values()));

        JFXTextField contentPath = new JFXTextField();
        contentPath.setPromptText("Content Path");
        JFXButton choosePath = new JFXButton("Choose...");

        addElementsToGrid(grid, column, row, columnSpan, rowSpan, contentType, contentPath, choosePath);

        dialog.getDialogPane().setContent(grid);

        createFileChooser(contentPath, choosePath);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DashboardCell(
                        Integer.parseInt(column.getText()),
                        Integer.parseInt(row.getText()),
                        Integer.parseInt(columnSpan.getText()),
                        Integer.parseInt(rowSpan.getText()),
                        contentPath.getText(),
                        contentType.getSelectionModel().getSelectedItem());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Boolean> createEditCellDialog(DashboardCell cell) {
        Dialog<Boolean> dialog = new Dialog();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = createGridPane();

        JFXTextField column = new JFXTextField();
        column.setPromptText("Column");
        column.textProperty().set(Integer.toString(cell.getColumn()));
        JFXTextField row = new JFXTextField();
        row.setPromptText("Row");
        row.textProperty().set(Integer.toString(cell.getRow()));
        JFXTextField columnSpan = new JFXTextField();
        columnSpan.setPromptText("Column Span");
        columnSpan.textProperty().set(Integer.toString(cell.getColSpan()));
        JFXTextField rowSpan = new JFXTextField();
        rowSpan.setPromptText("Row Span");
        rowSpan.textProperty().set(Integer.toString(cell.getRowSpan()));
        JFXComboBox<ContentType> contentType = new JFXComboBox<>();
        contentType.setItems(FXCollections.observableArrayList(ContentType.values()));
        contentType.getSelectionModel().select(cell.getContentType());

        JFXTextField contentPath = new JFXTextField();
        contentPath.setPromptText("Content Path");
        contentPath.textProperty().set(cell.getContentPath());
        JFXButton choosePath = new JFXButton("Choose...");

        createFileChooser(contentPath, choosePath);
        addElementsToGrid(grid, column, row, columnSpan, rowSpan, contentType, contentPath, choosePath);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                cell.setColumn(Integer.parseInt(column.getText()));
                cell.setRow(Integer.parseInt(row.getText()));
                cell.setColSpan(Integer.parseInt(columnSpan.getText()));
                cell.setRowSpan(Integer.parseInt(rowSpan.getText()));
                cell.setContentPath(contentPath.getText());
                cell.setContentType(contentType.getSelectionModel().getSelectedItem());
                return true;
            }
            return false;
        });

        return dialog;
    }

    /**
     * Create dialog grid pane
     * @return the created grid pane
     */
    private static GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        return grid;
    }

    /**
     * Creates a file chooser dialog
     * @param contentPath text field where the file path should be placed in
     * @param choosePath Button which opens the file chooser dialog
     */
    private static void createFileChooser(JFXTextField contentPath, JFXButton choosePath) {
        FileChooser fileChooser = new FileChooser();

        // File extension filters with the possible file types
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter("All Files","*.csv", "*.xlsx", "*.jpg", "*.png", "*.jpeg", "*.html", "*.pdf");

        fileChooser.getExtensionFilters().add(fileExtensions);

        choosePath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File selectedFile = fileChooser.showOpenDialog(WindowManager.getMainWindow());
                if (selectedFile != null) {
                    contentPath.setText(selectedFile.getAbsolutePath());
                }
            }
        });
    }

    /**
     * Adds elements to the given grid
     * @param grid grid to be added to
     * @param column column location test field
     * @param row row location test field
     * @param columnSpan tile length test field
     * @param rowSpan tile width test field
     * @param contentType type of content to be added
     * @param contentPath path of content text field
     * @param choosePath choose file button
     */
    private static void addElementsToGrid(GridPane grid, JFXTextField column, JFXTextField row, JFXTextField columnSpan, JFXTextField rowSpan, JFXComboBox<ContentType> contentType, JFXTextField contentPath, JFXButton choosePath) {
        grid.add(new Label("Column:"), 0, 0);
        grid.add(column, 1, 0);
        grid.add(new Label("Row:"), 0, 1);
        grid.add(row, 1, 1);
        grid.add(new Label("Column Span:"), 0, 2);
        grid.add(columnSpan, 1, 2);
        grid.add(new Label("Row Span:"), 0, 3);
        grid.add(rowSpan, 1, 3);
        grid.add(new Label("Content Path:"), 0, 4);
        grid.add(contentPath, 1, 4);
        grid.add(choosePath, 2, 4);
        grid.add(new Label("Content Type:"), 0, 5);
        grid.add(contentType, 1, 5);
    }

    public static Dialog<DashboardMessage> createMessageDialog(int configID, DashboardMessage msg) {
        Dialog<DashboardMessage> dialog = new Dialog();

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        JFXTextField msgField = new JFXTextField();
        msgField.setPromptText("Message");

        JFXTimePicker startTime = new JFXTimePicker();
        JFXTimePicker endTime = new JFXTimePicker();

        JFXComboBox<MessageType> contentType = new JFXComboBox<>();
        contentType.setItems(FXCollections.observableArrayList(MessageType.values()));

        if (msg != null) {
            msgField.setText(msg.getMsg());
            startTime.setValue(msg.getStart());
            endTime.setValue(msg.getEnd());
            contentType.getSelectionModel().select(msg.getType());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Message:"), 0, 0);
        grid.add(msgField, 1, 0);
        grid.add(new Label("Start time:"), 0, 1);
        grid.add(startTime, 1, 1);
        grid.add(new Label("End time:"), 0, 2);
        grid.add(endTime, 1, 2);
        grid.add(new Label("Severity:"), 0, 3);
        grid.add(contentType, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if (msg != null) {
                    msg.setMsg(msgField.getText());
                    msg.setType(contentType.getValue());
                    msg.setStart(startTime.getValue());
                    msg.setEnd(endTime.getValue());
                } else {
                    return new DashboardMessage(configID, msgField.getText(), contentType.getValue(), startTime.getValue(), endTime.getValue());
                }
                return msg;
            }
            return null;
        });

        return dialog;
    }

}