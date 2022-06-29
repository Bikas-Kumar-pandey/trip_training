package com.meratransport.trip.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meratransport.trip.dto.LocationRequest;
import com.meratransport.trip.entity.MasterReportNew;
import com.meratransport.trip.picklist.repository.PickListRepository;
import com.meratransport.trip.profile.repository.ProfileDriverMasterRepository;
import com.meratransport.trip.profile.repository.ShipperRepository;
import com.meratransport.trip.profile.repository.VehicleMasterRepository;
import com.meratransport.trip.report.dto.*;
import com.meratransport.trip.report.entity.*;
import com.meratransport.trip.report.entity.model.Format;
import com.meratransport.trip.report.entity.model.ReportType;
import com.meratransport.trip.report.entity.model.SortBy;
import com.meratransport.trip.report.exception.ReportException;
import com.meratransport.trip.report.repository.*;
import com.meratransport.trip.repository.MasterReportNewRepository;
import com.meratransport.trip.repository.TrackingReportNewRepository;
import com.meratransport.trip.repository.TripReportNewRepository;
import com.meratransport.trip.repository.TripRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String FILE_PATH = "src/main/resources/properties/jrxml/";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private IndentReportRepository indentReportRepository;

    @Autowired
    private TrackingReportRepository trackingReportRepository;

    @Autowired
    private TripReportRepository tripReportRepository;

    @Autowired
    private MasterReportNewRepository masterReportNewRepository;

    @Autowired
    private TripReportNewRepository tripReportNewRepository;

    @Autowired
    private TrackingReportNewRepository trackingReportNewRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleMasterRepository vehicleMasterRepository;

    @Autowired
    private PickListRepository pickListRepository;

    @Autowired
    private ShipperRepository shipperRepository;

    @Autowired
    private ProfileDriverMasterRepository driverMasterRepository;

    @Value("${base.location.uri}")
    private String locationUri;


    @Override
    public RowValues listReport(ReportListRequestDto reportReq, HttpHeaders httpHeaders, String tenant) throws JsonProcessingException {
        if (reportReq == null) {
            throw new ReportException("Query details required");
        }
        ReportType type = reportReq.getReportType();
        if (type == null) {
            throw new ReportException("Report type is required");
        }

        var startDate = reportReq.getFilterStartDate() != null ? reportReq.getFilterStartDate() : atStartOfDay(new Date());
        var endDate = reportReq.getFilterEndDate() != null ? reportReq.getFilterEndDate() : atEndOfDay(new Date());
        var sort = reportReq.getSort().toString() != null ? reportReq.getSort().name() : SortBy.ASC.name();
        var limit = reportReq.getLimit() != 0 ? reportReq.getLimit() : 500;
        var rowValues = new RowValues();
        Map<String, String> columnValues = new LinkedHashMap<>();
        switch (type) {
            case MASTERREPORT:
                rowValues = masterReport(startDate, endDate, sort, limit, httpHeaders, tenant);
                setColumnValuesMaster(columnValues);
                break;

            case INDENTREPORT:
                rowValues = indentReport(startDate, endDate, sort, limit);
                setColumnValuesIndent(columnValues);
                break;

            case TRACKINGREPORT:
                rowValues = trackingReport(startDate, endDate, sort, limit, httpHeaders, tenant);
                setColumnValuesTracking(columnValues);
                break;

            case TRIPREPORT:
                rowValues = tripReport(startDate, endDate, sort, limit, httpHeaders, tenant);
                setColumnValuesTrip(columnValues);
                break;

        }
        rowValues.setColumnValues(columnValues);

        return rowValues;
    }



    //
    private RowValues masterReport(Date startDate, Date endDate, String sort, int limit, HttpHeaders httpHeaders, String tenant) throws JsonProcessingException {
        var allMasterReport = reportRepository.getAllMasterReport(limit);
        var allMasterReportNew = masterReportNewRepository.getAllMasterReportNew();
        //var allTrip = tripRepository.findAllByTenantId(tenant);
        var masterReportNewList = allMasterReportNew_1(allMasterReportNew, httpHeaders);
        var reportResponses = allMasterReportToMasterReportResponseDto(allMasterReport, masterReportNewList);
        RowValues rowValues = new RowValues();
        rowValues.setRowValues(Collections.singletonList(reportResponses));
        return rowValues;
    }



    private RowValues indentReport(Date startDate, Date endDate, String sort, int limit) {
        var indentReports = indentReportRepository.getAllIndent(limit);
        RowValues rowValues = new RowValues();
        rowValues.setRowValues(Collections.singletonList(indentReports));
        return rowValues;
    }

    private RowValues trackingReport(Date startDate, Date endDate, String sort, int limit, HttpHeaders httpHeaders, String tenant) throws JsonProcessingException {
        var trackingReports = trackingReportRepository.getAllTrackingReport(limit);
//        var allTrackingReportNew = trackingReportNewRepository.getAllTrackingReportNew();
        var allTrip = tripRepository.findAllByTenantId(tenant);
        var allTrackingReportNew = masterReportNewRepository.getAllMasterReportNew();
        var masterReportNewList = allMasterReportNew_1(allTrackingReportNew, httpHeaders);
        var trackingReportResponses = allTrackingReportToTrackingReportDto(trackingReports, masterReportNewList);
        RowValues rowValues = new RowValues();
        rowValues.setRowValues(Collections.singletonList(trackingReportResponses));
        return rowValues;
    }



    private RowValues tripReport(Date startDate, Date endDate, String sort, int limit, HttpHeaders httpHeaders, String tenant) throws JsonProcessingException {
        var allTripReport = tripReportRepository.getAllTripReport(limit);
//        var allTripReportNew = tripReportNewRepository.getAllTripReportNew();
//        var allTrip = tripRepository.findAllByTenantId(tenant);
        var allTripReportNew = masterReportNewRepository.getAllMasterReportNew();
        var masterReportNewList = allMasterReportNew_1(allTripReportNew, httpHeaders);
        var tripReportResponses = allTripReportToTripReportDto(allTripReport, masterReportNewList);
        RowValues rowValues = new RowValues();
        rowValues.setRowValues(Collections.singletonList(tripReportResponses));
        return rowValues;
    }



    @Override
    public byte[] downloadReport(ReportDownloadRequestDto downloadReq, HttpHeaders httpHeaders, String tenant) {
        if (downloadReq == null) {
            throw new ReportException("Query details required");
        }
        var type = downloadReq.getReportType();
        if (type == null) {
            throw new ReportException("Report type is required");
        }
        Format format = downloadReq.getDownloadFormat();
        if (format == null) {
            throw new ReportException("Report format is required");
        }

        var startDate = downloadReq.getFilterStartDate() != null ? downloadReq.getFilterStartDate() : atStartOfDay(new Date());
        var endDate = downloadReq.getFilterEndDate() != null ? downloadReq.getFilterEndDate() : atEndOfDay(new Date());
        var sort = downloadReq.getSort().toString() != null ? downloadReq.getSort().name() : SortBy.ASC.name();
        var limit = downloadReq.getLimit() != 0 ? downloadReq.getLimit() : 500;
        var bytes = generateReport(type, startDate, endDate, sort, format, limit,httpHeaders, tenant);

        return bytes;
    }

    private byte[] generateReport(ReportType type, Date startDate, Date endDate, String sort, Format format, int limit, HttpHeaders httpHeaders, String tenant) {
        byte[] reportContentBytes = new byte[0];
        try {
            switch (type) {
                case MASTERREPORT:
                    reportContentBytes = generateMasterReport(startDate, endDate, sort, format, limit, httpHeaders, tenant);
                    break;
                case INDENTREPORT:
                    reportContentBytes = generateIndentReport(startDate, endDate, sort, format, limit);
                    break;
                case TRIPREPORT:
                    reportContentBytes = generateTripReport(startDate, endDate, sort, format, limit, httpHeaders, tenant);
                    break;
                case TRACKINGREPORT:
                    reportContentBytes = generateTrackingReport(startDate, endDate, sort, format, limit, httpHeaders, tenant);
            }
        } catch (IOException | JRException exception) {
            System.out.println(exception.getMessage());
            throw new ReportException("Internal error while generating the report\n" + exception.getMessage());
        }
        return reportContentBytes;
    }


    //download
    private byte[] generateMasterReport(Date startDate, Date endDate, String sort, Format format, int limit, HttpHeaders httpHeaders, String tenant) throws IOException, JRException {

        var allMasterReport = reportRepository.getAllMasterReport(limit);
        var allMasterReportNew = masterReportNewRepository.getAllMasterReportNew();
        //var allTrip = tripRepository.findAllByTenantId(tenant);
        var masterReportNewList = allMasterReportNew_1(allMasterReportNew, httpHeaders);
        var reportResponses = allMasterReportToMasterReportResponseDto(allMasterReport, masterReportNewList);

        var bytes = new byte[0];

        Map<String,String> columnValues=new LinkedHashMap<>();
        setColumnValuesMaster(columnValues);

        if (format == Format.PDF) {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportResponses);
            JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/new/MasterReport_2.jrxml"));
            Map<String, Object> param = new HashMap<>();
            param.put("MasterReport", "MasterReport details");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        if (format == Format.XLS) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();

            int rowCount = 0;
            Row row = sheet.createRow(rowCount++);

            Font font = wb.createFont();
            font.setBold(true);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setFont(font);

            Cell cell = row.createCell(0);
            cell.setCellValue(columnValues.get("indentId"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(columnValues.get("tripId"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(columnValues.get("orderType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(columnValues.get("contractType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(columnValues.get("createdTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(columnValues.get("transporterName"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue(columnValues.get("vehicleNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue(columnValues.get("vehicleType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue(columnValues.get("driverName"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue(columnValues.get("driverNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue(columnValues.get("pickUpLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(11);
            cell.setCellValue(columnValues.get("dropLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(12);
            cell.setCellValue(columnValues.get("plannedPickUpDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(13);
            cell.setCellValue(columnValues.get("plannedDropDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(14);
            cell.setCellValue(columnValues.get("plannedWeight"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(15);
            cell.setCellValue(columnValues.get("plannedVolume"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(16);
            cell.setCellValue(columnValues.get("plannedNoOfPKGS"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(17);
            cell.setCellValue(columnValues.get("actualWeight"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(18);
            cell.setCellValue(columnValues.get("actualVolume"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(19);
            cell.setCellValue(columnValues.get("actualNoOfPKGS"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(20);
            cell.setCellValue(columnValues.get("lrNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(21);
            cell.setCellValue(columnValues.get("lrTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(22);
            cell.setCellValue(columnValues.get("goodsType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(23);
            cell.setCellValue(columnValues.get("actualPickUpDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(24);
            cell.setCellValue(columnValues.get("actualDropUpDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(25);
            cell.setCellValue(columnValues.get("totalDuration"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(26);
            cell.setCellValue(columnValues.get("tripStatus"));
            cell.setCellStyle(cellStyle);




            cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (MasterReportResponse masterReport : reportResponses) {
                row = sheet.createRow(rowCount++);
                int columnCount = 0;

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getIndentId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getTripId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getOrderType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getContractType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getCreatedTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getTransporterName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getVehicleNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getVehicleType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getDriverName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getDriverNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPickUpLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getDropLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPlannedPickUpDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPlannedDropDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPlannedWeight());
                cell.setCellStyle(cellStyle);


                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPlannedVolume());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getPlannedNoOfPKGS());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getActualWeight());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getActualVolume());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getActualNoOfPKGS());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getLrNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getLrTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getGoodsType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getActualPickUpDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getActualDropUpDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getTotalDuration());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(masterReport.getTripStatus());
                cell.setCellStyle(cellStyle);

            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            wb.write(os);
            wb.close();
            bytes = os.toByteArray();
        }
        return bytes;
    }

    private byte[] generateIndentReport(Date startDate, Date endDate, String sort, Format format, int limit) throws IOException, JRException {
        var indentReports = indentReportRepository.getAllIndent(limit);

        Map<String,String> columnValues=new LinkedHashMap<>();
        setColumnValuesIndent(columnValues);

        var bytes = new byte[0];
        if (format == Format.PDF) {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(indentReports);

            JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/new/IndentReport_2.jrxml"));
            Map<String, Object> param = new HashMap<>();
            param.put("IndentReport", "IndentReport details");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        if (format == Format.XLS) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();

            var rowCount = 0;
            Row row = sheet.createRow(rowCount++);

            Font font = wb.createFont();
            font.setBold(true);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setFont(font);

            Cell cell = row.createCell(0);
            cell.setCellValue(columnValues.get("indentId"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(columnValues.get("createdTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(columnValues.get("orderType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(columnValues.get("contractType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(columnValues.get("vehicleNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(columnValues.get("vehicleType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue(columnValues.get("pickUpLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue(columnValues.get("dropLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue(columnValues.get("goodsType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue(columnValues.get("noOfPKGS"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue(columnValues.get("actualWeight"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(11);
            cell.setCellValue(columnValues.get("plannedPickUpDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(12);
            cell.setCellValue(columnValues.get("plannedDropDateAndTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(13);
            cell.setCellValue(columnValues.get("indentRequestedBy"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(14);
            cell.setCellValue(columnValues.get("vendorName"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(15);
            cell.setCellValue(columnValues.get("indentStatus"));
            cell.setCellStyle(cellStyle);

            cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (IndentReport indentReport : indentReports) {
                row = sheet.createRow(rowCount++);
                int columnCount = 0;

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getIndentId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getCreatedTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getOrderType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getContractType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getVehicleNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getVehicleType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getPickUpLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getDropLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getGoodsType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getNoOfPKGS());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getActualWeight());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getPlannedPickUpDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getPlannedDropDateAndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getIndentRequestedBy());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getVendorName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(indentReport.getIndentStatus());
                cell.setCellStyle(cellStyle);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            wb.close();
            bytes = os.toByteArray();
        }
        return bytes;
    }

    private byte[] generateTrackingReport(Date startDate, Date endDate, String sort, Format format, int limit, HttpHeaders httpHeaders, String tenant) throws IOException, JRException {
        var trackingReports = trackingReportRepository.getAllTrackingReport(limit);
        var allTrackingReportNew = masterReportNewRepository.getAllMasterReportNew();
        var masterReportNewList = allMasterReportNew_1(allTrackingReportNew, httpHeaders);
        var trackingReportResponses = allTrackingReportToTrackingReportDto(trackingReports, masterReportNewList);
        var bytes = new byte[0];

        Map<String,String> columnValues=new LinkedHashMap<>();
        setColumnValuesTracking(columnValues);

        if (format == Format.PDF) {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(trackingReportResponses);

            JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/new/TrackingReport_2.jrxml"));
            Map<String, Object> param = new HashMap<>();
            param.put("TrackingReport", "TrackingReport details");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        if (format == Format.XLS) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();

            int rowCount = 0;
            Row row = sheet.createRow(rowCount++);

            Font font = wb.createFont();
            font.setBold(true);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setFont(font);

            Cell cell = row.createCell(0);
            cell.setCellValue(columnValues.get("tripId"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(columnValues.get("createdTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(columnValues.get("pickUpLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(columnValues.get("dropLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(columnValues.get("vehicleNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(columnValues.get("vehicleType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue(columnValues.get("driverName"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue(columnValues.get("driverNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue(columnValues.get("tripStatus"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue(columnValues.get("startTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue(columnValues.get("endTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(11);
            cell.setCellValue(columnValues.get("totalDuration"));
            cell.setCellStyle(cellStyle);

            cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (TrackingReportDto trackingReport : trackingReportResponses) {
                row = sheet.createRow(rowCount++);
                int columnCount = 0;

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getTripId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getCreatedTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getPickUpLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getDropLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getVehicleNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getVehicleType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getDriverName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getDriverNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getTripStatus());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getStartTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getEndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(trackingReport.getTotalDuration());
                cell.setCellStyle(cellStyle);

            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            wb.close();
            bytes = os.toByteArray();
        }
        return bytes;
    }

    private byte[] generateTripReport(Date startDate, Date endDate, String sort, Format format, int limit, HttpHeaders httpHeaders, String tenant) throws JRException, IOException {
        var tripReports = tripReportRepository.getAllTripReport(limit);

        var allTripReportNew = masterReportNewRepository.getAllMasterReportNew();
        var masterReportNewList = allMasterReportNew_1(allTripReportNew, httpHeaders);

        var tripReportResponses = allTripReportToTripReportDto(tripReports, masterReportNewList);

        byte[] bytes = new byte[0];
        Map<String,String> columnValues=new LinkedHashMap<>();
        setColumnValuesTrip(columnValues);

        if (format == Format.PDF) {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tripReportResponses);

            JasperReport jasperReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/new/TripReport_2.jrxml"));
            Map<String, Object> param = new HashMap<>();
            param.put("TripReport", "TripReport details");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        if (format == Format.XLS) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();

            int rowCount = 0;
            Row row = sheet.createRow(rowCount++);

            Font font = wb.createFont();
            font.setBold(true);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setFont(font);

            Cell cell = row.createCell(0);
            cell.setCellValue(columnValues.get("tripId"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(columnValues.get("orderType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(columnValues.get("contractType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(columnValues.get("vehicleNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(columnValues.get("vehicleType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(columnValues.get("driverName"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue(columnValues.get("driverNumber"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue(columnValues.get("pickUpLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue(columnValues.get("dropLocation"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue(columnValues.get("goodsType"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue(columnValues.get("startTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(11);
            cell.setCellValue(columnValues.get("endTime"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(12);
            cell.setCellValue(columnValues.get("tripStatus"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(13);
            cell.setCellValue(columnValues.get("totalDuration"));
            cell.setCellStyle(cellStyle);

            cellStyle = wb.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (TripReportDto tripReport : tripReportResponses) {
                row = sheet.createRow(rowCount++);
                int columnCount = 0;

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getTripId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getOrderType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getContractType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getVehicleNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getVehicleType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getDriverName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getDriverNumber());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getPickUpLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getDropLocation());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getGoodsType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getStartTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getEndTime());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getTripStatus());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount++);
                cell.setCellValue(tripReport.getTotalDuration());
                cell.setCellStyle(cellStyle);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            wb.close();
            bytes = os.toByteArray();
        }
        return bytes;
    }


    private Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private List<MasterReportNew> allMasterReportNew_1(List<MasterReportNew> allTrip, HttpHeaders httpHeaders) throws JsonProcessingException {
        List<MasterReportNew> newList=new ArrayList<>();
        for (MasterReportNew masterReportNew : allTrip) {

            
            HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            JsonObject jsonObject = null;
            ResponseEntity<String> responseLocation = null;
            LocationRequest locationInfo = null;
            responseLocation = restTemplate.exchange(locationUri
                            + masterReportNew.getDropLocation(),
                    HttpMethod.GET, requestEntity, String.class);

            jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
            if (jsonObject.has("response")) {
                jsonObject = (JsonObject) jsonObject.get("response");

                locationInfo = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
                        LocationRequest.class);
                masterReportNew.setDropLocation(locationInfo.getAddress());//dropLocation
            }

            responseLocation = restTemplate.exchange(locationUri
                            + masterReportNew.getPickUpLocation(),
                    HttpMethod.GET, requestEntity, String.class);

            jsonObject = new Gson().fromJson(responseLocation.getBody(), JsonObject.class);
            if (jsonObject.has("response")) {
                jsonObject = (JsonObject) jsonObject.get("response");

                locationInfo = new ObjectMapper().findAndRegisterModules().readValue(jsonObject.toString(),
                        LocationRequest.class);

                masterReportNew.setPickUpLocation(locationInfo.getAddress());//pickUpLocation
            }
            newList.add(masterReportNew);
        }
        return newList;
    }


    private List<MasterReportResponse> allMasterReportToMasterReportResponseDto(List<MasterReport> allMasterReport, List<MasterReportNew> allMasterReportNew) {
        List<MasterReportResponse> reportResponses = new ArrayList<>();
        for (MasterReport masterReport : allMasterReport) {
            MasterReportResponse response = new MasterReportResponse();
            response.setIndentId(masterReport.getIndentId());
            response.setTripId(masterReport.getTripId());
            response.setOrderType(masterReport.getOrderType());
            response.setContractType(masterReport.getContractType());
            response.setCreatedTime(masterReport.getCreatedTime());
            response.setVehicleNumber(masterReport.getVehicleNumber());
            response.setVehicleType(masterReport.getVehicleType());
            response.setDriverName(masterReport.getDriverName());
            response.setDriverNumber(masterReport.getDriverNumber());
            response.setPickUpLocation(masterReport.getPickUpLocation());
            response.setDropLocation(masterReport.getDropLocation());
            response.setPlannedPickUpDateAndTime(masterReport.getPlannedPickUpDateAndTime());
            response.setPlannedDropDateAndTime(masterReport.getPlannedDropDateAndTime());
            response.setPlannedWeight(masterReport.getPlannedWeight());
            response.setPlannedVolume(masterReport.getPlannedVolume());
            response.setPlannedNoOfPKGS(masterReport.getPlannedNoOfPKGS());
            response.setActualWeight(masterReport.getActualWeight());
            response.setActualVolume(masterReport.getActualVolume());
            response.setActualNoOfPKGS(masterReport.getActualNoOfPKGS());
            response.setLrNumber(masterReport.getLrNumber());
            response.setLrTime(masterReport.getLrTime());
            response.setGoodsType(masterReport.getGoodsType());
            response.setActualPickUpDateAndTime(masterReport.getActualPickUpDateAndTime());
            response.setActualDropUpDateAndTime(masterReport.getActualDropUpDateAndTime());
            response.setTotalDuration(masterReport.getTotalDuration());
            response.setTripStatus(masterReport.getTripStatus());
            reportResponses.add(response);
        }
        for (MasterReportNew reportNew : allMasterReportNew) {
            MasterReportResponse response = new MasterReportResponse();
            response.setTripId(reportNew.getTripId());
            response.setCreatedTime(reportNew.getCreatedTime());
            response.setTransporterName(reportNew.getTransporterName());
            response.setVehicleNumber(reportNew.getVehicleNumber());
            response.setVehicleType(reportNew.getVehicleType());
            response.setDriverName(reportNew.getDriverName());
            response.setDriverNumber(reportNew.getDriverNumber());
            response.setPickUpLocation(reportNew.getPickUpLocation());
            response.setDropLocation(reportNew.getDropLocation());
            response.setActualWeight(reportNew.getActualWeight());
            response.setActualVolume(reportNew.getActualVolume());
            response.setActualNoOfPKGS(reportNew.getActualQuantity());
            response.setLrNumber(reportNew.getLrNumber());
            response.setGoodsType(reportNew.getGoodsType());
            response.setTripStatus(reportNew.getTripStatus());
            reportResponses.add(response);
        }
        return reportResponses;
    }

    private List<TrackingReportDto> allTrackingReportToTrackingReportDto(List<TrackingReport> trackingReports, List<MasterReportNew> allTrackingReportNew) {
        List<TrackingReportDto> responses = new ArrayList<>();
        for (TrackingReport trackingReport : trackingReports) {
            TrackingReportDto response = new TrackingReportDto();
            response.setTripId(trackingReport.getTripId());
            response.setCreatedTime(trackingReport.getCreatedTime());
            response.setPickUpLocation(trackingReport.getPickUpLocation());
            response.setDropLocation(trackingReport.getDropLocation());
            response.setVehicleNumber(trackingReport.getVehicleNumber());
            response.setVehicleType(trackingReport.getVehicleType());
            response.setDriverName(trackingReport.getDriverName());
            response.setDriverNumber(trackingReport.getDriverNumber());
            response.setTripStatus(trackingReport.getTripStatus());
            response.setStartTime(trackingReport.getStartTime());
            response.setEndTime(trackingReport.getEndTime());
            response.setTotalDuration(trackingReport.getTotalDuration());
            responses.add(response);
        }
        for (MasterReportNew trackingReportNew : allTrackingReportNew) {
            TrackingReportDto response = new TrackingReportDto();
            response.setTripId(trackingReportNew.getTripId());
            response.setCreatedTime(trackingReportNew.getCreatedTime());
            response.setPickUpLocation(trackingReportNew.getPickUpLocation());
            response.setDropLocation(trackingReportNew.getDropLocation());
            response.setVehicleNumber(trackingReportNew.getVehicleNumber());
            response.setVehicleType(trackingReportNew.getVehicleType());
            response.setDriverName(trackingReportNew.getDriverName());
            response.setDriverNumber(trackingReportNew.getDriverNumber());
            response.setTripStatus(trackingReportNew.getTripStatus());
            responses.add(response);
        }
        return responses;
    }


    private List<TripReportDto> allTripReportToTripReportDto(List<TripReport> allTripReport, List<MasterReportNew> allTripReportNew) {
        List<TripReportDto> responses = new ArrayList<>();
        for (TripReport tripReport : allTripReport) {
            TripReportDto response = new TripReportDto();
            response.setTripId(tripReport.getTripId());
            response.setOrderType(tripReport.getOrderType());
            response.setContractType(tripReport.getContractType());
            response.setVehicleNumber(tripReport.getVehicleNumber());
            response.setVehicleType(tripReport.getVehicleType());
            response.setDriverName(tripReport.getDriverName());
            response.setDriverNumber(tripReport.getDriverNumber());
            response.setPickUpLocation(tripReport.getPickUpLocation());
            response.setDropLocation(tripReport.getDropLocation());
            response.setGoodsType(tripReport.getGoodsType());
            response.setStartTime(tripReport.getStartTime());
            response.setEndTime(tripReport.getEndTime());
            response.setTripStatus(tripReport.getTripStatus());
            response.setTotalDuration(tripReport.getTotalDuration());
            responses.add(response);
        }
        for (MasterReportNew tripReportNew : allTripReportNew) {
            TripReportDto response = new TripReportDto();
            response.setTripId(tripReportNew.getTripId());
            response.setVehicleNumber(tripReportNew.getVehicleNumber());
            response.setVehicleType(tripReportNew.getVehicleType());
            response.setDriverName(tripReportNew.getDriverName());
            response.setDriverNumber(tripReportNew.getDriverNumber());
            response.setPickUpLocation(tripReportNew.getPickUpLocation());
            response.setDropLocation(tripReportNew.getDropLocation());
            response.setGoodsType(tripReportNew.getGoodsType());
            response.setTripStatus(tripReportNew.getTripStatus());
            responses.add(response);
        }
        return responses;
    }

    private void setColumnValuesMaster(Map<String, String> columnValues) {
        columnValues.put("indentId", "Indent Id");
        columnValues.put("tripId", "TRIP ID");
        columnValues.put("orderType", "Order Type");
        columnValues.put("contractType", "Contract Type");
        columnValues.put("createdTime", "CREATED DATE & TIME");
        columnValues.put("transporterName", "Transporter Name");
        columnValues.put("vehicleNumber", "VEHICLE NUMBER");
        columnValues.put("vehicleType", "VEHICLE TYPE");
        columnValues.put("driverName", "DRIVER NAME");
        columnValues.put("driverNumber", "DRIVER MOBILE No.");
        columnValues.put("pickUpLocation", "PICKUP LOCATION");
        columnValues.put("dropLocation", "DROPOFF LOCATION");
        columnValues.put("plannedPickUpDateAndTime", "PLANNED PICKUP DATE & TIME");
        columnValues.put("plannedDropDateAndTime", "PLANNEDDROPOFF DATE & TIME");
        columnValues.put("plannedWeight", "Planned Weight");
        columnValues.put("plannedVolume", "Planned Volume");
        columnValues.put("plannedNoOfPKGS", "Planned No. Of Packages");
        columnValues.put("actualWeight", "Actual Weight");
        columnValues.put("actualVolume", "Actual Volume");
        columnValues.put("actualNoOfPKGS", "Actual No. Of Packages");
        columnValues.put("lrNumber", "LR Number");
        columnValues.put("lrTime", "LR Time");
        columnValues.put("goodsType", "GOODS TYPE");
        columnValues.put("actualPickUpDateAndTime", "ACTUAL PICKUP DATE & TIME");
        columnValues.put("actualDropUpDateAndTime", "ACTUAL DROPOFF DATE & TIME");
        columnValues.put("totalDuration", "Total Duration");
        columnValues.put("tripStatus", "TRIP STATUS");

    }

    private void setColumnValuesTrip(Map<String, String> columnValues) {
        columnValues.put("tripId", "TRIP ID");
        columnValues.put("orderType", "ORDER TYPE");
        columnValues.put("contractType", "CONTRACT TYPE");
        columnValues.put("vehicleNumber", "VEHICLE NUMBER");
        columnValues.put("vehicleType", "VEHICLE TYPE");
        columnValues.put("driverName", "DRIVER NAME");
        columnValues.put("driverNumber", "DRIVER MOBILE NUMBER");
        columnValues.put("pickUpLocation", "PICK LOCATION");
        columnValues.put("dropLocation", "DROPOFF LOCATION");
        columnValues.put("goodsType", "GOODS TYPE");
        columnValues.put("startTime", "TRIP START DATE & TIME");
        columnValues.put("endTime", "TRIP END DATE & TIME");
        columnValues.put("tripStatus", "TRIP STATUS");
        columnValues.put("totalDuration", "Total Duration");
    }

    private void setColumnValuesTracking(Map<String, String> columnValues) {
        columnValues.put("tripId", "TRIP ID");
        columnValues.put("createdTime", "CREATED DATE & TIME");
        columnValues.put("pickUpLocation", "PICKUP LOCATION");
        columnValues.put("dropLocation", "DROPOFF LOCATION");
        columnValues.put("vehicleNumber", "VEHICLE NUMBER");
        columnValues.put("vehicleType", "VEHICLE TYPE");
        columnValues.put("driverName", "DRIVER NAME");
        columnValues.put("driverNumber", "DRIVER MOBILE No.");
        columnValues.put("tripStatus", "Trip Status");
        columnValues.put("startTime", "TRIP START DATE & TIME");
        columnValues.put("endTime", "TRIP END DATE & TIME");
        columnValues.put("totalDuration", "Total Duration");
    }

    private void setColumnValuesIndent(Map<String, String> columnValues) {
        columnValues.put("indentId", "INDENT ID");
        columnValues.put("createdTime", "CREATED DATE & TIME");
        columnValues.put("orderType", "Order Type");
        columnValues.put("contractType", "CONTRACT TYPE");
        columnValues.put("vehicleNumber", "VEHICLE NUMBER");
        columnValues.put("vehicleType", "VEHICLE TYPE");
        columnValues.put("pickUpLocation", "PICK LOCATION");
        columnValues.put("dropLocation", "DROPOFF LOCATION");
        columnValues.put("goodsType", "GOODS TYPE");
        columnValues.put("noOfPKGS", "NUMBER OF PACKAGES");
        columnValues.put("actualWeight", "ACTUAL WEIGHT (MT)");
        columnValues.put("plannedPickUpDateAndTime", "PLANNED PICKUP DATE & TIME");
        columnValues.put("plannedDropDateAndTime", "PLANNED DROPOFF DATE & TIME");
        columnValues.put("indentRequestedBy", "Indent Requested By");
        columnValues.put("vendorName", "Vendor Name");
        columnValues.put("indentStatus", "INDENT STATUS");
    }

}

