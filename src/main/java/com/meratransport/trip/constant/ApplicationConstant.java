package com.meratransport.trip.constant;

public final class ApplicationConstant {


	// Header Constants
	public static final String TENANT_ID = "x-keeboot-tid";
	public static final String USER_ID = "x-keeboot-uid";
	public static final String EMPLOYEE_ID = "x-keeboot-eid";
	public static final String APP_TYPE = "x-keeboot-apt";
	public static final String START_INSTALLATION = "START_INSTALLATION";

	public static final String ID="x-keeboot-ID";

	public static final String NO_SHIPPER_INDENT ="No Shipper Indent Exists with given Id";
	
	public static final String GROUP_ID = "x-keeboot-gid";

	//Status Constants

	public static final String SCAN_TAGS = "SCAN_TAGS";
	public static final String INVENTORY_ADDED = "INVENTORY_ADDED";
	
	public static final String ADD_MATERIAL = "ADD_MATERIAL";

	public static final String TRIP_CREATED = "TRIP_CREATED";
	public static final String ADD_BARCODE = "ADD_BARCODE";

	public static final String START_TRIP = "START_TRIP";

	public static final String SCAN_TAGS_UNLOAD = "SCAN_TAGS_UNLOAD";
	
	public static final String IN_TRANSIT = "IN_TRANSIT";

	public static final String VERIFY_BARCODE = "VERIFY_BARCODE";

	public static final String END_TRIP = "END_TRIP";
	
	public static final String TRIP_COMPLETED = "TRIP_COMPLETED";

	// Exception Constants

	public static final String TRIP_NOT_EXISTS = "Trip Doesn't Exists";

	public static final String MASTER_VEHICLE_NOT_FOUND = "Master Vehicle not found";
	public static final String NO_TENANT_EXISTS ="No Tenant Exists with given Id";
	public static final String NO_SHIPPER_EXISTS= "No Shipper Exists with given Id";
	public static final String MASTER_LOCATION_NOT_FOUND = "Master Location not found";

	public static final String TRIP_ALREADY_ENDED = "Trip already ended";

	public static final String MASTER_DRIVER_NOT_FOUND ="Master Driver not found";

	public static final String TENANT_WITH_ID_EXISTS  ="Tenant with same id also registered earlier";

	public static final String TAGS_COUNT_MISMATCH  ="Tags Count Mismatch. There could be some unassigned tags";

	public static final String UNASSIGNED_TAGS ="Unassigned Tags";


	//Common
	public static final String DRIVER ="DRIVER";
	public static final String VEHICLE ="VEHICLE";
	public static final String PICKUP ="PICKUP";
	public static final String DROP ="DROP";
	public static final String MATERIAL ="MATERIAL";

	//Inventory
	public static final String TAG ="TAG";
	public static final String IN_USE ="IN_USE";
	public static final String FREE ="FREE";
	public static final String GPS ="GPS";
	public static final String DAMAGED ="DAMAGED";
	public static final String VEHICLE_TYPE ="VehicleType";
	
	public static final String GROUP_NAME = "groupname";
	



}
