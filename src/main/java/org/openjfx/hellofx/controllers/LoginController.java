package org.openjfx.hellofx.controllers;

import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import org.mindrot.jbcrypt.BCrypt;  // Import BCrypt
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Email and password cannot be empty.");
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            // Fetch the user by email
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();

            // Compare entered password with the hashed password stored in the database
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                // Password is correct, proceed with login
                String role = user.getRole();
                switch (role.toLowerCase()) {
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
            } else {
                // Password doesn't match
                errorLabel.setText("Invalid email or password.");
            }
        } catch (NoResultException e) {
            // No user found with that email
            errorLabel.setText("Invalid email or password.");
        } finally {
            em.close();
        }
    }

    public void goToRegister() {
        SceneManager.switchScene("register.fxml", "Register");
    }
}