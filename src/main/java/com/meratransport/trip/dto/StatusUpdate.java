package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class StatusUpdate {
	
	private String tripId;
	
	private String status;
	
	private List<DamagedItems> damageImgs;
	
	

}
