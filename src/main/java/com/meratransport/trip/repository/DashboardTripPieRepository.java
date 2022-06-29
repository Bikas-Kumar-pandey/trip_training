package com.meratransport.trip.repository;

import com.meratransport.trip.entity.LocationFrequencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashboardTripPieRepository extends JpaRepository<LocationFrequencyEntity , String>
{
    //GET_LIST_TOTAL_COMPLETED_TRIPS - DAY, WEEK ,MONTH

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND currentStatus =\"TRIP_COMPLETED\" AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripCompletedInDay();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND currentStatus =\"TRIP_COMPLETED\" AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripCompletedInWeek();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND currentStatus =\"TRIP_COMPLETED\" AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripCompletedInMonth();

    //GET_LIST_TOTAL_INTRANSIT_TRIPS - DAY, WEEK ,MONTH
    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND nextStatus =\"IN_TRANSIT\" AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInTransitInDay();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND nextStatus =\"IN_TRANSIT\" AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInTransitInWeek();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND nextStatus =\"IN_TRANSIT\"AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInTransitInMonth();

    //GET_LIST_TOTAL_PENDING_TRIPS - DAY, WEEK ,MONTH
    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\") AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInPendingInDay();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\") AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInPendingInWeek();

    @Query(nativeQuery = true,value = "SELECT tl.locationId  as locationId , COUNT(tl.locationId) AS freq  FROM Trip t join TripLocation tl on t.id = tl.trip_id WHERE (((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) AND (currentStatus = \"TRIP_CREATED\" or currentStatus = \"START_INSTALLATION\" or currentStatus = \"SCAN_TAGS\" or currentStatus = \"ADD_MATERIAL\") AND tl.activity = \"PICKUP\") GROUP BY tl.locationId")
    List<LocationFrequencyEntity> listTripInPendingInMonth();
}
