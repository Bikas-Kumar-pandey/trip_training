package com.meratransport.trip.repository;

import com.meratransport.trip.entity.MasterReportNew;
import com.meratransport.trip.entity.TrackingReportNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackingReportNewRepository extends JpaRepository<TrackingReportNew,String> {
    @Query(value = "SELECT " +
            " t.id as tripId," +
            " t.createdTime as createdTime," +
            " glp.name as pickUp, " +
            " tv.vehicleNumber as vehicleNumber," +
            " KBV.VEHICLE_TYPE as vehicleType," +
            " KBD.NAME as driverName," +
            " KBD.MOBILE_NUMBER as driverNumber, " +
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
            " td.driverId = KBD.ID " ,nativeQuery = true)
    List<TrackingReportNew> getAllTrackingReportNew();
}
