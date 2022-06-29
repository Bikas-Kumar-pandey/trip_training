package com.meratransport.trip.report.dto;

import lombok.Data;

import java.util.List;

@Data
public class BiddingConfirmationDto {

    private String targetRate;
    List<BroadcastContractDto> broadcastContractDtos;
}
