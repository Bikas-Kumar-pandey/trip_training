package com.meratransport.trip.report.dto;

import lombok.Data;

@Data
public class TripReportDto {
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

