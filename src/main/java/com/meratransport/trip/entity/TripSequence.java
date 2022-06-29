package com.meratransport.trip.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class TripSequence {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
}
