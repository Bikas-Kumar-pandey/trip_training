package com.meratransport.trip.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="MERA_VEHICLE")
public class VehicleNumberInfo {
	
	@Id
	@Column(name = "ID")
	private String id;

	@Column(name="VEHICLE_NUMBER")
	private String vehicleNumber;
	
	@OneToOne(mappedBy = "vehicle")
	private VehicleMaster master;

}
