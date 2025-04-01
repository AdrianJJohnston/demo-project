package org.openjfx.hellofx.controllers;

import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;

    public void handleRegister() {
        EntityManager em = DatabaseUtil.getEntityManager();

        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String phone = phoneNumberField.getText().trim();

            // Debugging output
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            System.out.println("Phone: " + phone);

            // Validate required fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                showAlert("Registration Error", "All fields must be filled.");
                return;
            }

            // Check if email or phone already exists
            if (isEmailOrPhoneExists(em, email, phone)) {
                showAlert("Registration Error", "Email or phone number already in use.");
                return;
            }

            // Create and persist the user
            User user = new User(firstName, lastName, email, password, phone);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            System.out.println("User registered successfully.");
            SceneManager.switchScene("login.fxml", "Login");
        } catch (Exception e) {
            showAlert("Error", "Something went wrong during registration.");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private boolean isEmailOrPhoneExists(EntityManager em, String email, String phone) {
        TypedQuery<Long> emailQuery = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        emailQuery.setParameter("email", email);
        long emailCount = emailQuery.getSingleResult();

        TypedQuery<Long> phoneQuery = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.phoneNumber = :phone", Long.class);
        phoneQuery.setParameter("phone", phone);
        long phoneCount = phoneQuery.getSingleResult();

        return emailCount > 0 || phoneCount > 0;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToLogin() {
        SceneManager.switchScene("login.fxml", "Login");
    }
}