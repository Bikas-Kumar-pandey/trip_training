package com.meratransport.trip.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "KEEBOOT_VENDOR")
public class Vendor {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@OneToOne(mappedBy = "vendor")
	private VehicleMaster master;
}
