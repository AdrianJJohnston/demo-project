package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DriverHomeController {

    @FXML private Label welcomeLabel;
    @FXML private Label driverHomeLink;
    @FXML private Label driverScheduleLink;
    @FXML private Label driverAvailabilityLink;
    @FXML private Label profileLink;

    @FXML
    public void initialize() {
        // Set the welcome text
        welcomeLabel.setText("Welcome to the Driver Schedule Page!");

        // Set navigation actions to switch scenes using SceneManager
        driverHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverHome.fxml", "Home"));
        driverScheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverSchedule.fxml", "Schedule"));
        driverAvailabilityLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverAvailability.fxml", "Availability"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
    }
}





//home.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.Region?>
<?import javafx.scene.control.Label?>

<AnchorPane xmlns="http://javafx.com/javafx/21"
          xmlns:fx="http://javafx.com/fxml/1"
          fx:controller="org.openjfx.hellofx.controllers.HomeController"
          stylesheets="@../application.css">

  <!-- Include Navigation Bar for Customer -->
  <HBox spacing="20" styleClass="nav-bar"
    AnchorPane.topAnchor="0" AnchorPane.leftAnchor="0" AnchorPane.rightAnchor="0">

      <!-- Pushes the nav links to the right -->
      <Region HBox.hgrow="ALWAYS" />

      <HBox fx:id="navLinks" spacing="20" alignment="CENTER_RIGHT">
          <Label fx:id="homeLink" text="Home" styleClass="nav-link"/>
          <Label fx:id="scheduleLink" text="Schedule" styleClass="nav-link"/>
          <Label fx:id="profileLink" text="Profile" styleClass="nav-link"/>
      </HBox>
  </HBox>
  <!-- Main Content Area -->
  <VBox alignment="CENTER" spacing="20" AnchorPane.topAnchor="50" AnchorPane.bottomAnchor="50" 
        AnchorPane.leftAnchor="50" AnchorPane.rightAnchor="50">
      <!-- White Content Box -->
      <VBox styleClass="content-box" spacing="20" alignment="CENTER">
          <Label fx:id="welcomeLabel" text="Welcome to the Customer Home Page!" />
      </VBox>
  </VBox>
</AnchorPane>

      
      
      
      
      
//driverHomeController.java
      
      package org.openjfx.hellofx.controllers;

      import javafx.fxml.FXML;
      import javafx.scene.control.Label;


      public class DriverHomeController {

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


