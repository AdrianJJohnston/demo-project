package org.openjfx.hellofx.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + fxmlFile);
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


}
