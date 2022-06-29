package com.meratransport.trip.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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
public class TripExpenses {
	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JsonManagedReference
	private Trip trip;

	@OneToOne(cascade = CascadeType.ALL)
	private TripLocation location;

	private String expenseType;

	private LocalDateTime expenseDate;

	private String status;
	private double amount;
	private String currency;

	private String expensesinfo;

	private String notes;

}
