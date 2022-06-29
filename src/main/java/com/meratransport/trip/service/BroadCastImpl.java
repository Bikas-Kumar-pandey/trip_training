package com.meratransport.trip.service;

import com.meratransport.trip.broadcastDetail.broadcastDetailEntity.BroadcastDetailsEntity;
import com.meratransport.trip.broadcastDetail.broadcastDetailsRepo.BroadcastDetailsRepo;
import com.meratransport.trip.broadCast.entity.BroadCastEntity;
import com.meratransport.trip.broadCast.repository.BroadCastRepository;
import com.meratransport.trip.constant.ApplicationConstant;
import com.meratransport.trip.driverbroadCast.entity.Keeboot;
import com.meratransport.trip.driverbroadCast.repository.DriverBroadCastRepo;
import com.meratransport.trip.dto.*;
import com.meratransport.trip.entity.BiddingQueryEntity;
import com.meratransport.trip.entity.BroadcastCompleteDetails;
import com.meratransport.trip.entity.VendorsDetails;
import com.meratransport.trip.report.dto.*;
import com.meratransport.trip.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.meratransport.trip.constant.ApplicationConstant.ID;
import static com.meratransport.trip.constant.ApplicationConstant.TENANT_ID;

@Service
public class BroadCastImpl implements BroadCastService {

    @Autowired
    private DriverBroadCastRepo driverBroadCastRepo;

    @Autowired
    private BroadCastRepository broadCastRepo;

    @Autowired
    private BroadcastDetailsRepo broadcastDetailsRepo;

    @Autowired
    private BroadcastCompleteDetailsrepo broadcastCompleteDetailsrepo;

    @Autowired
    private VendorNamesAndIdsRepo vendorNamesAndIdsRepo;

    @Autowired
    private VendorsDetailsRepo vendorsDetails;
    
    @Autowired
    private BiddingQueryRepo biddingQueryRepo;

//@Autowired
//private BiddingRepo biddingRepo;


    @Override
    public List<BroadCastEntity> getEntityType(String entityType, String handleSource, String handleType, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
        isValidRequest(headers);
        List<BroadCastEntity> byEntityTypeAndHandleSourceAndHandleType = broadCastRepo.findByEntityTypeAndHandleSourceAndHandleType(entityType, handleSource, handleType);
        return byEntityTypeAndHandleSourceAndHandleType;
    }

    @Override
    public List<Keeboot> getDriverBroadCast(String id, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        List<Keeboot> drivers = driverBroadCastRepo.findByID(id);
        return drivers;

    }

    @Override
    public BrodcastBiddingConfirmDto broadcastBiddingConfirmations(Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        List<BroadcastBiddingConfirmation> dto1 = new ArrayList<>();
        List<BroadcastDetailsEntity> all = broadcastDetailsRepo.findAllByTenantId(headers.get(TENANT_ID));
        for (BroadcastDetailsEntity values : all) {
            BroadcastBiddingConfirmation confirmation = new BroadcastBiddingConfirmation();
            confirmation.setVendorName(values.getVendorName());
            confirmation.setId(values.getId());
            dto1.add(confirmation);
        }
        BrodcastBiddingConfirmDto dto = new BrodcastBiddingConfirmDto();
        dto.setBroadcastBiddingConfirmations(dto1);
        return dto;
    }

    @Override
    //    POST
    public BroadcastCompleteDetails saveBroadcastBiddingConfirmations(BiddingConfirmationDto biddingDto, Map<String, String> headers, HttpHeaders prepareHttpHeader) throws Exception {
//        isValidRequest(headers);
        String tenantId = headers.get(TENANT_ID);
        BroadcastCompleteDetails detailss = new BroadcastCompleteDetails();
        List<BroadcastCompleteDetails> list = new ArrayList<>();
        BiddingConfirmationDto dto = new BiddingConfirmationDto();
        VendorsDetails vendorsDetails = new VendorsDetails();
        List<VendorsDetails> det = new ArrayList<>();
        List<BroadcastContractDto> broadcastContractDtos = biddingDto.getBroadcastContractDtos();
        for (BroadcastContractDto broadcastContractDto : broadcastContractDtos) {
            vendorsDetails.setName(broadcastContractDto.getVendorName());
            vendorsDetails.setVendoridd(broadcastContractDto.getIds());
            det.add(vendorsDetails);
            detailss.setFinalAmount(broadcastContractDto.getFinalAmount());
            detailss.setRank(broadcastContractDto.getRank());
        }
        detailss.setTenantId(tenantId);
        detailss.setVendorsDetails(det);
        detailss.setTargetRate(biddingDto.getTargetRate());
        return broadcastCompleteDetailsrepo.save(detailss);

    }

    @Override
    public BroadCastVendorResponse getVendorsandTargetRate(Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        List<BroadcastDetailsEntity> all = broadcastDetailsRepo.findAllByTenantId(headers.get(TENANT_ID));
        List<BroadCastVendorName> vendornameAndId = new ArrayList<>();
        BroadCastVendorResponse response = new BroadCastVendorResponse();
      for(BroadcastDetailsEntity resp : all){
          BroadCastVendorName vendor = new BroadCastVendorName();
          vendor.setVendorName(resp.getVendorName());
          vendor.setVendorIds(resp.getId());
          vendornameAndId.add(vendor);
      }
        response.setVendorNames(vendornameAndId);
        return response;
    }




    @Override
    //POST
    public BroadcastCompleteDetails vendorsAndTargetRateResponse(BroadCastVendorRequest broadCastVendorResponse, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        boolean b = broadcastDetailsRepo.existsByTenantId(headers.get(TENANT_ID));
        if(!b){
            throw new Exception("No tenant id present");
        }
        if (broadCastVendorResponse==null ){
            throw new Exception("Select atleast one vendor to Broadcast");
        }
        String tenantId = headers.get(TENANT_ID);
        BroadcastCompleteDetails broadcastCompleteDetails = new BroadcastCompleteDetails();
        List<BroadCastTargetRate> names = broadCastVendorResponse.getNames();
        List<VendorsDetails> details = new ArrayList<>();
for (BroadCastTargetRate detail:names){
    VendorsDetails details1 = new VendorsDetails();
    details1.setVendoridd(detail.getVendorIds());
    details1.setName(detail.getVendorName());
    details.add(details1);
}

broadcastCompleteDetails.setTargetRate(broadCastVendorResponse.getTargetRate());
        broadcastCompleteDetails.setVendorsDetails(details);
        broadcastCompleteDetails.setTenantId(tenantId);
     return broadcastCompleteDetailsrepo.save(broadcastCompleteDetails);
    }

    @Override
    public BroadcastDetailsContract broadcastDetailsContract(Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        List<BroadcastDetailsEntity> allShipperWithContractType = broadcastDetailsRepo.getAllShipperWithContractType(headers.get(TENANT_ID));
List<BroadCastTargetRate> list = new ArrayList<>();
        BroadcastDetailsContract contractResponse = new BroadcastDetailsContract();
        for (BroadcastDetailsEntity details:allShipperWithContractType){
            BroadCastTargetRate vendorDetails = new BroadCastTargetRate();
            vendorDetails.setVendorName(details.getVendorName());
            vendorDetails.setVendorIds(details.getId());
            list.add(vendorDetails);
        }
        contractResponse.setVendorNameAndId(list);
        return contractResponse;
    }


    @Override
    //Post
    public BroadcastCompleteDetails saveBroadcastDetailsContract(String vendorId, Broadcastcontract broadcastContract, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception {
//        isValidRequest(headers);
        Optional<BroadcastDetailsEntity> byId = broadcastDetailsRepo.findById(vendorId);
        if (byId.isEmpty()) {
            throw new Exception("Incorrect Vendor id !");
        }
        String tenantId = headers.get(TENANT_ID);
        BroadcastCompleteDetails details = new BroadcastCompleteDetails();
        details.setTenantId(tenantId);
        String vehicle = broadcastContract.getVehicle();
        String accept = broadcastContract.getAccept();
        String type = broadcastContract.getType();
        List<BroadcastContractDto> broadcastContractDtos = broadcastContract.getBroadcastContractDtos();
        List<VendorsDetails> vendorsDetails = new ArrayList<>();
        for (BroadcastContractDto vendorNamesAndID : broadcastContractDtos) {
            VendorsDetails vendorsDetail = new VendorsDetails();
            vendorsDetail.setVendoridd(vendorNamesAndID.getIds());
            vendorsDetail.setName(vendorNamesAndID.getVendorName());
            vendorsDetails.add(vendorsDetail);
        }
        details.setVendorsDetails(vendorsDetails);
        details.setVehicle(vehicle);
        details.setAccept(accept);
        details.setType(type);
        BroadcastCompleteDetails save = broadcastCompleteDetailsrepo.save(details);
        return save;
    }





    @Override
    public BiddingRequest bidding(String vendoridd) throws Exception {
        BiddingQueryEntity byId = biddingQueryRepo.findByVendoridd(vendoridd); //Used only to fetch from join column

        if (byId==null) {
            throw new Exception("VENDOR DOES NOT EXISTS !");
        }
        BiddingQueryEntity vendorsDetails1 = byId;
        BiddingRequest biddingRequest = new BiddingRequest();
        biddingRequest.setTargetRate(vendorsDetails1.getTargetRate());
        biddingRequest.setVendorName(vendorsDetails1.getName());
        return biddingRequest;
    }

@Override

//Post
    public BiddingResponse biddingResponses(String vendorId, BiddingResponse biddingResponse ) throws Exception{
    BiddingQueryEntity byId = biddingQueryRepo.findByVendoridd(vendorId);
    if (byId==null) {
        throw new Exception("VENDOR DOES NOT EXISTS !");
    }
    BroadcastCompleteDetails entity = new BroadcastCompleteDetails();
    BiddingQueryEntity vendorsDetails1 = byId;
    biddingResponse.getShipperId();
    entity.setVendorName(vendorsDetails1.getName());
    entity.setTargetRate(vendorsDetails1.getTargetRate());
    entity.setCounterOffer(biddingResponse.getCounterOffer());
    broadcastCompleteDetailsrepo.save(entity);
 return biddingResponse;
    }

    @Override
    public List<BiddingResponse> shipperConformation(String tenantId) {
        List<BiddingQueryEntity> all = biddingQueryRepo.findByTenantId(tenantId);
        List<BiddingResponse> biddingResponses = new ArrayList<>();
        BiddingResponse detail= new BiddingResponse();
        for(BiddingQueryEntity details:all){
            detail.setShipperName(details.getName());
            detail.setTargetRate(details.getTargetRate());
            detail.setCounterOffer(details.getCounterOffer());
            biddingResponses.add(detail);
        }
        return biddingResponses;
    }

    @Override
    //POST
    public BroadcastCompleteDetails shipperConfirmCounterOffer(String tenantIds,BiddingResponse biddingResponse,Map<String, String> headers, HttpHeaders httpHeaders ) throws Exception {
        isValidRequest(headers);
       headers.get(TENANT_ID);
        BroadcastCompleteDetails details = new BroadcastCompleteDetails();
        details.setCounterOffer(biddingResponse.getCounterOffer());
        return broadcastCompleteDetailsrepo.save(details);

    }

    @Override
    public BiddingResponse allVendorBiddingConfirmation(String vendorId) {
        BiddingQueryEntity byId = biddingQueryRepo.findByVendoridd(vendorId);
        BiddingResponse details = new BiddingResponse();
        details.setShipperName(byId.getName());
        details.setShipperId(byId.getTenantId());
        details.setTargetRate(byId.getTargetRate());
        details.setCounterOffer(byId.getCounterOffer());
        return details;


    }





    //VALIDATION
    private void isValidRequest(Map<String, String> headers) throws Exception {

        if (!broadcastDetailsRepo.existsById(headers.get(ID))) {
            throw new Exception(ApplicationConstant.NO_SHIPPER_INDENT);
        }

        if (!broadcastDetailsRepo.existsById(headers.get(TENANT_ID))) {
            throw new Exception(ApplicationConstant.NO_TENANT_EXISTS);
        }
    }
}

