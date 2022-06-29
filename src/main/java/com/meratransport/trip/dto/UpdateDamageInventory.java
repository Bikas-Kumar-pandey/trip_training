package com.meratransport.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateDamageInventory {

	private Location location;

	private String techName;

	private String reason;
	private String notes;

	List<DamagedTagsInfo> info;

}
