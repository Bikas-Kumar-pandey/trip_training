package com.meratransport.trip.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
public class TripLocation {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Trip trip;

	private String locationId;

	private String serialNo;

	private String activity;

	private LocalDateTime activityTime;

	private LocalDateTime inTime = LocalDateTime.now() ;

	private LocalDateTime outTime;

	private String serviceTime;

	@OneToOne(mappedBy = "location")
	private TripDevicesLocation devicesLocation;

	@OneToOne(mappedBy = "location")
	private TripConsignmentLocation consignmentLocation;
	
	@OneToOne(mappedBy = "location")
	private TripExpenses expense;

}
