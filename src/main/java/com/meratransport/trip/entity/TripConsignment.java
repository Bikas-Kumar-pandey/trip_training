package com.meratransport.trip.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class TripConsignment {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	private Trip trip;

	private String sku;

	private String description;

	private double weight;
	private double volume;
	private double quantity;

	private String goodsType;

	private String changeReason;

	@OneToOne(mappedBy = "consignment")
	private TripConsignmentLocation consingmentLocation;

}
