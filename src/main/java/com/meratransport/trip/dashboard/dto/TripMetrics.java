package com.meratransport.trip.dashboard.dto;

import com.meratransport.trip.dto.LocationFrequencyDto;
import com.meratransport.trip.entity.LocationFrequencyEntity;

import java.util.List;

public class TripMetrics
{

    public List<LocationFrequencyDto> getPendingTrips() {
        return pendingTrips;
    }

    public void setPendingTrips(List<LocationFrequencyDto> pendingTrips) {
        this.pendingTrips = pendingTrips;
    }

    public List<LocationFrequencyDto> getCompletedTrips() {
        return completedTrips;
    }

    public void setCompletedTrips(List<LocationFrequencyDto> completedTrips) {
        this.completedTrips = completedTrips;
    }

    public List<LocationFrequencyDto> getInTransitTrips() {
        return inTransitTrips;
    }

    public void setInTransitTrips(List<LocationFrequencyDto> inTransitTrips) {
        this.inTransitTrips = inTransitTrips;
    }

    private List<LocationFrequencyDto> pendingTrips;
    private List<LocationFrequencyDto> inTransitTrips;
    private List<LocationFrequencyDto> completedTrips;
}
