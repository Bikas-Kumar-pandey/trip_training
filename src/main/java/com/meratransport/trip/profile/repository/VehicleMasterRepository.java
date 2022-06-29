package com.meratransport.trip.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.profile.entity.VehicleMaster;
import com.meratransport.trip.profile.entity.VehicleNumberInfo;

public interface VehicleMasterRepository extends JpaRepository<VehicleNumberInfo ,String>{

}
