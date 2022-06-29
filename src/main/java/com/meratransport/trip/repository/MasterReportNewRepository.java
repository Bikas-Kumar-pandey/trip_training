package com.meratransport.trip.repository;

import com.meratransport.trip.entity.MasterReportNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterReportNewRepository extends JpaRepository<MasterReportNew,String> {
    @Query(value = "SELECT " +
            " t.id as tripId," +
            " t.createdTime as createdTime," +
            " t.transporterName as transporterName," +
            " tv.vehicleNumber as vehicleNumber," +
            " KBV.VEHICLE_TYPE as vehicleType," +
            " KBD.NAME as driverName," +
            " KBD.MOBILE_NUMBER as driverNumber, " +
            " tl1.locationId as pickUpLocation, " +
            " tl2.locationId as dropLocation, " +
            " tc.weight as actualWeight," +
            " tc.volume as actualVolume," +
            " tc.quantity as actualQuantity," +
            " tcl.lrNumber as lrNumber," +
            " tc.goodsType as goodsType," +
            " t.currentStatus as tripStatus " +
            " FROM Trip t" +
            " join TripConsignment tc on" +
            " t.id = tc.trip_id " +
            " join " +
            " (select * from TripLocation where activity='PICKUP') tl1 on " +
            " t.id = tl1.trip_id " +
            " join " +
            " (select * from TripLocation where activity='DROP') tl2 on " +
            " t.id = tl2.trip_id " +
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
    List<MasterReportNew> getAllMasterReportNew();
}
