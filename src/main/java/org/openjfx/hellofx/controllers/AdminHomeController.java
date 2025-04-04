package org.openjfx.hellofx.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListCell;  
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.Driver;
import org.openjfx.hellofx.models.Ride;
import java.time.LocalTime;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import java.time.LocalDate;

public class AdminHomeController {

    @FXML private TableView<Ride> rideTable;
    @FXML private TableColumn<Ride, Long> rideIdColumn;
    @FXML private TableColumn<Ride, String> pickupColumn;
    @FXML private TableColumn<Ride, String> dropoffColumn;
    @FXML private TableColumn<Ride, String> pickupTimeColumn;
    @FXML private TableColumn<Ride, String> dropoffTimeColumn;
    @FXML private TableColumn<Ride, String> statusColumn;
    @FXML private TableColumn<Ride, LocalDate> rideDateColumn;  // Add column for rideDate
    @FXML private TableColumn<Ride, Long> driverIdColumn;

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private CheckBox filterByDriverZero;

    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<Driver> driverComboBox;
    @FXML private TextField dropoffTimeField;

    //Nav Bar
    @FXML private Label adminHomeLink;
    @FXML private Label fleetMLink;
    @FXML private Label userMLink;
    @FXML private Label reportLink;
    @FXML private Label profileLink;
    @FXML private ImageView logoImage;

    private ObservableList<Ride> rideList = FXCollections.observableArrayList();

    @FXML public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        // Navigation bar links
        adminHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("Adminhome.fxml", "Home"));
        fleetMLink.setOnMouseClicked(event -> SceneManager.switchScene("fleet_management.fxml", "Fleet Management"));
        userMLink.setOnMouseClicked(event -> SceneManager.switchScene("UserManagement.fxml", "User Management"));
        reportLink.setOnMouseClicked(event -> SceneManager.switchScene("ReportView.fxml", "Reports"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));

        // Initialize TableView columns
        rideIdColumn.setCellValueFactory(new PropertyValueFactory<>("rideID"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        dropoffColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
        pickupTimeColumn.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        dropoffTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("rideStatus"));
        rideDateColumn.setCellValueFactory(new PropertyValueFactory<>("rideDate"));
        driverIdColumn.setCellValueFactory(cellData -> {
            Driver driver = cellData.getValue().getDriver();
            if (driver != null) {
                return new SimpleObjectProperty<>(driver.getDriverID());
            } else {
                return new SimpleObjectProperty<>(0L);  // Return 0 if no driver assigned
            }
        });

        // Populate the status ComboBox
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "Accepted", "Denied"));
        statusComboBox.getSelectionModel().selectFirst(); // Default value

        loadRideSchedule(); // Load initial data
        loadDriverOptions(); // Load available drivers
    }

    @FXML
    public void filterRides() {
        loadRideSchedule();  // Reload the ride schedule based on current filters
    }

    @FXML
    private void loadRideSchedule() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            // Build the base query for retrieving rides
            StringBuilder queryStr = new StringBuilder("SELECT r FROM Ride r WHERE 1=1 ");
    
            // Apply date range filter if dates are selected
            if (fromDatePicker.getValue() != null) {
                queryStr.append(" AND r.rideDate >= :fromDate");
            }
            if (toDatePicker.getValue() != null) {
                queryStr.append(" AND r.rideDate <= :toDate");
            }
            // Apply filter for rides with driver ID = 0 if selected
            if (filterByDriverZero.isSelected()) {
                queryStr.append(" AND r.driver.driverId = 0");
            }
    
            // Prepare the query
            Query query = em.createQuery(queryStr.toString(), Ride.class);
    
            // Set the parameters for date range filter if available
            if (fromDatePicker.getValue() != null) {
                query.setParameter("fromDate", fromDatePicker.getValue());
            }
            if (toDatePicker.getValue() != null) {
                query.setParameter("toDate", toDatePicker.getValue());
            }
    
            // Execute the query and get the results
            List<Ride> rides = query.getResultList();
    
            // Log the number of rides retrieved for debugging
            System.out.println("Number of rides found: " + rides.size());
    
            // Check if no rides are found
            if (rides.isEmpty()) {
                System.out.println("No rides found with the given filters.");
            }
    
            // Set the results to the ObservableList for the TableView
            rideList.setAll(rides);
    
            // Update the TableView with the new data
            rideTable.setItems(rideList);
    
        } catch (Exception e) {
            // Log any exceptions that occur during the process
            e.printStackTrace();
            // Show an alert to the user in case of an error
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading ride schedule: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } finally {
            em.close();
        }
    }

    // Load available drivers for ComboBox
    private void loadDriverOptions() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT d FROM Driver d", Driver.class);
            List<Driver> drivers = query.getResultList();
            System.out.println("Loaded Drivers: " + drivers);  // Debugging the loaded drivers
            
            // Set the items for the ComboBox with driver objects
            driverComboBox.setItems(FXCollections.observableArrayList(drivers));
    
            // Customize the display of the ComboBox to show driver ID
            driverComboBox.setCellFactory(param -> new ListCell<Driver>() {
                @Override
                protected void updateItem(Driver item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(String.valueOf(item.getDriverID())); // Display driver ID
                    } else {
                        setText(null);
                    }
                }
            });
            
            // Display driver ID in the ComboBox's selected item
            driverComboBox.setButtonCell(new ListCell<Driver>() {
                @Override
                protected void updateItem(Driver item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(String.valueOf(item.getDriverID())); // Display driver ID
                    } else {
                        setText(null);
                    }
                }
            });
        } finally {
            em.close();
        }
    }

    @FXML
    private void saveRideChanges() {
        Ride selectedRide = rideTable.getSelectionModel().getSelectedItem();
        
        if (selectedRide != null) {
            // Get the selected driver from the ComboBox (this is the driver you want to assign)
            Driver selectedDriver = driverComboBox.getValue();  // This retrieves the selected driver from the ComboBox
            String selectedStatus = statusComboBox.getValue();
            
            // Get the dropoff time input from the TextField
            String dropoffTimeInput = dropoffTimeField.getText();
            
            // Debugging: Print the selected ride and status
            System.out.println("Selected Ride: " + selectedRide);
            System.out.println("Selected Driver: " + (selectedDriver != null ? selectedDriver.getDriverID() : "No driver selected"));
            System.out.println("Selected Status: " + selectedStatus);
            System.out.println("Dropoff Time Input: " + dropoffTimeInput);
            
            // Validate the dropoff time input format (HH:mm)
            try {
                LocalTime dropoffTime = LocalTime.parse(dropoffTimeInput);  // Validate the format
                
                // Debugging: Print the parsed dropoff time
                System.out.println("Parsed Dropoff Time: " + dropoffTime);
                
                // Condition 1: If status = "Accepted", the dropoff time must be after pickup time and driver should not be 0
                boolean condition1 = selectedStatus.equals("Accepted") && selectedDriver != null && selectedDriver.getDriverID() != 0 && dropoffTime.isAfter(selectedRide.getPickupTime());
                // Condition 2: If status = "Denied", changes can be saved regardless of dropoff time and driver
                boolean condition2 = selectedStatus.equals("Denied");
                
                // Debugging: Print the condition checks
                System.out.println("Condition 1 (Accepted): " + condition1);
                System.out.println("Condition 2 (Denied): " + condition2);
                
                if (condition1 || condition2) {
                    // Update ride details
                    selectedRide.setDriver(selectedDriver);  // Update the driver with the selected driver from ComboBox
                    selectedRide.setDropoffTime(dropoffTime);
                    selectedRide.setRideStatus(selectedStatus);
                    
                    // Debugging: Print updated ride details
                    System.out.println("Updated Ride: " + selectedRide);
                    
                    // Save the updated ride schedule to the database
                    EntityManager em = DatabaseUtil.getEntityManager();
                    try {
                        em.getTransaction().begin();
                        em.merge(selectedRide);  // Merge the ride object with the database
                        em.getTransaction().commit();
                        
                        // Debugging: Print successful commit message
                        System.out.println("Transaction committed successfully!");
                        
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Changes saved successfully!", ButtonType.OK);
                        successAlert.showAndWait();
                    } catch (Exception e) {
                        // Handle any exceptions during the transaction
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error saving changes. Please try again.", ButtonType.OK);
                        errorAlert.showAndWait();
                    } finally {
                        em.close();
                    }
                } else {
                    // Show a warning alert if conditions are not met
                    System.out.println("Conditions not met for saving changes.");
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot save changes: Conditions not met.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (Exception e) {
                // Handle invalid dropoff time format
                System.out.println("Error parsing dropoff time: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid dropoff time format. Please use HH:mm.", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            // Handle case where no ride is selected
            System.out.println("No ride selected.");
        }
    }
}

