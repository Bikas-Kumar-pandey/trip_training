package com.meratransport.trip.dto;//package com.meratransport.trip.dto;
//
//import net.minidev.json.JSONObject;
//
//import java.rmi.ServerException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class BroadcastVehicleRequest {
//    private static final Logger LOG = LoggerFactory.getLogger(BroadcastVehicleRequest.class);
//
//    @Autowired
//    private HttpConnection httpConnection;
//
//    @Value("${url.kmp.create}")
//    private String kmpUrl;
//
//    @Value("${url.kmp.process}")
//    private String kmpProcessUrl;
//
//    @Override
//    public void execute(TransitionContext<ShipperIndentBroadcast> context) throws Exception {
//        super.execute(context);
//        // Process call
//        try {
//            if (null != context) {
//                Map<String, Object> acceptMap = new HashMap<>();
//                ShipperIndentBroadcast sib = (ShipperIndentBroadcast) context.getTransitionParam();
//                if (null == sib || 0 == sib.getShipperIndentBroadcastDetails().size()) {
//                    // throw exception.
//                    throw new ServerException("Shipper Indent broadcast details are empty.");
//                }
//                ShipperIndentBroadcastDetails sibd = sib.getShipperIndentBroadcastDetails().get(0);
//                final BroadcastType type = sibd.getType();
//                final List<VendorRequest> vendors = sibd.getVendors();
//                if (null != type && type.equals(BroadcastType.BROADCAST_VEHICLE_REQUEST) && vendors.size() != 1) {
//                    throw new ServerException("Only one vendor is req.");
//                }
//                acceptMap.put("targetRate", sibd.getTargetRate());
//                acceptMap.put("vendorId", sibd.getVendors());
//                if(null != sib.getLoadId()) {
////					String url = kmpUrl + "/"+sib.getLoadId()+"/VEHICLE-ACCEPT-REQUEST";
//                    Map<String, String> params = new HashMap<>(2);
//                    params.put("url", kmpUrl);
//                    params.put("id", sib.getLoadId());
//                    final String url = StrSubstitutor.replaceNamedKeysInTemplate(kmpProcessUrl, params, "%");
//                    String httpResponse = httpConnection.put(url,acceptMap);
//                    if (!httpResponse.isBlank()) {
//                        LOG.info("Load confirmed for."+sib.getLoadId());
//                        JSONObject obj = new JSONObject(httpResponse);
//                        boolean responseStatus =  (boolean) obj.get("success");
//                        if(responseStatus == true) {
////							String responseId = (String) obj.getJSONObject("data").get("message");
////							sib.setLoadId(responseId);
//                            LOG.info("Load confirmed successfully.");
//                        }else {
//                            throw new ServerException("Error while Process load.");
//                        }
//                    }else {
//                        throw new ServerException("Unable to process load.");
//                    }
//                }else {
//                    throw new ServerException("Load is not created.");
//                }
//            }
//        } catch (Exception e) {
//            LOG.error("Unable to update ", e);
//            throw e;
//        }
//    }
//}
//
//
