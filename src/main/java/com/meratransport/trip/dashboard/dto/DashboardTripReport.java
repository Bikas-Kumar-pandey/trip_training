package com.meratransport.trip.dashboard.dto;

import lombok.Data;

@Data
public class DashboardTripReport
{
    private int countTotalTrips;
    private int countPendingTrips;
    private int countInTransitTrips;
    private int countTripCreated;
    private int countTripCompleted;
}
