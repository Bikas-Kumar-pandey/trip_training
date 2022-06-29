package com.meratransport.trip.picklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.picklist.entity.CommonPickList;

public interface PickListRepository extends JpaRepository<CommonPickList, String>{

	CommonPickList findByCommonKey(String vehicleType);

}
