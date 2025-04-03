package org.openjfx.hellofx.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.mindrot.jbcrypt.BCrypt;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import org.openjfx.hellofx.utils.Session;

public class ProfileController {
    @FXML private Label homeLink;
    @FXML private ImageView logoImage;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button saveButton;
    @FXML private Button resetPasswordButton;
    @FXML private Button logOutButton;
    @FXML private Label messageLabel;

    private User loggedInUser;

    @FXML
    public void initialize() {

        System.out.println("DEBUG: Initializing ProfileController");
        
        // Load logo
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        
        // Get logged-in user's email
        String email = Session.getLoggedInUser();

        System.out.println("DEBUG: Retrieved email from session -> " + email);

        if (email == null) {
            messageLabel.setText("Error: No user logged in.");
            System.out.println("DEBUG: No user logged in.");
            return;
        }

        // Retrieve user from database
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            loggedInUser = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                             .setParameter("email", email)
                             .getSingleResult();

            System.out.println("DEBUG: User fetched from DB -> " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

            if (loggedInUser != null) {
                // Populate fields
                firstNameField.setText(loggedInUser.getFirstName());
                lastNameField.setText(loggedInUser.getLastName());
                emailField.setText(loggedInUser.getEmail()); // Non-editable
                emailField.setDisable(true);
                phoneField.setText(loggedInUser.getPhoneNumber());
            }
        } catch (NoResultException e) {
            System.out.println("DEBUG: No user found in DB for email -> " + email);
            messageLabel.setText("Error: User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error fetching user data.");
        } finally {
            em.close();
        }

        // Nav Bar
        homeLink.setOnMouseClicked(event -> {
            if (loggedInUser != null) {
                String role = loggedInUser.getRole().toLowerCase();

                // Redirect based on role
                switch (role) {
                    case "admin":
                        SceneManager.switchScene("AdminHome.fxml", "Admin Home");
                        break;
                    case "driver":
                        SceneManager.switchScene("DriverHome.fxml", "Driver Home");
                        break;
                    case "customer":
                    default:
                        SceneManager.switchScene("home.fxml", "Home");
                        break;
                }
            }
        });

        // Save button action
        saveButton.setOnAction(event -> saveChanges());

        // Reset password button action
        resetPasswordButton.setOnAction(event -> resetPassword());

        // Log out button action
        logOutButton.setOnAction(event -> logOut());
    }

    private void saveChanges() {
        System.out.println("DEBUG: Save changes triggered");
        String newFirstName = firstNameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String newPhone = phoneField.getText().trim();

        if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhone.isEmpty()) {
            messageLabel.setText("Fields cannot be empty.");
            System.out.println("DEBUG: Empty fields detected");
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            loggedInUser.setFirstName(newFirstName);
            loggedInUser.setLastName(newLastName);
            loggedInUser.setPhoneNumber(newPhone);
            em.merge(loggedInUser);
            tx.commit();

            messageLabel.setText("Profile updated successfully.");
            System.out.println("DEBUG: Profile updated in DB");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            messageLabel.setText("Error updating profile.");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void resetPassword() {
        System.out.println("DEBUG: Reset password triggered");
    
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
    
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Password fields cannot be empty.");
            System.out.println("DEBUG: Empty password fields detected");
            return;
        }
    
        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            System.out.println("DEBUG: Passwords do not match");
            return;
        }
    
        // Hash the new password just like in the RegisterController
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        System.out.println("DEBUG: Generated Hashed Password -> " + hashedPassword);
    
        EntityManager em = DatabaseUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
    
        try {
            tx.begin();
    
            // Ensure the logged-in user entity is managed, like RegisterController
            loggedInUser = em.merge(loggedInUser);  // Ensures that the entity is managed
    
            // Set the new plain password (hashed in the setter)
            loggedInUser.setPasswordHash(newPassword);  // Pass the plain password to setPasswordHash
    
            // Commit the transaction to save the changes
            em.persist(loggedInUser);  // Save the user with the updated password (like RegisterController does)
            tx.commit();
    
            System.out.println("DEBUG: Password successfully updated in DB.");
            messageLabel.setText("Password updated successfully.");
    
            newPasswordField.clear();
            confirmPasswordField.clear();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            messageLabel.setText("Error updating password.");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    private void logOut() {
        // Clear session and log out
        Session.clearSession();
        
        // Redirect to login page or home page
        SceneManager.switchScene("login.fxml", "Login");
    }
}
