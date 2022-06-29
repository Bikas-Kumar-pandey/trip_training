package com.meratransport.trip.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.entity.Trip;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, String>{

	List<Trip> findAllByTenantId(String teanantId, Pageable pageable);

	@Query(value = "SELECT " +
			" t.id as tripId," +
			" t.createdTime as createdTime," +
			" t.transporterName as transporterName," +
			" tv.vehicleNumber as vehicleNumber," +
			" KBV.VEHICLE_TYPE as vehicleType," +
			" KBD.NAME as driverName," +
			" KBD.MOBILE_NUMBER as driverNumber, " +
			" glp.name as pickUp, " +
			" tc.weight as actualWeight," +
			" tc.volume as actualVolume," +
			" tc.quantity as actualQuantity," +
			" tcl.lrNumber as lrNumber," +
			" tc.goodsType as goodsType," +
			" t.currentStatus as tripStatus " +
			" FROM Trip t" +
			" join TripConsignment tc on" +
			" t.id = tc.trip_id " +
			" join TripLocation tl on " +
			" t.id = tl.trip_id " +
			" join " +
			" (select * from kb_location.geo_locations) glp on " +
			" (glp.location_id = tl.locationId and tl.activity='PICKUP') " +
			" join TripConsignmentLocation tcl on" +
			" tcl.TripConsignment_id =tc.id " +
			" JOIN TripVehicles tv on " +
			" tv.trip_id =t.id " +
			" join KB_PROFILE_NEW.MERA_VEHICLE MV on " +
			" tv.vehicleId = MV.ID " +
			" join KB_PROFILE_NEW.KEEBOOT_VEHICLE KBV on " +
			" KBV.VEHICLE_ID = MV.ID " +
			" join TripDriver td on " +
			" t.id=td.trip_id " +
			" join KB_PROFILE_NEW.KEEBOOT_DRIVER KBD ON " +
			" td.driverId = KBD.ID " +
			" limit 5",nativeQuery = true)
	List<Object> getAllMasterReportNew();

	List<Trip> findAllByTenantId(String tenantId);

	List<Trip> findAllByTenantIdAndVehiclesVehicleNumberLike(String string, String string2, Pageable pageable);

	//GET_TOTAL_TRIPS - DAY,WEEK,MONTH
	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE())")
	int countTotalTripsInDay();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE())")
	int countTotalTripsInWeek();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE())")
	int countTotalTripsInMonth();

	//GET_TOTAL_PENDING_TRIPS - DAY,WEEK,MONTH
	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\"))")
	int countTotalPendingTripsInDay();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\"))")
	int countTotalPendingTripsInWeek();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\"))")
	int countTotalPendingTripsInMonth();

	//GET_TOTAL_INTRANSIT_TRIPS - DAY,WEEK,MONTH
	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (nextStatus = \"IN_TRANSIT\"))")
	int countTotalInTransitTripsInDay();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (nextStatus = \"IN_TRANSIT\"))")
	int countTotalInTransitTripsInWeek();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (nextStatus = \"IN_TRANSIT\"))")
	int countTotalInTransitTripsInMonth();

	//GET_TOTAL_CREATED_TRIPS - DAY,WEEK,MONTH
	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_CREATED'))")
	int countTotalCreatedTripsInDay();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_CREATED'))")
	int countTotalCreatedTripsInWeek();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_CREATED'))")
	int countTotalCreatedTripsInMonth();

	//GET_TOTAL_COMPLETED_TRIPS - DAY,WEEK,MONTH
	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_COMPLETED'))")
	int countTotalCompletedTripsInDay();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_COMPLETED'))")
	int countTotalCompletedTripsInWeek();

	@Query(nativeQuery = true,value = "SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus  = 'TRIP_COMPLETED'))")
	int countTotalCompletedTripsInMonth();

}
