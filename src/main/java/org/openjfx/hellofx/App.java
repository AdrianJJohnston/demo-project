package org.openjfx.hellofx;

import org.h2.tools.Server;
import org.openjfx.hellofx.controllers.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the SceneManager with the primary stage
        SceneManager.setPrimaryStage(primaryStage);

        // Explicitly set the primaryStage to be maximized right away
        primaryStage.setMaximized(true);

        // Switch to the login scene
        SceneManager.switchScene("login.fxml", "Login");

        // Show the primary stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            // Start the H2 server (if needed)
            Server h2Server = Server.createTcpServer("-tcpAllowOthers", "-tcpDaemon").start();
            System.out.println("H2 server started at: " + h2Server.getURL());
            // Launch the JavaFX application
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}