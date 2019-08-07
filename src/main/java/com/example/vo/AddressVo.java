package com.example.vo;

import java.util.List;

public class AddressVo {
	private String status;
	private List<PoiVo> pois;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PoiVo> getPois() {
		return pois;
	}
	public void setPois(List<PoiVo> pois) {
		this.pois = pois;
	}
	
}
