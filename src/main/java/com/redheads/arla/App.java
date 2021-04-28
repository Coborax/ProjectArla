package com.redheads.arla;

import com.redheads.arla.ui.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        WindowManager.setMainWindow(stage);
        WindowManager.pushScene("loginView", 1280, 720);
    }

    public static void main(String[] args) {
        launch();
    }

}