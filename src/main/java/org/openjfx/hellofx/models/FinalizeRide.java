package org.openjfx.hellofx.models;

import jakarta.persistence.*;

@Entity
@Table(name = "finalize_ride")
public class FinalizeRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rideID")
    private Ride ride;

    private double amount;
    private String paymentStatus;
    private String paymentMethod;
    private Integer rating;

    public FinalizeRide() {}

    public Long getPaymentID() { return paymentID; }
    public void setPaymentID(Long paymentID) { this.paymentID = paymentID; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}