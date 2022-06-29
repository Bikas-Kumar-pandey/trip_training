package com.meratransport.trip.report.dto;

import com.meratransport.trip.report.entity.model.Format;
import com.meratransport.trip.report.entity.model.ReportType;
import com.meratransport.trip.report.entity.model.SortBy;
import lombok.Data;

import java.util.Date;

@Data
public class ReportDownloadRequestDto {
    private Date filterStartDate;
    private Date filterEndDate;
    private SortBy sort;
    private ReportType reportType;
    private Format downloadFormat;
    private int limit;
}

