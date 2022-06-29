package com.meratransport.trip.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class TripDevicesLocation extends BaseEntity {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String tagId;

	@ManyToOne
	@JsonManagedReference
	private Trip trip;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TripLocation_id", referencedColumnName = "id")
	private TripLocation location;

	private String deviceType;

	private String status;

	private String verificationStatus;

	private String notes;

}
