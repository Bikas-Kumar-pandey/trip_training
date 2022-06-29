package com.meratransport.trip.dashboard.controller;
import com.meratransport.trip.dashboard.dto.DashboardTripReport;
import com.meratransport.trip.dashboard.dto.TripCardDashboard;
import com.meratransport.trip.dashboard.dto.TripMetrics;
import com.meratransport.trip.dashboard.service.DashboardService;
import com.meratransport.trip.dto.VendorBarGraphDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meratransport.trip.constant.ApplicationConstant.*;
import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;

@RestController
public class DashboardController
{
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/getTotalTripReport/{timeInterval}")
    public DashboardTripReport getTotalTripReport(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType, @RequestHeader(value="Authorization",required = false) String authorization, @PathVariable String timeInterval) throws Exception {
        return dashboardService.getTotalTripReport(timeInterval , prepareHeaderMap(tenantId, userId, employeeId, appType),prepareHttpHeader(tenantId, userId, employeeId, appType,authorization));
    }

    @GetMapping("/getTripPieChartReport/{timeInterval}")
    public TripMetrics getTripLocationPieDetails(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType, @RequestHeader(value="Authorization",required = false) String authorization, @PathVariable String timeInterval) throws Exception {
        return dashboardService.getTripLocationPieDetails(timeInterval , prepareHeaderMap(tenantId, userId, employeeId, appType),prepareHttpHeader(tenantId, userId, employeeId, appType,authorization));
    }

    @GetMapping("/getVendorWiseBarGraph/{timeInterval}")
    public List<VendorBarGraphDto> getVendorWiseBarGraph(@RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value = USER_ID,required = false) String userId, @RequestHeader(value = EMPLOYEE_ID,required = false) String employeeId, @RequestHeader(value = APP_TYPE,required = false) String appType,
                                                         @RequestHeader(value="Authorization",required = false) String authorization, @PathVariable String timeInterval) throws Exception{
        return  dashboardService.getVendorWiseBarGraph(timeInterval , prepareHeaderMap(tenantId, userId, employeeId, appType),prepareHttpHeader(tenantId, userId, employeeId, appType,authorization));
    }

    @GetMapping("/getTripCardDashboardReport/{timeInterval}")
    public TripCardDashboard getTripCardDashboardReport(@RequestHeader(TENANT_ID) String tenantId, @RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId, @RequestHeader(APP_TYPE) String appType, @RequestHeader(value="Authorization",required = false) String authorization, @PathVariable String timeInterval) throws Exception {
        return dashboardService.getTripCardDashboardReport(timeInterval , prepareHeaderMap(tenantId, userId, employeeId, appType),prepareHttpHeader(tenantId, userId, employeeId, appType,authorization));
    }


    private Map<String, String> prepareHeaderMap(String tenantId, String userId, String employeeId, String appType) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(TENANT_ID, tenantId);
        headers.put(EMPLOYEE_ID, employeeId);
        headers.put(USER_ID, userId);
        headers.put(APP_TYPE, appType);
        return headers;

    }

    private HttpHeaders prepareHttpHeader(String tenantId, String userId, String employeeId, String appType,
                                          String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(TENANT_ID, tenantId);
        headers.set(EMPLOYEE_ID, employeeId);
        headers.set(USER_ID, userId);
        headers.set(APP_TYPE, appType);
        headers.set("Authorization", authorization);
        return headers;
    }
}
