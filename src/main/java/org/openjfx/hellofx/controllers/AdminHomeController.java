package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Set the text of the label after it has been injected
        welcomeLabel.setText("Welcome to the Admin Home Page!");
    }
}

