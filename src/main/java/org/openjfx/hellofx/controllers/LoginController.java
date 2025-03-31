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
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        EntityManager em = DatabaseUtil.getEntityManager();
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        if (!query.getResultList().isEmpty()) {
            SceneManager.switchScene("home.fxml", "Home");
        } else {
            errorLabel.setText("Invalid credentials");
        }
        em.close();
    }

    public void goToRegister() {
        SceneManager.switchScene("register.fxml", "Register");
    }
}