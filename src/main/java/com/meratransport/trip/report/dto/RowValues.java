package com.meratransport.trip.report.dto;

import com.meratransport.trip.report.entity.MasterReport;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RowValues {
    List<Object> rowValues;
    Map<String,String> columnValues;
}
