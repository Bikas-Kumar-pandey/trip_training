package com.meratransport.trip.report.dto;

import lombok.Data;

@Data
public class TrackingReportDto {
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
