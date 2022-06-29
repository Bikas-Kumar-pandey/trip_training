package com.meratransport.trip.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import com.meratransport.trip.constant.TripType;

import lombok.Data;

@Data
@Entity(name = "Trip")
public class Trip extends BaseEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@OneToOne(cascade = CascadeType.ALL)
	private TripSequence sequence;

	private TripType tripType;

	private String currentStatus;

	private String nextStatus;

	private String tenantId;
	
	private String employeeId;

	private LocalDateTime startedAt;
	
	private int tagCount;
	
	private String transporterName;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripVehicles> vehicles;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripDriver> driver;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripDevices> devices;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripDevicesLocation> deviceLocation;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripConsignment> consignment;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "trip_id")
	private List<TripLocation> location;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<TripExpenses> expense;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIP_ID")
	private List<DeviceLog> deviceInfo;

}
