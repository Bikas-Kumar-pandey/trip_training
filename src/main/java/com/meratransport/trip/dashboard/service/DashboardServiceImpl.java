package com.meratransport.trip.dashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.dashboard.dto.DashboardTripReport;
import com.meratransport.trip.dashboard.dto.TripCardDashboard;
import com.meratransport.trip.dto.LocationFrequencyDto;
import com.meratransport.trip.dto.VendorBarGraphDto;
import com.meratransport.trip.entity.LocationFrequencyEntity;
import com.meratransport.trip.dashboard.dto.TripMetrics;
import com.meratransport.trip.dto.LocationRequest;
import com.meratransport.trip.entity.VendorBarGraph;
import com.meratransport.trip.profile.repository.ShipperRepository;
import com.meratransport.trip.profile.repository.TenantRepository;
import com.meratransport.trip.repository.DashboardTripPieRepository;
import com.meratransport.trip.repository.DashboardVendorTripBarRepository;
import com.meratransport.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.meratransport.trip.constant.ApplicationConstant.EMPLOYEE_ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DashboardTripPieRepository dashboardTripPieRepository;

    @Autowired
    private DashboardVendorTripBarRepository dashboardVendorTripBarRepository;

    @Value("${base.location.uri}")
    private String locationUri;

    DashboardTripReport dashboardTripReport = new DashboardTripReport();

    TripMetrics tripMetrics = new TripMetrics();

    TripCardDashboard tripCardDashboard = new TripCardDashboard();

    @Override
    public DashboardTripReport getTotalTripReport(String timeInterval, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
        isValidRequest(headers);
        switch (timeInterval)
        {
            case "DAY":
                dashboardTripReport.setCountTotalTrips(tripRepository.countTotalTripsInDay());
                dashboardTripReport.setCountPendingTrips(tripRepository.countTotalPendingTripsInDay());
                dashboardTripReport.setCountInTransitTrips(tripRepository.countTotalInTransitTripsInDay());
                dashboardTripReport.setCountTripCreated(tripRepository.countTotalCreatedTripsInDay());
                dashboardTripReport.setCountTripCompleted(tripRepository.countTotalCompletedTripsInDay());
                break;


            case "WEEK":
                dashboardTripReport.setCountTotalTrips(tripRepository.countTotalTripsInWeek());
                dashboardTripReport.setCountPendingTrips(tripRepository.countTotalPendingTripsInWeek());
                dashboardTripReport.setCountInTransitTrips(tripRepository.countTotalInTransitTripsInWeek());
                dashboardTripReport.setCountTripCreated(tripRepository.countTotalCreatedTripsInWeek());
                dashboardTripReport.setCountTripCompleted(tripRepository.countTotalCompletedTripsInWeek());
                break;

            case "MONTH":
                dashboardTripReport.setCountTotalTrips(tripRepository.countTotalTripsInMonth());
                dashboardTripReport.setCountPendingTrips(tripRepository.countTotalPendingTripsInMonth());
                dashboardTripReport.setCountInTransitTrips(tripRepository.countTotalInTransitTripsInMonth());
                dashboardTripReport.setCountTripCreated(tripRepository.countTotalCreatedTripsInMonth());
                dashboardTripReport.setCountTripCompleted(tripRepository.countTotalCompletedTripsInMonth());
                break;
        }
        return dashboardTripReport;
    }

    @Override
    public List<LocationFrequencyDto> getLocationInfo(List<LocationFrequencyEntity> locationFrequency, Map<String, String> headers, HttpHeaders httpHeaders) throws JsonProcessingException
    {
        List<LocationFrequencyDto> locationtemp =new ArrayList<LocationFrequencyDto>() ;
        for (LocationFrequencyEntity loc :locationFrequency)
        {   LocationFrequencyDto object = new LocationFrequencyDto();

            HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            JsonObject jsonObject = null;
            ResponseEntity<String> responseLocation = null;
            LocationRequest locationInfo = null;
            responseLocation = restTemplate.exchange(locationUri
                            + loc.getLocationId(),
                    HttpMethod.GET, requestEntity, String.class);

            jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
            if (jsonObject.has("response")) {
                jsonObject = (JsonObject) jsonObject.get("response");

                locationInfo = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
                        LocationRequest.class);
                object.setLocation(locationInfo.getAddress());
                object.setFreq(loc.getFreq());

            }
            locationtemp.add(object);
        }
        return locationtemp;
    }

    @Override
    public TripMetrics getTripLocationPieDetails(String timeInterval , Map<String , String> headers, HttpHeaders httpHeaders) throws Exception
    {
        isValidRequest(headers);

        switch (timeInterval)
        {
            case "DAY":
               tripMetrics.setPendingTrips(getLocationInfo(dashboardTripPieRepository.listTripInPendingInDay(),headers,httpHeaders));
               tripMetrics.setInTransitTrips(getLocationInfo(dashboardTripPieRepository.listTripInTransitInDay(),headers,httpHeaders));
               tripMetrics.setCompletedTrips(getLocationInfo(dashboardTripPieRepository.listTripCompletedInDay(),headers,httpHeaders));
               break;

            case "WEEK":
                tripMetrics.setPendingTrips(getLocationInfo(dashboardTripPieRepository.listTripInPendingInWeek(),headers,httpHeaders));
                tripMetrics.setInTransitTrips(getLocationInfo(dashboardTripPieRepository.listTripInTransitInWeek(),headers,httpHeaders));
                tripMetrics.setCompletedTrips(getLocationInfo(dashboardTripPieRepository.listTripCompletedInWeek(),headers,httpHeaders));
                break;

            case "MONTH":
                tripMetrics.setPendingTrips(getLocationInfo(dashboardTripPieRepository.listTripInPendingInMonth(),headers,httpHeaders));
                tripMetrics.setInTransitTrips(getLocationInfo(dashboardTripPieRepository.listTripInTransitInMonth(),headers,httpHeaders));
                tripMetrics.setCompletedTrips(getLocationInfo(dashboardTripPieRepository.listTripCompletedInMonth(),headers,httpHeaders));
                break;
        }
        return tripMetrics;
    }

    @Override
    public List<VendorBarGraphDto> getVendorWiseBarGraph(String timeInterval, Map<String, String> headers, HttpHeaders prepareHttpHeader) throws Exception {
        List<VendorBarGraph> objects =null;

        List<VendorBarGraphDto> dtos = new ArrayList<>();

        switch (timeInterval) {
            case "DAY":
                objects = dashboardVendorTripBarRepository.countVendorGraphInDay();//CORRECT
                break;

            case "WEEK":
                objects = dashboardVendorTripBarRepository.countVendorInWeek();
                break;

            case "MONTH":
                objects = dashboardVendorTripBarRepository.countVendorInMonth();
                break;
        }
        for (VendorBarGraph details :objects){
            VendorBarGraphDto dto = new VendorBarGraphDto();
            dto.setVendorName(details.getVendorNames());
            dto.setCount(details.getCount());
            dtos.add(dto);
        }
        return dtos;
    }

    public TripCardDashboard getTripCardDashboardReport(String timeInterval, Map<String, String> headers, HttpHeaders prepareHttpHeader) throws Exception
    {
        isValidRequest(headers);
        float countTotalCompletedTrips;
        float countTotalTrips;
        switch (timeInterval)
        {

            case "DAY":
                countTotalCompletedTrips = tripRepository.countTotalCompletedTripsInDay();
                countTotalTrips = tripRepository.countTotalTripsInDay();
                if (countTotalTrips > 0)
                {
                    tripCardDashboard.setTripCompletion((countTotalCompletedTrips/countTotalTrips)*100);
                }
                else
                {
                    tripCardDashboard.setTripCompletion(0);
                }
                break;
            case "WEEK":
                countTotalTrips = tripRepository.countTotalTripsInWeek();
                countTotalCompletedTrips = tripRepository.countTotalCompletedTripsInWeek();
                if (countTotalTrips > 0)
                {
                    tripCardDashboard.setTripCompletion((countTotalCompletedTrips/countTotalTrips)*100);
                }
                else
                {
                    tripCardDashboard.setTripCompletion(0);
                }
                break;
            case "MONTH":
                countTotalTrips = tripRepository.countTotalTripsInMonth();
                countTotalCompletedTrips = tripRepository.countTotalCompletedTripsInMonth();
                if (countTotalTrips > 0)
                {
                    tripCardDashboard.setTripCompletion((countTotalCompletedTrips/countTotalTrips)*100);
                }
                else
                {
                    tripCardDashboard.setTripCompletion(0);
                }
                break;
        }
        return tripCardDashboard;
    }

    private void isValidRequest(Map<String, String> headers) throws Exception {
        if (!shipperRepository.existsById(headers.get(EMPLOYEE_ID))) {
            throw new Exception(ApplicationConstant.NO_SHIPPER_EXISTS);
        }
        if (!tenantRepository.existsById(headers.get(TENANT_ID))) {
            throw new Exception(ApplicationConstant.NO_TENANT_EXISTS);
        }
    }

}
