package com.meratransport.trip.repository;

import com.meratransport.trip.entity.VendorsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorsDetailsRepo extends JpaRepository<VendorsDetails,String> {

    @Query(nativeQuery = true,value = " SELECT * FROM BroadcastCompleteDetails left JOIN VendorsDetails on  BroadcastCompleteDetails.ID  = VendorsDetails.VENDORS_ID WHERE VendorsDetails.vendoridd =:vendoridd ")
    VendorsDetails findByVendoridd(String vendoridd);
}
