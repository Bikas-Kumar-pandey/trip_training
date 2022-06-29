package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class MaterialRequest {
	
	private String tripId;
	
	private List<String> imgs;
	
}
