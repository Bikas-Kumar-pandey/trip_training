package com.meratransport.trip.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class TripVehicles extends BaseEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	// Master Table Reference
	private String vehicleId;

	@ManyToOne(fetch = FetchType.LAZY)
	private Trip trip;
	
	private String vehicleNumber;

	private boolean isActive;

	private String status;

	private String changeReason;

}
