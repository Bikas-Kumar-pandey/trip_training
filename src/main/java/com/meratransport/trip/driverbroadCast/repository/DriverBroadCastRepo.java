package com.meratransport.trip.driverbroadCast.repository;

import com.meratransport.trip.driverbroadCast.entity.Keeboot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverBroadCastRepo extends JpaRepository<Keeboot, Integer> {
    @Query(nativeQuery = true,value ="SELECT * from KEEBOOT_DRIVER WHERE TENANT_ID =:id")
    public List<Keeboot> findByID(String id);
}