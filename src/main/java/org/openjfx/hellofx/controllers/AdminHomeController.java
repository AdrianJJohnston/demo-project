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
        //Navigation bar
        adminHomeLink.setOnMouseClicked(event -> SceneManager.switchScene("Adminhome.fxml", "Home"));
        fleetMLink.setOnMouseClicked(event -> SceneManager.switchScene("fleet_management.fxml", "Fleet Management"));
        userMLink.setOnMouseClicked(event -> SceneManager.switchScene("UserManagement.fxml", "User Management"));
        reportLink.setOnMouseClicked(event -> SceneManager.switchScene("ReportView.fxml", "Reports"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));

        rideIdColumn.setCellValueFactory(new PropertyValueFactory<>("rideID"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        dropoffColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
        pickupTimeColumn.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        dropoffTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("rideStatus"));
        rideDateColumn.setCellValueFactory(new PropertyValueFactory<>("rideDate"));
        driverIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDriver().getDriverID()));

        // Populate status ComboBox with options
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "Accepted", "Denied"));
        statusComboBox.getSelectionModel().selectFirst(); // Default value

        loadRideSchedule(); // Load initial data
        loadDriverOptions(); // Load available drivers
    }

    @FXML
    private void loadRideSchedule() {
        // Get EntityManager instance for database interaction
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
            // Ensure EntityManager is closed even if an exception occurs
            em.close();
        }
    }

    // Load available drivers for ComboBox
    private void loadDriverOptions() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT d FROM Driver d", Driver.class);
            List<Driver> drivers = query.getResultList();
            
            // Set the items for the ComboBox with driver IDs
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

    @FXML private void saveRideChanges() {
        Ride selectedRide = rideTable.getSelectionModel().getSelectedItem();

        if (selectedRide != null) {
            // Validate conditions for saving
            if ((selectedRide.getDriver().getDriverID() != 0 || statusComboBox.getValue().equals("Denied"))) {
                // Update ride details
                selectedRide.setDriver(driverComboBox.getValue());
                selectedRide.setDropoffTime(LocalTime.parse(dropoffTimeField.getText()));
                selectedRide.setRideStatus(statusComboBox.getValue());

                // Save updated ride schedule to the database
                EntityManager em = DatabaseUtil.getEntityManager();
                try {
                    em.getTransaction().begin();
                    em.merge(selectedRide);
                    em.getTransaction().commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    em.close();
                }
            } else {
                // Show alert if validation fails
                Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot save: Driver ID is 0 and status is not Denied.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML private void filterRides() {
        loadRideSchedule(); // Reload rides based on filters
    }
}

