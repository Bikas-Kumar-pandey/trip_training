package com.meratransport.trip.broadcastDetail.broadcastDetailEntity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data

public class VNameAndId {
    @Id
    @Column(name="ID")
    private String id;

    @Column(name="VENDOR_NAME")
    private String vendorName;
}
