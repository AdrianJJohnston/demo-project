package org.openjfx.hellofx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ReportController {

    @FXML
    private ToggleButton toggleRevenue;
    @FXML
    private ToggleButton toggleStatistics;
    @FXML
    private ComboBox<String> startDateCombo;
    @FXML
    private ComboBox<String> endDateCombo;
    @FXML
    private Button generateReportButton;
    @FXML
    private TextArea reportTextArea;
    @FXML
    private void handleBack() {
        SceneManager.loadAdminHome(); // This will navigate back to Admin Home
    }


    @FXML
    public void initialize() {
        // Populate date filters with example values
        List<String> dates = Arrays.asList("Last Week", "Last Month", "Last 3 Months", "Last 6 Months", "Last Year");
        startDateCombo.getItems().addAll(dates);
        endDateCombo.getItems().addAll(dates);

        // Set event handler for report generation
        generateReportButton.setOnAction(event -> generateReport());
    }

    private void generateReport() {
        boolean includeRevenue = toggleRevenue.isSelected();
        boolean includeStats = toggleStatistics.isSelected();
        String startDate = startDateCombo.getValue();
        String endDate = endDateCombo.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select a time period!", Alert.AlertType.ERROR);
            return;
        }

        StringBuilder report = new StringBuilder("Report Summary\n");
        report.append("Time Period: ").append(startDate).append(" to ").append(endDate).append("\n");

        if (includeRevenue) {
            report.append("\n[✔] Revenue Data Included\n");
            report.append("Total Revenue: $500,000 (example data)\n");
        }

        if (includeStats) {
            report.append("\n[✔] Statistics Included\n");
            report.append("Total Rides: 15,000\n");
            report.append("Average Ride Duration: 20 minutes\n");
        }

        reportTextArea.setText(report.toString());
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
