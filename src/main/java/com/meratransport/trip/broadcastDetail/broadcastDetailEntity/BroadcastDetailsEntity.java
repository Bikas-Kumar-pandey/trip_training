package com.meratransport.trip.broadcastDetail.broadcastDetailEntity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data

@Table(name="SHIPPER_INDENT")
public class BroadcastDetailsEntity {
    @Id
    @Column(name="ID")
    private String id;
    @Column(name="CREATED_TIME")
    private LocalDateTime createdTime;
    @Column(name="CREATED_BY")
    private String createdBy;
    @Column(name="MODIFIED_TIME")
    private LocalDateTime modifiedTime;
    @Column(name="MODIFIED_BY")
    private String modifiedBy;
    @Column(name="APP_TYPE")
    private String appType;
    @Column(name="VERSION")
    private int version;
    @Column(name="TENANT_ID")
    private String tenantId;
    @Column(name="STATE_ID")
    private String stateId;
    @Column(name="FLOW_ID")
    private String flowId;
    @Column(name="STATE_ENTRY_TIME")
    private LocalDateTime stateEntryTime;
    @Column(name="SLA_TENDING_LATE")
    private int slaTendingLate;
    @Column(name="SLA_LATE")
    private int slaLate;
    @Column(name="CUSTOMER_ID")
    private String customerId;
    @Column(name="CUSTOMER_CONTACT_ID")
    private String customerContactId;
    @Column(name="CUSTOMER_CONTACT_NAME")
    private String customerContactName;
    @Column(name="CUSTOMER_CONTACT_NUMBER")
    private String customerContactNumber;
    @Column(name="PLAN_ID")
    private String plainId;
    @Column(name="INDENT_DISP_ID")
    private String indentDispId;
    @Column(name="BOOKING_BRANCH_ID")
    private String bookingBranchId;
    @Column(name="ORDER_TYPE")
    private String orderType;
    @Column(name="CONTRACT_TYPE")
    private String contractType;
    @Column(name="INDENT_REQUESTOR")
    private String indentRequest;
    @Column(name="CONTRACT_NO")
    private String contractNo;
    @Column(name="GOODS_TYPE")
    private String goodsType;
    @Column(name="VEHICLE_TYPE")
    private String vehicleType;
    @Column(name="NO_OF_VEHICLES_REQUIRED")
    private int NoOfVehicleReq;
    @Column(name="INDENT_REF_NUMBER")
    private String indentRefNum;
    @Column(name="INDENT_TIME")
    private LocalDateTime indentTime;
    @Column(name="CUSTOM_FIELD_1")
    private String customFiel1;
    @Column(name="CUSTOM_FIELD_2")
    private String customField2;
    @Column(name="CUSTOM_FIELD_3")
    private String customField3;
    @Column(name="CUSTOM_FIELD_4")
    private String customField4;
    @Column(name="CUSTOM_FIELD_5")
    private String customField5;
    @Column(name="VEHICLE_REQUIRED_DATE")
    private LocalDateTime vehicleReqDate;
    @Column(name="DRIVER_LICENSE_NUMBER")
    private String driverLicenseNumber;
    @Column(name="DRIVER_NAME")
    private String driverName;
    @Column(name="DRIVER_TELEPHONE_NUMBER")
    private String driverTelephoneNumber;
    @Column(name="VEHICLE_NUMBER")
    private String vehicleNumber;
    @Column(name="VENDOR_NAME")
    private String vendorName;
    @Column(name="VENDOR_TELEPHONE_NUMBER")
    private String vendorTelephoneNumber;
    @Column(name="LOAD_ID")
    private String loadId;
    @Column(name="LOAD_VENDOR_USER_ID")
    private String loadVendorUserId;
    @Column(name="FINAL_AMOUNT")
    private String finalAmount;
    @Column(name="TARGET_RATE")
    private String targetRate;
    @Column(name="INDENT_ID")
    private String indentId;
//    @Column(name="f")
//    private double f;


}