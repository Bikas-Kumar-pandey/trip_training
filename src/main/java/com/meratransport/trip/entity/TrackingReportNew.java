package com.meratransport.trip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TrackingReportNew {
    @Id
    private String tripId;
    private String createdTime;
    private String pickUp;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String tripStatus;
}
