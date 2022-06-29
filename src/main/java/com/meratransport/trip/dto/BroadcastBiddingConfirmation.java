package com.meratransport.trip.dto;

import lombok.Data;

import java.util.List;

@Data
public class BroadcastBiddingConfirmation {
//    private List<String> vendorName;
    private String id;
    private String vendorName;
   private String rank;
    private String finalAmount;

//    private String targetRate;



}