package com.meratransport.trip.dto;

import com.meratransport.trip.report.dto.BroadCastVendorName;
import lombok.Data;

import java.util.List;

@Data
public class BroadCastVendorResponse {
    private String targetRate;
    List<BroadCastVendorName> vendorNames;

//    private String indentId;

}