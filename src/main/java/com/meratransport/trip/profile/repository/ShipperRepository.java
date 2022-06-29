package com.meratransport.trip.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.profile.entity.ShipperInfo;

public interface ShipperRepository extends JpaRepository<ShipperInfo, String>{

}
