package com.meratransport.trip.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.location.entity.LocationMaster;

public interface LocationMasterRepository extends JpaRepository<LocationMaster, String> {

	LocationMaster findByLocationId(String locationId);

}
