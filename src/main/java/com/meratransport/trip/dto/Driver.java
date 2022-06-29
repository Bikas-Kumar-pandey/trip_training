package com.meratransport.trip.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meratransport.trip.entity.Images;

import lombok.Data;

@Data
public class Driver {

	private String id;

	private String name;

	private String shortName;

	private String mobileNumber;

	private String title;

	private String ownershipType;

	private String language;

	private String emailId;

	List<Images> imgs;

}
