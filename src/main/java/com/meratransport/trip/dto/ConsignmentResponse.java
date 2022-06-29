package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ConsignmentResponse {

	private String id;
	private String sku;
	private String description;
	private double weight;
	private double volume;
	private double quantity;
	private String goodsType;
	private String changeReason;

}
