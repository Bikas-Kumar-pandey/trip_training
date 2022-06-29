package com.meratransport.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.entity.DeviceInfo;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, String>{

	DeviceInfo findAllByTenantId(String tenantId);

	DeviceInfo findByTenantId(String tenantId);

	int countByTenantId(String string);

}
