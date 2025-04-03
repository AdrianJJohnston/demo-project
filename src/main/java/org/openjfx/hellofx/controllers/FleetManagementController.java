package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class FleetManagementController {

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, Long> colVehicleID;
    @FXML
    private TableColumn<Vehicle, String> colModel;
    @FXML
    private TableColumn<Vehicle, String> colPlateNumber;
    @FXML
    private TableColumn<Vehicle, Integer> colCapacity;
    @FXML
    private TableColumn<Vehicle, String> colStatus;
    @FXML
    private TextField modelField;
    @FXML
    private TextField plateNumberField;
    @FXML
    private TextField capacityField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button addVehicleButton;
    @FXML
    private Button deleteVehicleButton;

    @FXML private Label welcomeLabel;
    @FXML private Label adminHomeLink;
    @FXML private Label fleetMLink;
    @FXML private Label userMLink;
    @FXML private Label reportLink;
    @FXML private Label profileLink;
    @FXML private ImageView logoImage;

    @FXML
    private void handleBack() {
        SceneManager.loadAdminHome();
    }


    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        //Navigation bar
        adminHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("Adminhome.fxml", "Home"));
        fleetMLink.setOnMouseClicked(event -> SceneManager.switchScene("fleet_management.fxml", "Fleet Management"));
        userMLink.setOnMouseClicked(event -> SceneManager.switchScene("UserManagement.fxml", "User Management"));
        reportLink.setOnMouseClicked(event -> SceneManager.switchScene("ReportView.fxml", "Reports"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));

        //Content
        setupTableColumns();
        loadVehiclesFromDatabase();

        statusComboBox.setItems(FXCollections.observableArrayList("Available", "In Use", "Under Maintenance"));

        addVehicleButton.setOnAction(event -> addVehicle());
        deleteVehicleButton.setOnAction(event -> deleteVehicle());
    }

    private void setupTableColumns() {
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPlateNumber.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        vehicleTable.setItems(vehicleList);
    }

    private void loadVehiclesFromDatabase() {
        EntityManager em = DatabaseUtil.getEntityManager();
        vehicleList.clear();
        try {
            Query query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
            List<Vehicle> vehicles = query.getResultList();
            vehicleList.addAll(vehicles);
        } finally {
            em.close();
        }
    }

    private void addVehicle() {
        String model = modelField.getText();
        String plateNumber = plateNumberField.getText();
        String capacityText = capacityField.getText();
        String status = statusComboBox.getValue();

        if (model.isEmpty() || plateNumber.isEmpty() || capacityText.isEmpty() || status == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Capacity must be a number!", Alert.AlertType.ERROR);
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Vehicle newVehicle = new Vehicle();
            newVehicle.setModel(model);
            newVehicle.setPlateNumber(plateNumber);
            newVehicle.setCapacity(capacity);
            newVehicle.setStatus(status);
            em.persist(newVehicle);
            em.getTransaction().commit();

            vehicleList.add(newVehicle);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Could not add vehicle: " + e.getMessage(), Alert.AlertType.ERROR);
        } finally {
            em.close();
        }
    }

    private void deleteVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("Error", "No vehicle selected!", Alert.AlertType.ERROR);
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Vehicle vehicleToDelete = em.find(Vehicle.class, selectedVehicle.getVehicleID());
            if (vehicleToDelete != null) {
                em.remove(vehicleToDelete);
                em.getTransaction().commit();
                vehicleList.remove(selectedVehicle);
            }
        } catch (Exception e) {
            showAlert("Error", "Could not delete vehicle: " + e.getMessage(), Alert.AlertType.ERROR);
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
        modelField.clear();
        plateNumberField.clear();
        capacityField.clear();
        statusComboBox.getSelectionModel().clearSelection();
    }
}
