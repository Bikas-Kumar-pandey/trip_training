package com.meratransport.trip.report.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TrackingReport {
    @Id
    private String tripId;
    private String createdTime;
    private String pickUpLocation;
    private String dropLocation;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String tripStatus;
    private String startTime;
    private String endTime;
    private String totalDuration;
}

