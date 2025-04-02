package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class UserManagementController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Long> colUserID;
    @FXML
    private TableColumn<User, String> colFirstName;
    @FXML
    private TableColumn<User, String> colLastName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colPhoneNumber;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button addUserButton;
    @FXML
    private Button deleteUserButton;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void handleAddUser() {
        addUser();
    }

    @FXML
    private void handleDeleteUser() {
        deleteUser();
    }

    @FXML
    private void handleBack() {
        SceneManager.loadAdminHome();
    }


    @FXML
    public void initialize() {
        try {
            System.out.println("Initializing UserManagementController...");

            setupTableColumns();
            loadUsersFromDatabase();

            if (roleComboBox != null) {
                roleComboBox.setItems(FXCollections.observableArrayList("Customer", "Driver", "Admin"));
            } else {
                System.err.println("Error: roleComboBox is null. Check FXML file.");
            }

            if (addUserButton != null) {
                addUserButton.setOnAction(event -> addUser());
            } else {
                System.err.println("Error: addUserButton is null. Check FXML file.");
            }

            if (deleteUserButton != null) {
                deleteUserButton.setOnAction(event -> deleteUser());
            } else {
                System.err.println("Error: deleteUserButton is null. Check FXML file.");
            }

        } catch (Exception e) {
            System.err.println("Error initializing UserManagementController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTable.setItems(userList);
    }

    private void loadUsersFromDatabase() {
        EntityManager em = DatabaseUtil.getEntityManager();
        userList.clear();
        try {
            Query query = em.createQuery("SELECT u FROM User u", User.class);
            List<User> users = query.getResultList();
            userList.addAll(users);
        } catch (Exception e) {
            System.err.println("Error loading users from database: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void addUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phoneNumber = phoneNumberField.getText();
        String role = roleComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || role == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User newUser = new User(firstName, lastName, email, password, phoneNumber);
            newUser.setRole(role);
            em.persist(newUser);
            em.getTransaction().commit();

            userList.add(newUser);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Could not add user: " + e.getMessage(), Alert.AlertType.ERROR);
        } finally {
            em.close();
        }
    }

    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "No user selected!", Alert.AlertType.ERROR);
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User userToDelete = em.find(User.class, selectedUser.getUserID());
            if (userToDelete != null) {
                em.remove(userToDelete);
                em.getTransaction().commit();
                userList.remove(selectedUser);
            }
        } catch (Exception e) {
            showAlert("Error", "Could not delete user: " + e.getMessage(), Alert.AlertType.ERROR);
        } finally {
            em.close();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        phoneNumberField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

}
