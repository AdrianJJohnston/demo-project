package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HomeController {

    @FXML private Label welcomeLabel;
    @FXML private Label homeLink;
    @FXML private Label scheduleLink;
    @FXML private Label profileLink;

    @FXML
    public void initialize() {
        // Set the welcome text
        welcomeLabel.setText("Welcome to the Customer Home Page!");

        // Set navigation actions to switch scenes using SceneManager
        homeLink.setOnMouseClicked(event -> SceneManager.switchScene("home.fxml", "Home"));
        scheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("CSchedule.fxml", "Schedule"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
    }
}