package com.meratransport.trip.dto;

import com.meratransport.trip.report.dto.BroadCastTargetRate;
import lombok.Data;

import java.util.List;

@Data
public class BroadcastDetailsContract {

    private String accept;
    private String type ;
    private String vehicle;
    private List<BroadCastTargetRate> vendorNameAndId;


}