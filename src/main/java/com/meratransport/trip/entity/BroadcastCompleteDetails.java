package com.meratransport.trip.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicUpdate
public class BroadcastCompleteDetails {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String targetRate;
    private String accept;
    private String vehicle;
    private String finalAmount;
    private String rank;
    private String indentId;
    private String type;
    private String tenantId;
//    private List<BroadcastBiddingConfirmation> broadcastBiddingConfirmations ;


    private String vendorName;
//    private String targetRate;
    private String counterOffer;
    private String pickupLocation;
    private String dropLocation;
    private String goodsType;
    private String vehicleType;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDORS_ID")
    private List<VendorsDetails> vendorsDetails;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "Bidding_FK_of_BroadcastCompleteDetails")
//private List<BiddingEntity> biddingEntity;



}