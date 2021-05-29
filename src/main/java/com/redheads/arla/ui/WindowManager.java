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

    /**
     * Loads a scene and pushes it to the front of the stack, and swithes to it
     * @param viewName The name of the view to load, and show
     * @param w The width the window should become
     * @param h The height the window should become
     * @throws IOException If there is an error loading the .fxml file
     */
    public static void pushScene(String viewName, int w, int h) throws IOException {
        Scene scene = new Scene(loadFXML(viewName), w, h);
        scenes.push(scene);
        mainWindow.setScene(scene);
    }

    /**
     * Will pop the top scene, and change to the one below
     */
    public static void popScene() {
        scenes.pop();
        mainWindow.setScene(scenes.peek());
    }

    /**
     * Loads the UI of a fxml file
     * @param fxml The fxml file path
     * @return A parent object, used to load the UI
     * @throws IOException If there is an erroe loading the .fxml file
     */
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

    /**
     * Will set the cursor type for the main window
     * @param cursorType The type of cursor to change to
     */
    public static void setCursorType(Cursor cursorType) {
        mainWindow.getScene().setCursor(cursorType);
    }
}
