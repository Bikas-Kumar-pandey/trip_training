package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class DamagedItems {

	private String type;
	private String reason;
	private String notes;
	private List<String> imgs;

}
