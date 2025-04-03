package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DriverHomeController2 {

    @FXML private Label welcomeLabel;
    @FXML private Label driverHomeLink;
    @FXML private Label driverScheduleLink;
    @FXML private Label driverAvailabilityLink;
    @FXML private Label profileLink;
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        // Set the welcome text
        welcomeLabel.setText("Update Your Current Ride Status");

        // Set navigation actions to switch scenes using SceneManager
        driverHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverHome.fxml", "Home"));
        driverScheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverSchedule.fxml", "Schedule"));
        driverAvailabilityLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverAvailability.fxml", "Availability"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
    }
}