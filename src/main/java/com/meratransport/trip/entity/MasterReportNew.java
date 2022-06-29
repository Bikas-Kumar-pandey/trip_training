package com.meratransport.trip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MasterReportNew {
    @Id
    private String tripId;
    private String createdTime;
    private String transporterName;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String pickUpLocation;
    private String dropLocation;
    private String actualWeight;
    private String actualVolume;
    private String actualQuantity;
    private String lrNumber;
    private String goodsType;
    private String tripStatus;
}
