package com.meratransport.trip.repository;

import com.meratransport.trip.entity.BiddingQueryEntity;
import com.meratransport.trip.entity.VendorsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BiddingQueryRepo extends JpaRepository<BiddingQueryEntity,String > { //Used only to fetch from join column

    @Query(nativeQuery = true,value = " SELECT * FROM BroadcastCompleteDetails left JOIN VendorsDetails on  BroadcastCompleteDetails.ID  = VendorsDetails.VENDORS_ID WHERE VendorsDetails.vendoridd =:vendoridd ")
    BiddingQueryEntity findByVendoridd(String vendoridd);

    @Query(nativeQuery = true,value = " SELECT * FROM BroadcastCompleteDetails left JOIN VendorsDetails on  BroadcastCompleteDetails.ID  = VendorsDetails.VENDORS_ID WHERE BroadcastCompleteDetails.tenantId=:tenantId ")
    List<BiddingQueryEntity> findByTenantId(String tenantId);

    BiddingQueryEntity existsByVendoridd(String vendorIdd);
}
