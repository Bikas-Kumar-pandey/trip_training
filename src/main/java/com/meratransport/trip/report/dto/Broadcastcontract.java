package com.meratransport.trip.report.dto;

import lombok.Data;

import java.util.List;

@Data
public class Broadcastcontract {
    private String vendorName;
    private String ids;
    private String vehicle;
    private String accept;
    private String type ;
    List<BroadcastContractDto> broadcastContractDtos;
}
