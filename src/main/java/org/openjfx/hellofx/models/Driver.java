package org.openjfx.hellofx.models;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverID;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "vehicleID", unique = true)
    private Vehicle vehicle;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    public Driver() {}

    public Driver(User user, Vehicle vehicle, String licenseNumber) {
        this.user = user;
        this.vehicle = vehicle;
        this.licenseNumber = licenseNumber;
    }

    // Getters and Setters
    public Long getDriverID() { return driverID; }
    public void setDriverID(Long driverID) { this.driverID = driverID; }

    public User getUserID() { return user; }
    public void setUserID(User user) { this.user = user; }

    public Vehicle getVehicleID() { return vehicle; }
    public void setVehicleID(Vehicle vehicle) { this.vehicle = vehicle; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
}