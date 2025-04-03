package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CProfileController {
    @FXML private Label welcomeLabel;
    @FXML private Label homeLink;
    @FXML private Label scheduleLink;
    @FXML private Label profileLink;
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        // Set the welcome text
        welcomeLabel.setText("Welcome to the Customer PROFILE Page!");

        // Set navigation actions to switch scenes using SceneManager
        homeLink.setOnMouseClicked(event -> SceneManager.switchScene("home.fxml", "Home"));
        scheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("CSchedule.fxml", "Schedule"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
    }
}
