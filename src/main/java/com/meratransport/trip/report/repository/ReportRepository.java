package com.meratransport.trip.report.repository;

import com.meratransport.trip.report.entity.IndentReport;
import com.meratransport.trip.report.entity.MasterReport;
import com.meratransport.trip.report.entity.TripReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<MasterReport,String> {

    //MasterReport
    @Query(value = "SELECT " +
            "i.ID as indentId, " +
            "TRIP.ID AS tripId," +
            "i.ORDER_TYPE as orderType, " +
            "i.CONTRACT_TYPE as contractType, " +
            "i.CREATED_TIME as createdTime, " +
            "ii.VEHICLE_NUMBER as vehicleNumber," +
            "i.VEHICLE_TYPE as vehicleType, " +
            "ii.DRIVER_NAME as driverName," +
            "ii.DRIVER_TELEPHONE_NUMBER as driverNumber," +
            "cv.PICKUP_LOCATION as pickUpLocation, " +
            "cv.DROPOFF_LOCATION as dropLocation, " +
            "cv.PICKUP_PLANNED_DATETIME as plannedPickUpDateAndTime, " +
            "cv.DROPOFF_PLANNED_DATETIME as plannedDropDateAndTime, " +
            "cv.PLANNED_ESTIMATED_WEIGHT as plannedWeight, " +
            "cv.PLANNED_ESTIMATED_VOLUME as plannedVolume, " +
            "cv.PLANNED_NO_OF_PKGS as plannedNoOfPKGS, " +
            "cv.ACTUAL_ESTIMATED_WEIGHT as actualWeight, " +
            "cv.ACTUAL_ESTIMATED_VOLUME as actualVolume, " +
            "cv.ACTUAL_NO_OF_PKGS as actualNoOfPKGS, " +
            "cv.LR_NUMBER as lrNumber, " +
            "cv.LR_TIME as lrTime, " +
            "i.GOODS_TYPE as goodsType, " +
            "cv.PICKUP_ACTUAL_DATETIME as actualPickUpDateAndTime, " +
            "cv.DROPOFF_ACTUAL_DATETIME as actualDropUpDateAndTime, " +
            "TRIP.TOTAL_DURATION as totalDuration," +
            "TRIP.STATE_ID as tripStatus " +
            " FROM INDENT i,INDENT_ITEM ii ,CONSIGNMENT_VIEW cv,TRIP,PLAN " +
            " WHERE cv.PLAN_ID =i.PLAN_ID " +
            " and TRIP.PLAN_ID=PLAN.ID " +
            " and i.ID=ii.INDENT_ID " +
            " limit :limit", nativeQuery = true)
    public List<MasterReport> getAllMasterReport(int limit);




}

