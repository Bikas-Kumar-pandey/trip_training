package com.meratransport.trip.dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meratransport.trip.dashboard.dto.DashboardTripReport;
import com.meratransport.trip.dashboard.dto.TripCardDashboard;
import com.meratransport.trip.dto.LocationFrequencyDto;
import com.meratransport.trip.dto.VendorBarGraphDto;
import com.meratransport.trip.entity.LocationFrequencyEntity;
import com.meratransport.trip.dashboard.dto.TripMetrics;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public interface DashboardService
{
    DashboardTripReport getTotalTripReport(String timeInterval , Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

    List<VendorBarGraphDto> getVendorWiseBarGraph(String timeInterval, Map<String, String> headers, HttpHeaders prepareHttpHeader) throws Exception;

    List<LocationFrequencyDto> getLocationInfo(List<LocationFrequencyEntity> locationFrequency, Map<String, String> headers, HttpHeaders httpHeaders) throws JsonProcessingException;

    TripMetrics getTripLocationPieDetails(String timeInterval , Map<String , String> headers, HttpHeaders httpHeaders) throws Exception;

    TripCardDashboard getTripCardDashboardReport(String timeInterval, Map<String, String> headers, HttpHeaders prepareHttpHeader) throws Exception;
}