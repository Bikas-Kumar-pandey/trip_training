package com.meratransport.trip.repository;

import com.meratransport.trip.entity.BroadcastCompleteDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BroadcastCompleteDetailsrepo extends JpaRepository<BroadcastCompleteDetails, String> {

//    List<BroadcastCompleteDetails> findByVendoridd(vendorId String vendorId);

}