package com.example.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 逆向地理编码返回的具体数据结构信息
 * @author chinamobile
 *
 */
public class GaodeReverseGeoCodeResult {
	private String formatted_address=null;
	private GaodeAddressComponent addressComponent=null;
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public GaodeAddressComponent getAddressComponent() {
		return addressComponent;
	}
	public void setAddressComponent(GaodeAddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}
	public void clearInvalidString() {
		if(StringUtils.equals(formatted_address, "[]")==true)formatted_address="";
		if(addressComponent!=null)addressComponent.clearInvalidString();
	}
}
