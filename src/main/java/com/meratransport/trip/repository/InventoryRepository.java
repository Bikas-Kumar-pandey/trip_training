package com.meratransport.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meratransport.trip.entity.DeviceLog;

public interface InventoryRepository extends JpaRepository<DeviceLog, String> {

	List<DeviceLog> findByTripId(String tripId);

	List<DeviceLog> findBySerialNoInAndType(List<String> numbers, String type);

	List<DeviceLog> findByTripIdAndSerialNoIn(String tripId, List<String> tagNumber);

	List<DeviceLog> findByTripIdAndType(String tripId, String string);

	int countByTenantId(String string);

	List<DeviceLog> findByTripIdAndTypeAndStatus(String id, String type, String status);

	List<DeviceLog> findByTenantId(String string);

	List<DeviceLog> findByTenantIdAndType(String tenantId, String type);

	DeviceLog findByTenantIdAndTypeAndSerialNo(String string, String string2, String gpsNumber);

	List<DeviceLog> findBySerialNoInAndTypeAndTenantId(List<String> tags, String string, String string2);

}
