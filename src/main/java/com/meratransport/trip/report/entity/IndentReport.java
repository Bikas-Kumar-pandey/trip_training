package com.meratransport.trip.report.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class IndentReport {
    @Id
    private String indentId;
    private String createdTime;
    private String orderType;
    private String contractType;
    private String vehicleNumber;
    private String vehicleType;
    private String pickUpLocation;
    private String dropLocation;
    private String goodsType;
    private String noOfPKGS;
    private String actualWeight;
    private String plannedPickUpDateAndTime;
    private String plannedDropDateAndTime;
    private String indentRequestedBy;
    private String vendorName;
    private String indentStatus;
}

