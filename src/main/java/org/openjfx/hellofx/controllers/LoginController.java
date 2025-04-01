package org.openjfx.hellofx.controllers;

import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import jakarta.persistence.EntityManager;
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
        String email = emailField.getText();
        String password = passwordField.getText();
    
        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Email and password cannot be empty.");
            return;
        }
    
        EntityManager em = DatabaseUtil.getEntityManager();
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
    
        if (!query.getResultList().isEmpty()) {
            User user = query.getSingleResult();
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
            errorLabel.setText("Invalid email or password.");
        }
    
        em.close();
    }

    public void goToRegister() {
        SceneManager.switchScene("register.fxml", "Register");
    }
}