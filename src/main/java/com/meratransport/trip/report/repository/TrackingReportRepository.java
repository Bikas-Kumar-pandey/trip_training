package com.meratransport.trip.report.repository;

import com.meratransport.trip.report.entity.TrackingReport;
import com.meratransport.trip.report.entity.TripReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackingReportRepository extends JpaRepository<TrackingReport,String> {

    //TripReport
    @Query(value = "SELECT " +
            "TRIP.ID as tripId," +
            "TRIP.CREATED_TIME as createdTime," +
            "cv.PICKUP_LOCATION as pickUpLocation, " +
            "cv.DROPOFF_LOCATION as dropLocation, " +
            "ii.VEHICLE_NUMBER as vehicleNumber," +
            "i.VEHICLE_TYPE as vehicleType, " +
            "ii.DRIVER_NAME as driverName," +
            "ii.DRIVER_TELEPHONE_NUMBER as driverNumber," +
            "TRIP.STATE_ID as tripStatus," +
            "TRIP.TRIP_START_TIME as startTime," +
            "TRIP.TRIP_END_TIME as endTime," +
            "TRIP.TOTAL_DURATION as totalDuration " +
            " FROM INDENT i,INDENT_ITEM ii ,CONSIGNMENT_VIEW cv,TRIP,PLAN  " +
            " WHERE cv.PLAN_ID =i.PLAN_ID " +
            " and TRIP.PLAN_ID=PLAN.ID " +
            " and i.ID=ii.INDENT_ID" +
            " limit :limit", nativeQuery = true)
    public List<TrackingReport> getAllTrackingReport(int limit);
}
