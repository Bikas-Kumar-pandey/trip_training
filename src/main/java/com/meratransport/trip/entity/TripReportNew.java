package com.meratransport.trip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TripReportNew {
    @Id
    private String tripId;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String pickUp;
    private String goodsType;
    private String tripStatus;
}
