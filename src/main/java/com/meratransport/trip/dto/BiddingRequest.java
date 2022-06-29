package com.meratransport.trip.dto;

import com.meratransport.trip.entity.VendorsDetails;
import lombok.Data;

import java.util.List;

@Data
public class BiddingRequest {
    private String vendorName;
    private String TargetRate;
    private String enterYourRate;
    private String pickupLocation;
    private String dropLocation;
    private String goodsType;
    private String vehicleType;
}
