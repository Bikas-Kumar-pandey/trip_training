package com.meratransport.trip.controller;


import com.meratransport.trip.broadCast.entity.BroadCastEntity;
import com.meratransport.trip.driverbroadCast.entity.Keeboot;
import com.meratransport.trip.dto.*;
import com.meratransport.trip.entity.BroadcastCompleteDetails;
import com.meratransport.trip.report.dto.BiddingConfirmationDto;
import com.meratransport.trip.report.dto.BroadCastVendorRequest;
import com.meratransport.trip.report.dto.Broadcastcontract;
import com.meratransport.trip.service.BroadCastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meratransport.trip.constant.ApplicationConstant.*;

@RestController
public class BroadCastController {

    @Autowired
    private BroadCastService broadCastService;


    @GetMapping("broadcast/{entityType}/{handleSource}/{handleType}")
    //Filter By Entity_type, Handle_source, Handle_type
    public  List<BroadCastEntity> getNotification(@PathVariable String entityType,@PathVariable String handleSource, @PathVariable String handleType,@RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                  @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization ) throws Exception {
        return broadCastService.getEntityType(entityType, handleSource, handleType,prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));

    }

    @GetMapping("/teenant/{id}")
    //Get Driver Details Using tenant ID
    public List<Keeboot> getDriverBroadCast(@PathVariable String id,@RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                            @RequestHeader(value = ID,required = false) String ids, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false )String authorization) throws Exception {
        return broadCastService.getDriverBroadCast(id,prepareHeaderMap(tenantId, userId, ids, appType), prepareHttpHeader(tenantId, userId, ids, appType, authorization));

        //@RequestHeader(value = ID,required = false) String ids should be id
    }


    @GetMapping("/vendorsAndTargetRate")
    //Page1 spot vendors and Target Rate Display
    public BroadCastVendorResponse vendorsAndTargetRate(@RequestHeader(value = TENANT_ID) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                        @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception {
        return broadCastService.getVendorsandTargetRate( prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));
    }

    @PostMapping("/vendorsAndTargetRateResponse") //@RequestParam("indentId") String indentId,
    //Page1 save
    public BroadcastCompleteDetails vendorsAndTargetRateResponse( @RequestBody BroadCastVendorRequest broadCastVendorResponse, @RequestHeader(value = TENANT_ID) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                               @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception{
        return broadCastService.vendorsAndTargetRateResponse(broadCastVendorResponse,prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));
    }



    @GetMapping("/broadcastDetailsContract")
    //Page 2 Contract Broadcast Display
    //No Bidding
    public  BroadcastDetailsContract broadcastDetailsContract(@RequestHeader(value = TENANT_ID) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                             @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception{//@RequestHeader(ID) String id
        return broadCastService.broadcastDetailsContract(prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));  //id,authorization
    }


    @PostMapping("/saveBroadcastDetailsContract")
    public  BroadcastCompleteDetails saveBroadcastDetailsContract(@RequestParam String vendorId, @RequestBody Broadcastcontract broadcastContract, @RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                          @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception {
       return broadCastService.saveBroadcastDetailsContract(vendorId,broadcastContract,prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));
    }



    @GetMapping("/vendorsAndRateBiddingDetails")
    //Page3 Broadcast Bidding details Display
    public BrodcastBiddingConfirmDto broadcastBiddingConfirmations(@RequestHeader(value = TENANT_ID) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                                   @RequestHeader(ID) String id, @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception {
        return broadCastService.broadcastBiddingConfirmations(prepareHeaderMap(tenantId, userId, id, appType), prepareHttpHeader(tenantId, userId, id, appType, authorization));
    }

    @PostMapping("/SaveVendorsAndRateBiddingDetails")
    public BroadcastCompleteDetails saveBroadcastBiddingConfirmations(@RequestBody BiddingConfirmationDto biddingDto,  @RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                                      @RequestHeader(ID) String id,   @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception {
        return broadCastService.saveBroadcastBiddingConfirmations(biddingDto, prepareHeaderMap(tenantId, userId, appType, id), prepareHttpHeader(tenantId, userId, appType, id, authorization));

    }




    @GetMapping("/Bidding")
        public BiddingRequest Bidding(String vendorId) throws Exception {
            return broadCastService.bidding(vendorId);


        }

    @PutMapping("/BiddingResponse")
    public BiddingResponse biddingResponse(String vendorId,@RequestBody BiddingResponse biddingResponse ) throws Exception{
       return broadCastService.biddingResponses(vendorId,biddingResponse);

}


@GetMapping("/shipperConformation")
public  List<BiddingResponse>  shipperConformation(String tenantId){
       return broadCastService.shipperConformation(tenantId);
}

@PutMapping("/shipperConfirmCounterOffer")
    public BroadcastCompleteDetails shipperConfirmCounterOffer(String tenantIds, @RequestBody BiddingResponse biddingResponse,@RequestHeader(value = TENANT_ID,required = false) String tenantId, @RequestHeader(value=USER_ID , required = false) String userId,
                                                               @RequestHeader(ID) String id,   @RequestHeader(value = APP_TYPE,required = false) String appType, @RequestHeader(value = "Authorization", required = false)String authorization) throws Exception {
        return broadCastService.shipperConfirmCounterOffer(tenantIds, biddingResponse,prepareHeaderMap(tenantId, userId, appType, id), prepareHttpHeader(tenantId, userId, appType, id, authorization));
    }


    @GetMapping("/vendor-Conformation")
    public BiddingResponse allVendorBiddingConfirmation(String vendorId){
        return broadCastService.allVendorBiddingConfirmation(vendorId);
    }



    private Map<String, String> prepareHeaderMap(String tenantId, String userId, String id, String appType) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(TENANT_ID, tenantId);
        headers.put(ID, id);
        headers.put(USER_ID, userId);
        headers.put(APP_TYPE, appType);
        return headers;

    }

    private HttpHeaders prepareHttpHeader(String tenantId, String userId, String id, String appType,
                                          String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(TENANT_ID, tenantId);
        headers.set(ID, id);
        headers.set(USER_ID, userId);
        headers.set(APP_TYPE, appType);
        headers.set("Authorization", authorization);
        return headers;
    }

}



