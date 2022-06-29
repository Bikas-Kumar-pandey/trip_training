package com.meratransport.trip.dto;

import com.meratransport.trip.entity.VendorsDetails;
import lombok.Data;

import java.util.List;

@Data
public class BiddingResponse {
//    private List<VendorsDetails> vendorName;
    private String TargetRate;
    private String enterYourRate;
    private String shipperId;
    private String shipperName;
    private String counterOffer;
    private String pickupLocation;
    private String dropLocation;
    private String goodsType;
    private String vehicleType;

}
