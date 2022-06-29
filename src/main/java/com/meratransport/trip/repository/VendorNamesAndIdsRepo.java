package com.meratransport.trip.repository;

import com.meratransport.trip.broadcastDetail.broadcastDetailEntity.BroadcastDetailsEntity;
import com.meratransport.trip.entity.VendorNamesAndIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorNamesAndIdsRepo extends JpaRepository<VendorNamesAndIds,String> {


    @Query(nativeQuery = true, value = "select ID as vendorId,VENDOR_NAME as vendorName from  SHIPPER_INDENT")
   public List<VendorNamesAndIds> getVendorNames();
}
