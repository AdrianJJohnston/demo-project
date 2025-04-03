package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.User;
import org.openjfx.hellofx.models.Driver;
import org.openjfx.hellofx.models.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    @FXML private Label welcomeLabel;
    @FXML private ImageView logoImage;

    //Nav Bar
    @FXML private Label adminHomeLink;
    @FXML private Label fleetMLink;
    @FXML private Label userMLink;
    @FXML private Label reportLink;
    @FXML private Label profileLink;

    // New fields for drivers
    @FXML
    private TextField licenseNumberField; // License input
    @FXML
    private ComboBox<String> vehicleComboBox; // Available vehicles dropdown

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        //Navigation bar
        adminHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("Adminhome.fxml", "Home"));
        fleetMLink.setOnMouseClicked(event -> SceneManager.switchScene("fleet_management.fxml", "Fleet Management"));
        userMLink.setOnMouseClicked(event -> SceneManager.switchScene("UserManagement.fxml", "User Management"));
        reportLink.setOnMouseClicked(event -> SceneManager.switchScene("ReportView.fxml", "Reports"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));
        
        setupTableColumns();
        loadUsersFromDatabase();
        
        roleComboBox.setItems(FXCollections.observableArrayList("Customer", "Driver", "Admin"));
        roleComboBox.setOnAction(event -> toggleDriverFields()); // Show/hide driver fields
        
        addUserButton.setOnAction(event -> addUser());
        deleteUserButton.setOnAction(event -> deleteUser());
        
        loadAvailableVehicles(); // Load vehicles into dropdown
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
        } finally {
            em.close();
        }
    }

    private void loadAvailableVehicles() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT v.plateNumber FROM Vehicle v WHERE v.status = 'Available'");
            List<String> availableVehicles = query.getResultList();
            vehicleComboBox.setItems(FXCollections.observableArrayList(availableVehicles));
        } finally {
            em.close();
        }
    }

    private void toggleDriverFields() {
        boolean isDriver = "Driver".equals(roleComboBox.getValue());
        licenseNumberField.setDisable(!isDriver);
        vehicleComboBox.setDisable(!isDriver);
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
            em.flush(); // Ensure user ID is generated

            if ("Driver".equals(role)) {
                String licenseNumber = licenseNumberField.getText();
                String selectedVehicle = vehicleComboBox.getValue();

                if (licenseNumber.isEmpty() || selectedVehicle == null) {
                    showAlert("Error", "License number and vehicle must be provided for drivers!", Alert.AlertType.ERROR);
                    return;
                }

                // Fetch the actual Vehicle entity instead of just its ID
                Query vehicleQuery = em.createQuery("SELECT v FROM Vehicle v WHERE v.plateNumber = :plate");
                vehicleQuery.setParameter("plate", selectedVehicle);
                Vehicle assignedVehicle = (Vehicle) vehicleQuery.getSingleResult(); // Get the Vehicle entity

                Driver newDriver = new Driver();
                newDriver.setUserID(newUser);  // Correct: Assigning User object, not Long
                newDriver.setLicenseNumber(licenseNumber);
                newDriver.setVehicleID(assignedVehicle);  // Correct: Assigning Vehicle object, not Long
                em.persist(newDriver);

                // Update vehicle status
                em.createQuery("UPDATE Vehicle v SET v.status = 'In Use' WHERE v.vehicleID = :id")
                    .setParameter("id", assignedVehicle.getVehicleID())
                    .executeUpdate();
            }

            em.getTransaction().commit();
            userList.add(newUser);
            clearFields();
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
        licenseNumberField.clear();
        vehicleComboBox.getSelectionModel().clearSelection();
    }
}
