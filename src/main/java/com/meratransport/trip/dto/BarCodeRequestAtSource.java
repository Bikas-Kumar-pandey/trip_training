package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class BarCodeRequestAtSource {
	
	private String tripId;

	private String provider;

	private List<TagInfoAtSource> tagInfo;

}
