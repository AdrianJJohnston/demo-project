package org.openjfx.hellofx.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleID;

    @ManyToOne
    @JoinColumn(name = "driverID")
    private Driver driver;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String availabilityStatus;

    public Schedule() {}

    public Long getScheduleID() { return scheduleID; }
    public void setScheduleID(Long scheduleID) { this.scheduleID = scheduleID; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getStatus() { return availabilityStatus; }
    public void setStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
}
