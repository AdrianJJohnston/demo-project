package org.openjfx.hellofx.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ride")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "driverID")
    private Driver driver;

    private LocalDate rideDate;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalTime pickupTime;
    private LocalTime dropoffTime;
    private int capacityNeeded;
    private String rideStatus;

    public Ride() {}

    public Long getRideID() { return rideID; }
    public void setRideID(Long rideID) { this.rideID = rideID; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public LocalDate getRideDate() { return rideDate; }
    public void setRideDate(LocalDate rideDate) { this.rideDate = rideDate; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public LocalTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; }

    public LocalTime getDropoffTime() { return dropoffTime; }
    public void setDropoffTime(LocalTime dropoffTime) { this.dropoffTime = dropoffTime; }

    public int getCapacityNeeded() { return capacityNeeded; }
    public void setCapacityNeeded(int capacityNeeded) { this.capacityNeeded = capacityNeeded; }

    public String getRideStatus() { return rideStatus; }
    public void setRideStatus(String rideStatus) { this.rideStatus = rideStatus; }
}