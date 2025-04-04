package org.openjfx.hellofx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.openjfx.hellofx.models.DatabaseUtil;
import org.openjfx.hellofx.models.Ride;
import org.openjfx.hellofx.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class CScheduleController {
    @FXML private Label welcomeLabel;
    @FXML private Label homeLink;
    @FXML private Label scheduleLink;
    @FXML private Label profileLink;
    @FXML private ImageView logoImage;

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private TableView<Ride> rideTable;
    @FXML private TableColumn<Ride, LocalDate> dateColumn;
    @FXML private TableColumn<Ride, String> timeColumn;
    @FXML private TableColumn<Ride, String> pickupColumn;
    @FXML private TableColumn<Ride, String> dropoffColumn;
    @FXML private TableColumn<Ride, String> statusColumn;
    @FXML private Button filterButton;

    private ObservableList<Ride> rideData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/images/taxi.png").toExternalForm()));
        welcomeLabel.setText("Welcome to the Customer Schedule Page!");

        homeLink.setOnMouseClicked(event -> SceneManager.switchScene("home.fxml", "Home"));
        scheduleLink.setOnMouseClicked(event -> SceneManager.switchScene("CSchedule.fxml", "Schedule"));
        profileLink.setOnMouseClicked(event -> SceneManager.switchScene("CProfile.fxml", "Profile"));

        setupTable();
        loadRides();

        filterButton.setOnAction(e -> filterRides());
    }

    private void setupTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("rideDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        pickupColumn.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        dropoffColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("rideStatus"));

        rideTable.setItems(rideData);
    }

    private void loadRides() {
        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            User currentUser = SceneManager.getCurrentUser();
            TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.user = :user", Ride.class);
            query.setParameter("user", currentUser);
            List<Ride> rides = query.getResultList();
            rideData.setAll(rides);
        } finally {
            em.close();
        }
    }

    private void filterRides() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        EntityManager em = DatabaseUtil.getEntityManager();
        try {
            User currentUser = SceneManager.getCurrentUser();
            StringBuilder sb = new StringBuilder("SELECT r FROM Ride r WHERE r.user = :user");
            if (fromDate != null) sb.append(" AND r.rideDate >= :fromDate");
            if (toDate != null) sb.append(" AND r.rideDate <= :toDate");

            TypedQuery<Ride> query = em.createQuery(sb.toString(), Ride.class);
            query.setParameter("user", currentUser);
            if (fromDate != null) query.setParameter("fromDate", fromDate);
            if (toDate != null) query.setParameter("toDate", toDate);

            rideData.setAll(query.getResultList());
        } finally {
            em.close();
        }
    }
}
