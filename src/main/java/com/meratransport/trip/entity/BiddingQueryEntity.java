package com.meratransport.trip.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class BiddingQueryEntity { //Used only to fetch from join column
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String targetRate;
    private String accept;
    private String vehicle;
    private String finalAmount;
    private String counterOffer;
    private String rank;
    private String indentId;
    private String type;
    private String tenantId;
    private String name;
    private String vendoridd;

}
