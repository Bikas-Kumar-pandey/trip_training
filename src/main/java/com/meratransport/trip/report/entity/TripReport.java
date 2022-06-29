package com.meratransport.trip.report.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TripReport {
    @Id
    private String tripId;
    private String orderType;
    private String contractType;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String pickUpLocation;
    private String dropLocation;
    private String goodsType;
    private String startTime;
    private String endTime;
    private String tripStatus;
    private String totalDuration;

}

