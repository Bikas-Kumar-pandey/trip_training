package com.meratransport.trip.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.entity.TripVehicles;

public interface TripVehicleRepository extends JpaRepository<TripVehicles, UUID>{

	
}
