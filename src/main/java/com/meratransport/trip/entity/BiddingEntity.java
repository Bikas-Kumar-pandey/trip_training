//package com.meratransport.trip.entity;
//
//import lombok.Data;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//
//
//@Entity
//@Data
//public class BiddingEntity {
//    @Id
//    @Column(name = "BIDDING_ID")
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    private String biddingId;
//
//    private String vendorName;
//    private String TargetRate;
//    private String counterOffer;
//    private String pickupLocation;
//    private String dropLocation;
//    private String goodsType;
//    private String vehicleType;
//
//    @ManyToOne
//    private BroadcastCompleteDetails broadcastCompleteDetails;
//}
