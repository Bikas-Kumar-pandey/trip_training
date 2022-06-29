package com.meratransport.trip.profile.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="KEEBOOT_VEHICLE")
public class VehicleMaster {

	@Id
	@Column(name = "ID")
	private String id;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="VEHICLE_ID")
	private VehicleNumberInfo vehicle;
	
	@Column(name = "VEHICLE_TYPE")
	private String vehicleType;
	
	@Column(name = "OWNERSHIP_TYPE")
	private String ownershipType;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="VENDOR_ID")
	private Vendor vendor;
	
	
	
	
	


}
