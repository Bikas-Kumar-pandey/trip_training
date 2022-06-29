package com.meratransport.trip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TagInfoResponse {
	
	private String number;
	private String status;
	private String img;
	private String direction;
	private String destinationImg;
	private String type;

}
