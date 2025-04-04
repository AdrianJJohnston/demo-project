package org.openjfx.hellofx.controllers;


import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.stage.Screen;

import javafx.stage.Stage;

import javafx.geometry.Rectangle2D;

import org.openjfx.hellofx.models.User;

import java.io.IOException;

import java.util.prefs.Preferences;


public class SceneManager {

    private static Stage primaryStage;

    private static double lastWidth;

    private static double lastHeight;

    private static boolean isFirstRun = true; // Tracks if this is the first time running the app

    private static User currentUser;

    // Preferences to store window size

    private static Preferences prefs = Preferences.userRoot().node(SceneManager.class.getName());


    // Set the primary stage for the application

    public static void setPrimaryStage(Stage stage) {

        primaryStage = stage;


        // Get the screen size (maximized size)

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();


        // Initialize lastWidth and lastHeight to the screen's max size if it's the first run

        if (isFirstRun) {

            lastWidth = screenSize.getWidth();

            lastHeight = screenSize.getHeight();

        } else {

            // Load saved window size from Preferences (if user has resized the window)

            lastWidth = prefs.getDouble("windowWidth", lastWidth); // Default to max size

            lastHeight = prefs.getDouble("windowHeight", lastHeight); // Default to max size

        }


        // Listener for "resize events" to track the window size

        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> lastWidth = newWidth.doubleValue());

        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> lastHeight = newHeight.doubleValue());

    }


    // Switches scenes and set the stage size accordingly

    public static void switchScene(String fxmlFile, String title) {

        try {

            // Get the screen size of the primary monitor

            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();


            // Load the FXML for the scene located in main\resources\views

            Parent root = FXMLLoader.load(SceneManager.class.getResource("/views/" + fxmlFile));


            // Set the title for the stage

            primaryStage.setTitle(title);


            // Set the scene with the loaded root node

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);


            // Set the stage size based on the saved size or the initial size (MAX)

            if (isFirstRun) {

                primaryStage.setWidth(lastWidth);

                primaryStage.setHeight(lastHeight);

                primaryStage.setMaximized(true); // Maximize window on first run

                isFirstRun = false;

            } else {

                primaryStage.setWidth(lastWidth);

                primaryStage.setHeight(lastHeight);

            }


            // showing the stage we just set

            primaryStage.show();


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // New method for switching to Fleet Management scene
    public static void switchToFleetManagement() {
        switchScene("fleet_management.fxml", "Fleet Management");
    }
    //method for switching to report page
    public static void loadReportPage() {
        switchScene("ReportView.fxml", "Generate Reports");
    }
    public static void loadAdminHome() {
        switchScene("AdminHome.fxml", "Admin Dashboard");
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
