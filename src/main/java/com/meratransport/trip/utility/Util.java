package com.meratransport.trip.utility;

import java.util.regex.Pattern;

public class Util {
	
	public static String validateMobile(String mobileNumber) {
		if(mobileNumber.length()<10) return null;
		if(mobileNumber.length()==10)return "91"+ mobileNumber;
		Pattern checkPlus = Pattern.compile("^(\\+91)?\\d{10}$");
		if(checkPlus.matcher(mobileNumber).matches()) {
			return mobileNumber.replace("+", "");
		}
		Pattern check0 = Pattern.compile("(0)?[1-9][0-9]{9}");
		if(check0.matcher(mobileNumber).matches()) {
			return "91"+mobileNumber.substring(1);
		}
		Pattern p = Pattern.compile("(91)?[1-9][0-9]{9}");
		if(p.matcher(mobileNumber).matches()) {
			return mobileNumber;
		}
		return null;
		
	}

}
