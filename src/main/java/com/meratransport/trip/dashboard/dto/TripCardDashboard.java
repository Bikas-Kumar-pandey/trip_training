package com.meratransport.trip.dashboard.dto;

import lombok.Data;


@Data
public class TripCardDashboard
{
    private float avgRunTime;
    private float avgStoppageTime;

    public void setAvgRunTime(float avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public void setAvgStoppageTime(float avgStoppageTime) {
        this.avgStoppageTime = avgStoppageTime;
    }

    public void setAvgLoadingTime(float avgLoadingTime) {
        this.avgLoadingTime = avgLoadingTime;
    }

    public void setAvgUnloadingTime(float avgUnloadingTime) {
        this.avgUnloadingTime = avgUnloadingTime;
    }

    public void setTripTrackingPercentage(float tripTrackingPercentage) {
        this.tripTrackingPercentage = tripTrackingPercentage;
    }

    public void setTripCompletion(float tripCompletion) {
        this.tripCompletion = tripCompletion;
    }

    private float avgLoadingTime;
    private float avgUnloadingTime;
    private float tripTrackingPercentage;
    private float tripCompletion;
}
