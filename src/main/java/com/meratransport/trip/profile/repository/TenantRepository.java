package com.meratransport.trip.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.profile.entity.TenantInfo;

public interface TenantRepository extends JpaRepository<TenantInfo, String> {

}
