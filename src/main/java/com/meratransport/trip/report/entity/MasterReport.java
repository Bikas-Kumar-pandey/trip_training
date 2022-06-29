package com.meratransport.trip.report.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MasterReport {
    @Id
    private String indentId;
    private String tripId;
    private String orderType;
    private String contractType;
    private String createdTime;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String pickUpLocation;
    private String dropLocation;
    private String plannedPickUpDateAndTime;
    private String plannedDropDateAndTime;
    private String plannedWeight;
    private String plannedVolume;
    private String plannedNoOfPKGS;
    private String actualWeight;
    private String actualVolume;
    private String actualNoOfPKGS;
    private String lrNumber;
    private String lrTime;
    private String goodsType;
    private String actualPickUpDateAndTime;
    private String actualDropUpDateAndTime;
    private String totalDuration;
    private String tripStatus;

}

