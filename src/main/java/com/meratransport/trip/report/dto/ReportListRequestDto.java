package com.meratransport.trip.report.dto;

import com.meratransport.trip.report.entity.model.ReportType;
import com.meratransport.trip.report.entity.model.SortBy;
import lombok.Data;

import java.util.Date;

@Data
public class ReportListRequestDto {
    private Date filterStartDate=new Date();
    private Date filterEndDate=new Date();
    private SortBy sort;
    private ReportType reportType;
    private int limit;

}
