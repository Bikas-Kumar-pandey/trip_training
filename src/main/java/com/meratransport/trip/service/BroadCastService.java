package com.meratransport.trip.service;

import com.meratransport.trip.broadCast.entity.BroadCastEntity;
import com.meratransport.trip.driverbroadCast.entity.Keeboot;
import com.meratransport.trip.dto.*;
import com.meratransport.trip.entity.BroadcastCompleteDetails;
import com.meratransport.trip.report.dto.BiddingConfirmationDto;
import com.meratransport.trip.report.dto.BroadCastVendorRequest;
import com.meratransport.trip.report.dto.Broadcastcontract;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public interface BroadCastService {
    public List<BroadCastEntity>  getEntityType(String entityType, String handleSource, String handleType, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;
    public List<Keeboot> getDriverBroadCast(String id,Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

    BroadCastVendorResponse getVendorsandTargetRate(Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

    BroadcastCompleteDetails vendorsAndTargetRateResponse(BroadCastVendorRequest broadCastVendorResponse, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

    public  BroadcastDetailsContract broadcastDetailsContract(Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;


    BroadcastCompleteDetails saveBroadcastDetailsContract(String vendorId, Broadcastcontract broadcastContract, Map<String, String> headers, HttpHeaders httpHeaders) throws Exception;

     BrodcastBiddingConfirmDto broadcastBiddingConfirmations(Map<String, String> headers, HttpHeaders httpHeaders ) throws Exception;


    BroadcastCompleteDetails saveBroadcastBiddingConfirmations(BiddingConfirmationDto biddingDto, Map<String, String> prepareHeaderMap, HttpHeaders prepareHttpHeader) throws Exception;




    public BiddingRequest bidding(String vendorId) throws Exception;

    public BiddingResponse biddingResponses(String vendorId, BiddingResponse biddingResponse ) throws Exception;

public List<BiddingResponse> shipperConformation(String tenantId);


    BroadcastCompleteDetails shipperConfirmCounterOffer(String tenantIds, BiddingResponse biddingResponse, Map<String, String> prepareHeaderMap, HttpHeaders prepareHttpHeader) throws Exception;

    BiddingResponse allVendorBiddingConfirmation(String vendorId);
}

