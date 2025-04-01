package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.RideSchedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class AdminHomeController {

    @FXML
    private TableView<RideSchedule> rideTable;

    @FXML
    private TableColumn<RideSchedule, Integer> rideIdColumn;

    @FXML
    private TableColumn<RideSchedule, String> pickupColumn;

    @FXML
    private TableColumn<RideSchedule, String> dropoffColumn;

    @FXML
    private TableColumn<RideSchedule, String> pickupTimeColumn;

    @FXML
    private TableColumn<RideSchedule, String> dropoffTimeColumn;

    @FXML
    private TableColumn<RideSchedule, String> statusColumn;

    private ObservableList<RideSchedule> rideList = FXCollections.observableArrayList();

  //  @FXML
    //private void handleManageUsers() {
    //    SceneManager.openUserManagement();
    //}
  @FXML
  private void goToUserManagement() {
      System.out.println("Navigating to User Management now...");
      SceneManager.switchScene("UserManagement.fxml", "User Management");
  }



    @FXML
    public void initialize() {
        // Link columns with model attributes
        rideIdColumn.setCellValueFactory(new PropertyValueFactory<>("rideId"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        dropoffColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
        pickupTimeColumn.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        dropoffTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("rideStatus"));

        // Load ride schedule from database
        loadRideSchedule();
    }

    private void loadRideSchedule() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT r FROM RideSchedule r", RideSchedule.class);
            List<RideSchedule> rides = query.getResultList();
            rideList.setAll(rides);
            rideTable.setItems(rideList);
        } finally {
            em.close();
        }
    }


}
