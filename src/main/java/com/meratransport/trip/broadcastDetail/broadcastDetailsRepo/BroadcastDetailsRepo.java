package com.meratransport.trip.broadcastDetail.broadcastDetailsRepo;

import com.meratransport.trip.broadcastDetail.broadcastDetailEntity.BroadcastDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BroadcastDetailsRepo extends JpaRepository<BroadcastDetailsEntity, String> {

//    @Query(nativeQuery = true,value = "select VENDOR_NAME As vendorName, FINAL_AMOUNT As finalRate,TARGET_RATE As targetRate from  SHIPPER_INDENT ") //change
//    public List<BroadCastVendorResponse> getVendorResponse();

//    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME) from  SHIPPER_INDENT")
//    boolean existsById(String id);

//    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME),LOAD_VENDOR_USER_ID  from  SHIPPER_INDENT")
//     public List<String> findByVendorNames();

//    @Query(nativeQuery = true,value = "select com.meratransport.trip.broadcastDetail.broadcastDetailEntity(VENDOR_NAME,LOAD_VENDOR_USER_ID)  from  SHIPPER_INDENT")
//    List<BroadcastDetailsEntity> findByVendorNames();

//    @Query(nativeQuery = true,value = "select com.meratransport.trip.broadcastDetail.broadcastDetailEntity(VENDOR_NAME,LOAD_VENDOR_USER_ID)  from  SHIPPER_INDENT")
//    List<BroadcastDetailsEntity> findByVendorNames();

    List<BroadcastDetailsEntity> findAllByTenantId(String tenant);

    boolean existsByTenantId(String tenantID) ;

    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME) LOAD_VENDOR_USER_ID  from  SHIPPER_INDENT")
    public List<String> findByVendorNamesId();

//    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME) from  SHIPPER_INDENT WHERE CONTRACT_TYPE ='Contract' ")// WHERE CONTRACT_TYPE ='Contract'
//    SELECT COUNT(*) FROM Trip WHERE (((date_sub(CURRENT_DATE() , interval 1 WEEK )) <= DATE(createdTime) and DATE(createdTime) <= CURRENT_DATE()) and (currentStatus IN ('TRIP_CREATED','START_INSTALLATION','SCAN_TAGS','ADD_MATERIAL') AND (tenantId = :tenantId)))
    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME),ID from  SHIPPER_INDENT WHERE (CONTRACT_TYPE ='Contract') AND (TENANT_ID = :tenant)")
    public List<String> contractVendorDetails(String tenant);

    @Query(nativeQuery = true,value = "select DISTINCT(VENDOR_NAME) from  SHIPPER_INDENT")
    public  List<String>  broadcastbiddinggConfirm();

//    @Query(nativeQuery = true,value = "select INDENT_ID from  SHIPPER_INDENT")
//    boolean existsByIndentId(String s);
//
//    @Query(nativeQuery = true,value = "select TENANT_ID from  SHIPPER_INDENT=:id")
//    boolean existsByTeenantId(String s);

    boolean existsByIndentId(String indentId);

    @Query(nativeQuery = true,value = "Select * from SHIPPER_INDENT where CONTRACT_TYPE='Contract' and TENANT_ID=:tenantId ")
    List<BroadcastDetailsEntity> getAllShipperWithContractType(String tenantId);
}

