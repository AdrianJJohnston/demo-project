package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavigationBarController {

    @FXML private HBox navLinks;
    @FXML private ImageView logoImage;

    // This will be called to update the nav bar links based on user role
    public void updateNavLinks(String userRole) {
        // Clear existing links
        navLinks.getChildren().clear();

        // Add the appropriate links based on the role
        switch (userRole) {
            case "Admin":
                navLinks.getChildren().addAll(
                    createNavLink("Dashboard"),
                    createNavLink("Users"),
                    createNavLink("Settings")
                );
                break;
            case "Customer":
                navLinks.getChildren().addAll(
                    createNavLink("Home"),
                    createNavLink("Orders"),
                    createNavLink("Profile")
                );
                break;
            case "Driver":
                navLinks.getChildren().addAll(
                    createNavLink("Dashboard"),
                    createNavLink("Deliveries"),
                    createNavLink("Profile")
                );
                break;
            default:
                // Default for guests or unauthorized users
                navLinks.getChildren().add(createNavLink("Login"));
                break;
        }

        // Set the logo image dynamically
        logoImage.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
    }

    // Helper method to create a clickable navigation link
    private Label createNavLink(String text) {
        Label link = new Label(text);
        link.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        link.setOnMouseClicked(event -> handleNavigation(text));
        return link;
    }

    // Handle the navigation logic (can be expanded for different pages)
    private void handleNavigation(String linkText) {
        System.out.println("Navigating to: " + linkText);
        // Here you can switch scenes based on the clicked link
    }
}
