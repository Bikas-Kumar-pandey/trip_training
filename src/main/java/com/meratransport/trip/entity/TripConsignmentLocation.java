package com.meratransport.trip.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class TripConsignmentLocation {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TripConsignment_id", referencedColumnName = "id")
	private TripConsignment consignment;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TripLocation_id", referencedColumnName = "id")
	private TripLocation location;

	private double quantity;

	private String notes;

	private String status;

	private String lrNumber;

	private String lr_info;
	private String podNumber;
	private String pod_info;

	private String ewayBill;

	private String ewayBillInfo;

}
