package com.meratransport.trip.dto;

import lombok.Data;

@Data
public class LocationFrequencyDto
{
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getFreq() {
        return freq;
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    private String location;
    private Integer freq;
}
