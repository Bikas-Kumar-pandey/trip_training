package com.meratransport.trip.dto;

import lombok.Data;

@Data
public class ConsignmentRequest {

	private String goodsType;
	private double weight;
	private double volume;
	private double quantity;
	private String sku;
	private String description;

}
