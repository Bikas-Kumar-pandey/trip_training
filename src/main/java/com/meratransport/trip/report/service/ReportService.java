package com.meratransport.trip.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meratransport.trip.report.dto.ReportDownloadRequestDto;
import com.meratransport.trip.report.dto.ReportListRequestDto;
import com.meratransport.trip.report.dto.RowValues;
import org.springframework.http.HttpHeaders;


public interface ReportService {
     RowValues listReport(ReportListRequestDto listReq, HttpHeaders httpHeaders,String tenant) throws JsonProcessingException;
     byte[] downloadReport(ReportDownloadRequestDto downloadReq, HttpHeaders httpHeaders,String tenant);
}

