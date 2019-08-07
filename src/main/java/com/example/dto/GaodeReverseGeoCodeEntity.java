package com.example.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 高德逆向地理编码信息
 * @author chinamobile
 *
 */
public class GaodeReverseGeoCodeEntity {
	private String status=null;
	private String info=null;
	private GaodeReverseGeoCodeResult regeocode=null;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public GaodeReverseGeoCodeResult getRegeocode() {
		return regeocode;
	}
	public void setRegeocode(GaodeReverseGeoCodeResult regeocode) {
		this.regeocode = regeocode;
	}
	public void clearInvalidString() {
		if(StringUtils.equals(status, "[]")==true)status="";
		if(StringUtils.equals(info, "[]")==true)info="";
		if(regeocode!=null) {
			regeocode.clearInvalidString();
		}
	}
}
