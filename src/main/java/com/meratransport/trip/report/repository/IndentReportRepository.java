package com.meratransport.trip.report.repository;

import com.meratransport.trip.report.entity.IndentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndentReportRepository extends JpaRepository<IndentReport,String> {

    //Indent report

    @Query(value = "SELECT i.ID as indentId," +
            "i.CREATED_TIME as createdTime," +
            "i.ORDER_TYPE as orderType," +
            "i.CONTRACT_TYPE as contractType," +
            "ii.VEHICLE_NUMBER as vehicleNumber," +
            "i.VEHICLE_TYPE as vehicleType," +
            "cv.PICKUP_LOCATION as pickUpLocation," +
            "cv.DROPOFF_LOCATION as dropLocation," +
            "i.GOODS_TYPE as goodsType," +
            "cv.ACTUAL_NO_OF_PKGS as noOfPKGS," +
            "cv.ACTUAL_ESTIMATED_WEIGHT as actualWeight," +
            "cv.PICKUP_PLANNED_DATETIME as plannedPickUpDateAndTime," +
            "cv.DROPOFF_PLANNED_DATETIME as plannedDropDateAndTime," +
            "i.INDENT_REQUESTOR as indentRequestedBy, " +
            "ii.VENDOR_NAME as vendorName, " +
            "i.STATE_ID as indentStatus " +
            " FROM INDENT i,CONSIGNMENT_VIEW cv,INDENT_ITEM ii WHERE cv.PLAN_ID =i.PLAN_ID and i.ID=ii.INDENT_ID limit :limit", nativeQuery = true)
    public List<IndentReport> getAllIndent(int limit);


}
