package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DriverHomeController2 {

    @FXML private Label welcomeLabel;
    @FXML private Label driverHomeLink;
    @FXML private Label driverScheduleLink;
    @FXML private Label driverAvailabilityLink;
    @FXML private Label profileLink;

    @FXML
    public void initialize() {
        // Set the welcome text
        welcomeLabel.setText("Welcome to the Driver Home Page!");

        // Set navigation actions to switch scenes using SceneManager
        driverHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverHome.fxml", "Home"));
        driverScheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverSchedule.fxml", "Schedule"));
        driverAvailabilityLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverAvailability.fxml", "Availability"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
    }
}