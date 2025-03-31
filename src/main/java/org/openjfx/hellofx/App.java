package org.openjfx.hellofx;

import org.openjfx.hellofx.controllers.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchScene("login.fxml", "Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}