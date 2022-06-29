package com.meratransport.trip.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrodcastBiddingConfirmDto {
    private String targetRate;
    List<BroadcastBiddingConfirmation> broadcastBiddingConfirmations;
}
