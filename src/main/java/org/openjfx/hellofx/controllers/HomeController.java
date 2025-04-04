package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.Ride;
import org.openjfx.hellofx.models.User;
import org.openjfx.hellofx.utils.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public class HomeController {

    @FXML private ImageView logoImage;
    @FXML private Label welcomeLabel;
    @FXML private Label homeLink;
    @FXML private Label scheduleLink;
    @FXML private Label profileLink;

    // Ride booking form inputs
    @FXML private DatePicker rideDatePicker;
    @FXML private TextField pickupLocationField;
    @FXML private TextField dropoffLocationField;
    @FXML private TextField pickupTimeField;
    @FXML private TextField capacityNeededField;
    @FXML private Button bookRideButton;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        // Set the welcome text
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));

        welcomeLabel.setText("Welcome to the Customer Home Page!");

        // Set navigation actions to switch scenes using SceneManager
        homeLink.setOnMouseClicked(event -> SceneManager.switchScene("home.fxml", "Home"));
        scheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("CSchedule.fxml", "Schedule"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));

        // Set action for the book ride button
        bookRideButton.setOnAction(event -> bookRide());
    }

    private void bookRide() {
        String loggedInUserEmail = Session.getLoggedInUser(); // Get logged-in user email

        // Get the input values
        LocalDate rideDate = rideDatePicker.getValue();
        String pickupLocation = pickupLocationField.getText();
        String dropoffLocation = dropoffLocationField.getText();
        String pickupTimeStr = pickupTimeField.getText();
        String capacityNeededStr = capacityNeededField.getText();

        if (rideDate == null || pickupLocation.isEmpty() || dropoffLocation.isEmpty() || pickupTimeStr.isEmpty() || capacityNeededStr.isEmpty()) {
            messageLabel.setText("All fields must be filled!");
            return;
        }

        try {
            // Convert the time from string to LocalTime
            LocalTime pickupTime = LocalTime.parse(pickupTimeStr);
            int capacityNeeded = Integer.parseInt(capacityNeededStr);

            // Get the logged-in user from the email and then get the Driver (null for now)
            EntityManager em = DatabaseUtil.getEntityManager();
            User loggedInUser = getLoggedInUserByEmail(loggedInUserEmail); // Assuming you have a method to get the User by email

            if (loggedInUser == null) {
                messageLabel.setText("User not found.");
                return;
            }

            em.getTransaction().begin();

            // Create and persist the new Ride
            Ride newRide = new Ride();
            newRide.setUser(loggedInUser);  // Set the user to the logged-in user
            newRide.setRideDate(rideDate);
            newRide.setPickupLocation(pickupLocation);
            newRide.setDropoffLocation(dropoffLocation);
            newRide.setPickupTime(pickupTime);
            newRide.setCapacityNeeded(capacityNeeded);
            newRide.setRideStatus("Pending");  // Default status
            newRide.setDriver(null);  // Driver is not assigned yet

            em.persist(newRide);
            em.getTransaction().commit();

            messageLabel.setText("Ride booked successfully!");
            clearFields(); // Clear the form after successful booking
        } catch (Exception e) {
            messageLabel.setText("Invalid input format. Please check your fields.");
        }
    }

    private User getLoggedInUserByEmail(String email) {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            // Query the User table by email
            Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null; // No user found with that email
        } finally {
            em.close();
        }
    }

    private void clearFields() {
        rideDatePicker.setValue(null);
        pickupLocationField.clear();
        dropoffLocationField.clear();
        pickupTimeField.clear();
        capacityNeededField.clear();
    }
}