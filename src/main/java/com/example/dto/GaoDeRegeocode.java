package com.example.dto;

import java.util.List;

public class GaoDeRegeocode {
	String formatted_address;
	List<GaoDePoi> pois;
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public List<GaoDePoi> getPois() {
		return pois;
	}
	public void setPois(List<GaoDePoi> pois) {
		this.pois = pois;
	}
	
}
