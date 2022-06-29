package com.meratransport.trip.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="KEEBOOT_TENANT")
public class TenantInfo {
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "NAME")
	private String Name;

}
