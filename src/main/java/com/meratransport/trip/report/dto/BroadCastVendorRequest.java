package com.meratransport.trip.report.dto;

import lombok.Data;

import java.util.List;
@Data
public class BroadCastVendorRequest {
    private  String targetRate;
    private List<BroadCastTargetRate> names;

}
