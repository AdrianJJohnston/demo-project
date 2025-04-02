package org.openjfx.hellofx.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ride")
public class RideSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIDEID")
    private int rideId;

    @Column(name = "PICKUPLOCATION")
    private String pickupLocation;

    @Column(name = "DROPOFFLOCATION")
    private String dropoffLocation;

    @Column(name = "PICKUPTIME")
    private String pickupTime;

    @Column(name = "DROPOFFTIME")
    private String dropoffTime;

    @Column(name = "RIDESTATUS")
    private String rideStatus;

    @Column(name = "USERID")
    private int userId;

    @Column(name = "DRIVERID")
    private int driverId;

    // Constructors
    public RideSchedule() {}

    public RideSchedule(String pickupLocation, String dropoffLocation, String pickupTime, String dropoffTime, String rideStatus, int userId, int driverId) {
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.rideStatus = rideStatus;
        this.userId = userId;
        this.driverId = driverId;
    }

    // Getters and Setters
    public int getRideId() { return rideId; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; }
    public String getPickupTime() { return pickupTime; }
    public String getDropoffTime() { return dropoffTime; }
    public String getRideStatus() { return rideStatus; }
    public int getUserId() { return userId; }
    public int getDriverId() { return driverId; }

    public void setRideId(int rideId) { this.rideId = rideId; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }
    public void setDropoffTime(String dropoffTime) { this.dropoffTime = dropoffTime; }
    public void setRideStatus(String rideStatus) { this.rideStatus = rideStatus; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
}
