package com.meratransport.trip.driverbroadCast.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name="KEEBOOT_DRIVER")
public class Keeboot {
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
    @Column(name="USER_ID")
    private String userId;
    @Column(name="TENANT_ID")
    private String tenantId;
    @Column(name="NAME")
    private String name;

    @Column(name="SHORT_NAME")
    private String shortName;
    @Column(name="MOBILE_NUMBER")
    private String mobileNumber;
    @Column(name="TITLE")
    private String title;
    @Column(name="OWNERSHIP_TYPE")
    private String ownershipType;
    @Column(name="LANGUAGE")
    private String language;
    @Column(name="EMAIL_ID")
    private String emailId;
    @Column(name="ACTIVE")
    private int active;
    @Column(name="DELETED")
    private int deleted;
    @Column(name="AADHAR_VERIFIED")
    private int aadhrVerified;
    @Column(name="PROFILE_PICTURE")
    private String profilePiicture;
    @Column(name="EMERGENCY_CONTACT_NAME")
    private String contactName;
    @Column(name="EMERGENCY_CONTACT_NUMBER")
    private String EmgContactNumber;
    @Column(name="EMERGENCY_CONTACT_RELATION")
    private String emmgContactRelation;
    @Column(name="JOINING_DATE")
    private Date joiningDate;
    @Column(name="TERMINATION_DATE")
    private Date terminationDate;
    @Column(name="DATE_OF_BIRTH")
    private Date DOB;
    @Column(name="BLOOD_GROUP")
    private String bloodGroup;
    @Column(name="NOTES")
    private String notes;
    @Column(name="LICENSE_NUMBER")
    private String licenseNumber;
    @Column(name="LICENSE_TYPE")
    private String licenseType;
    @Column(name="LICENSE_IMAGE")
    private String licenseImg;
    @Column(name="LICENSE_EXPIRY_DATE")
    private Date licenseExpDate;

    @Column(name="LICENSE_ISSUED_BY_STATE")
    private String licenseIssuedByStat;
    @Column(name="MONTHLY_SALARY")
    private String monthlySalary;
    @Column(name="DAILY_BATA")
    private String dailyBata;
    @Column(name="COMMISSION")
    private String commission;
    @Column(name="BADGE_NUMBER")
    private String badgeNumber;
    @Column(name="PHONE")
    private String phone;
    @Column(name="TEANANT_ID")
    private String teanantId;
    @Column(name="driver")
    private String driver;
    @Column(name="app_name")
    private String appName;
    @Column(name="created_ts")
    private LocalDateTime createdTs;
    @Column(name="kfc_id")
    private String kfcId;
    @Column(name="updated_ts")
    private LocalDateTime updatedTs;











}