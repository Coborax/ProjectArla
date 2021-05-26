package com.redheads.arla.ui;

import com.redheads.arla.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class WindowManager {


    private static Stage mainWindow;
    private static Stack<Scene> scenes = new Stack<>();

    public static void pushScene(String viewName, int w, int h) throws IOException {
        Scene scene = new Scene(loadFXML(viewName), w, h);
        scenes.push(scene);
        mainWindow.setScene(scene);
    }

    public static void popScene() {
        scenes.pop();
        mainWindow.setScene(scenes.peek());
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getMainWindow() {
        return mainWindow;
    }

    public static void setMainWindow(Stage mainWindow) {
        WindowManager.mainWindow = mainWindow;
        Image ico = new Image("imgs/arla-logo.png");
        mainWindow.getIcons().add(ico);
        mainWindow.setTitle("Project Arla");
        mainWindow.show();
    }

    public static void setCursorType(Cursor cursorType) {
        mainWindow.getScene().setCursor(cursorType);
    }
}
