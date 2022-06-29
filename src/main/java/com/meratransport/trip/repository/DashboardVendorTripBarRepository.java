package com.meratransport.trip.repository;

import com.meratransport.trip.entity.VendorBarGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashboardVendorTripBarRepository extends JpaRepository<VendorBarGraph , String>
{
    @Query(nativeQuery = true,value = "select id as tripId,transporterName as VendorNames,count(id) as count from Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 DAY )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) GROUP BY transporterName")
    List<VendorBarGraph> countVendorGraphInDay();
    @Query(nativeQuery = true,value = "select id as tripId,transporterName as VendorNames,count(id) as count from Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime)<= CURRENT_DATE()) GROUP BY transporterName")
    List<VendorBarGraph> countVendorInWeek();
    @Query(nativeQuery = true,value = "select id as tripId,transporterName as VendorNames,count(id) as count from Trip WHERE ((date_sub(CURRENT_DATE() , interval 1 MONTH )) <= DATE(createdTime) and DATE(createdTime)<= CURRENT_DATE()) GROUP BY transporterName")
    List<VendorBarGraph> countVendorInMonth();
}
