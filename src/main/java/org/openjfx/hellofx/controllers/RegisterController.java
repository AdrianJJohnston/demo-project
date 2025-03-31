package org.openjfx.hellofx.controllers;

import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void handleRegister() {
        EntityManager em = DatabaseUtil.getEntityManager();
        em.getTransaction().begin();

        User user = new User(usernameField.getText(), passwordField.getText());
        em.persist(user);

        em.getTransaction().commit();
        em.close();

        SceneManager.switchScene("login.fxml", "Login");
    }

    public void goToLogin() {
        SceneManager.switchScene("login.fxml", "Login");
    }
}
