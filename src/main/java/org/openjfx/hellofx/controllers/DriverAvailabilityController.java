package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.openjfx.hellofx.utils.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.openjfx.hellofx.models.User;
import org.openjfx.hellofx.models.Driver;
import java.time.LocalTime;

import java.time.LocalDate;
import java.util.List;

public class DriverAvailabilityController {

    @FXML
    private TableView<Schedule> availabilityTable;
    @FXML
    private TableColumn<Schedule, LocalDate> colDate;
    @FXML
    private TableColumn<Schedule, String> colStartTime;
    @FXML
    private TableColumn<Schedule, String> colEndTime;
    @FXML
    private TableColumn<Schedule, String> colStatus;

    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button searchButton;

    @FXML
    private DatePicker newDatePicker;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private Button addButton;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Label messageLabel;
    @FXML
    private Label driverHomeLink;
    @FXML
    private Label driverScheduleLink;
    @FXML
    private Label driverAvailabilityLink;
    @FXML
    private Label profileLink;
    @FXML
    private ImageView logoImage;

    private ObservableList<Schedule> availabilityList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        
        // Navigation bar links
        driverHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverHome.fxml", "Home"));
        driverScheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverSchedule.fxml", "Schedule"));
        driverAvailabilityLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverAvailability.fxml", "Availability"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("DriverProfile.fxml", "Profile"));

        // Setup Table
        setupTableColumns();
        loadAvailabilityFromDatabase();

        // Set ComboBox items for status
        statusComboBox.setItems(FXCollections.observableArrayList("Available", "Unavailable"));

        // Set action for adding new availability
        addButton.setOnAction(event -> addAvailability());

        // Set action for searching availability
        searchButton.setOnAction(event -> searchAvailability());
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        colStartTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        colEndTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    
        availabilityTable.setItems(availabilityList);
    }

    private void loadAvailabilityFromDatabase() {
        EntityManager em = DatabaseUtil.getEntityManager();
        availabilityList.clear();
        try {
            Query query = em.createQuery("SELECT a FROM Schedule a", Schedule.class);
            List<Schedule> availabilities = query.getResultList();
            availabilityList.addAll(availabilities);
        } finally {
            em.close();
        }
    }

    private void addAvailability() {
        String loggedInUserEmail = Session.getLoggedInUser();  // Get logged-in user email
    
        // Proceed with adding availability
        LocalDate date = newDatePicker.getValue();
        String startTimeStr = startTimeField.getText();
        String endTimeStr = endTimeField.getText();
        String status = statusComboBox.getValue();
    
        if (date == null || startTimeStr.isEmpty() || endTimeStr.isEmpty() || status == null) {
            messageLabel.setText("All fields must be filled!");
            return;
        }
    
        try {
            // Convert String time values to LocalTime
            LocalTime startTime = LocalTime.parse(startTimeStr);  // Converts the startTime string to LocalTime
            LocalTime endTime = LocalTime.parse(endTimeStr);  // Converts the endTime string to LocalTime
    
            // Get the logged-in User from the email and then get the Driver
            EntityManager em = DatabaseUtil.getEntityManager();
            User loggedInUser = getLoggedInUserByEmail(loggedInUserEmail); // Assuming you have a method to get the User by email
            Driver driver = loggedInUser.getDriver(em);  // Get the associated Driver for this User
    
            if (driver == null) {
                messageLabel.setText("Driver not found for the logged-in user.");
                return;
            }
    
            em.getTransaction().begin();
            Schedule newAvailability = new Schedule();
            newAvailability.setDate(date);
            newAvailability.setStartTime(startTime);  // Set the LocalTime start time
            newAvailability.setEndTime(endTime);  // Set the LocalTime end time
            newAvailability.setStatus(status);  // Set the availability status
            newAvailability.setDriver(driver);  // Set the driver associated with the availability
            em.persist(newAvailability);
            em.getTransaction().commit();
    
            availabilityList.add(newAvailability);
            clearFields();
        } catch (Exception e) {
            messageLabel.setText("Invalid time format. Please enter times in HH:mm format.");
        }
    }

    private void searchAvailability() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            messageLabel.setText("Please select both dates!");
            return;
        }

        EntityManager em = DatabaseUtil.getEntityManager();
        availabilityList.clear();
        try {
            Query query = em.createQuery("SELECT a FROM Schedule a WHERE a.date BETWEEN :fromDate AND :toDate", Schedule.class);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            List<Schedule> availabilities = query.getResultList();
            availabilityList.addAll(availabilities);
        } finally {
            em.close();
        }
    }

    private void clearFields() {
        newDatePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        statusComboBox.getSelectionModel().clearSelection();
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
        }
    }
}