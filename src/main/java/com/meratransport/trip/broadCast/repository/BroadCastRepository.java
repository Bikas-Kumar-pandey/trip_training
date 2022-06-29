package com.meratransport.trip.broadCast.repository;

import com.meratransport.trip.broadCast.entity.BroadCastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BroadCastRepository extends JpaRepository<BroadCastEntity,Integer> {
    public List<BroadCastEntity> findByEntityTypeAndHandleSourceAndHandleType(String entityType,String handleSource,String handleType);

}
