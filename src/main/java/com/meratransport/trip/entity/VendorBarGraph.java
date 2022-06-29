package com.meratransport.trip.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class VendorBarGraph
{
    @Id
    private String tripId;
    private String VendorNames;
    private String count;
}
