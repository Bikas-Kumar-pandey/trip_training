package com.meratransport.trip.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.entity.TripDriver;

public interface TripDriverRepository extends JpaRepository<TripDriver, UUID> {

}
