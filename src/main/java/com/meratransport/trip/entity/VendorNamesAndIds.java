package com.meratransport.trip.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
public class VendorNamesAndIds {

    @Id
    private String vendorId;
    private String vendorName;
}
