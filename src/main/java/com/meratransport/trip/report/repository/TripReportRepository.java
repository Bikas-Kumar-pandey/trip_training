package com.meratransport.trip.report.repository;

import com.meratransport.trip.report.entity.TrackingReport;
import com.meratransport.trip.report.entity.TripReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripReportRepository extends JpaRepository<TripReport,String> {
    @Query(value = "SELECT " +
            "TRIP.ID as tripId," +
            "i.ORDER_TYPE as orderType," +
            "i.CONTRACT_TYPE as contractType," +
            "ii.VEHICLE_NUMBER as vehicleNumber," +
            "i.VEHICLE_TYPE as vehicleType," +
            "ii.DRIVER_NAME as driverName," +
            "ii.DRIVER_TELEPHONE_NUMBER as driverNumber," +
            "cv.PICKUP_LOCATION as pickUpLocation, " +
            "cv.DROPOFF_LOCATION as dropLocation, " +
            "i.GOODS_TYPE as goodsType, " +
            "TRIP.TRIP_START_TIME as startTime," +
            "TRIP.TRIP_END_TIME as endTime," +
            "TRIP.STATE_ID as tripStatus," +
            "TRIP.TOTAL_DURATION as totalDuration" +
            " FROM INDENT i,INDENT_ITEM ii ,CONSIGNMENT_VIEW cv,TRIP,PLAN  " +
            " WHERE cv.PLAN_ID =i.PLAN_ID " +
            " and TRIP.PLAN_ID=PLAN.ID " +
            " and i.ID=ii.INDENT_ID" +
            " limit :limit", nativeQuery = true)
    public List<TripReport> getAllTripReport(int limit);
}
