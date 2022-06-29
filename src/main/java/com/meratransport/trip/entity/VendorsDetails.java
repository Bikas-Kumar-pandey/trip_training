package com.meratransport.trip.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class
VendorsDetails {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String qid;



    private String name;
    private String vendoridd;


    @ManyToOne
    private BroadcastCompleteDetails broadcastCompleteDetails;


}