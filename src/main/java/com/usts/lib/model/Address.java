package com.usts.lib.model;

import java.util.HashMap;
import java.util.Map;

public class Address {
	private Map<String, Integer> addressMap = new HashMap<String, Integer>();
	private String standardAddress = "";
	
	
	public Map<String, Integer> getAddressMap() {
		return addressMap;
	}
	public void setAddressMap(Map<String, Integer> addressMap) {
		this.addressMap = addressMap;
	}
	public String getStandardAddress() {
		return standardAddress;
	}
	public void setStandardAddress(String standardAddress) {
		this.standardAddress = standardAddress;
	}

	
}
