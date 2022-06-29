
package com.meratransport.trip.report.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meratransport.trip.report.dto.ReportDownloadRequestDto;
import com.meratransport.trip.report.dto.ReportListRequestDto;
import com.meratransport.trip.report.dto.RowValues;
import com.meratransport.trip.report.entity.model.Format;
import com.meratransport.trip.report.entity.model.ReportType;
import com.meratransport.trip.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

import static com.meratransport.trip.constant.ApplicationConstant.*;
import static com.meratransport.trip.constant.ApplicationConstant.APP_TYPE;

@Controller
public class ReportController {

    public static final MediaType XLS_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    @Autowired
    private ReportService reportService;


    @GetMapping("/listReport")
    public @ResponseBody RowValues listReportData(ReportListRequestDto requestDto,@RequestHeader(TENANT_ID) String tenantId,
                                                  @RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,@RequestHeader(value="Authorization",required = false) String authorization,
                                                  @RequestHeader(APP_TYPE) String appType) throws JsonProcessingException {
        return reportService.listReport(requestDto,prepareHttpHeader(tenantId, userId, employeeId, appType,authorization),tenantId);
    }

    @GetMapping("/downloadReport")
    public ResponseEntity<byte[]> downloadReportData(ReportDownloadRequestDto requestDto,@RequestHeader(TENANT_ID) String tenantId,
                                                     @RequestHeader(USER_ID) String userId, @RequestHeader(EMPLOYEE_ID) String employeeId,@RequestHeader(value="Authorization",required = false) String authorization,
                                                     @RequestHeader(APP_TYPE) String appType) {
        var byteValue = reportService.downloadReport(requestDto,prepareHttpHeader(tenantId, userId, employeeId, appType,authorization),tenantId);
        var headers = new HttpHeaders();
        var format = requestDto.getDownloadFormat();

        setContentTypeHeader(format, headers);
        setFileNameHeader(format, requestDto.getReportType(), headers);

        return ResponseEntity.ok().headers(headers).body(byteValue);
    }

    private void setFileNameHeader(Format format, ReportType reportType, HttpHeaders headers) {
        var localDateTime = LocalDateTime.now().toString();
        var reportName = reportType.name() + "-" + localDateTime;
        if (format == Format.PDF) {
            headers.set(HttpHeaders.CONTENT_DISPOSITION, String.format("inline;filename=%s.pdf", reportName));
        } else if (format == Format.XLS) {
            headers.set(HttpHeaders.CONTENT_DISPOSITION, String.format("inline;filename=%s.xls", reportName));
        }
    }

    private void setContentTypeHeader(Format format, HttpHeaders headers) {
        if (format == Format.PDF) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else if (format == Format.XLS) {
            headers.setContentType(XLS_MEDIA_TYPE);
        }

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

