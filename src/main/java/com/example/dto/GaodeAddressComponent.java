package com.example.dto;

import org.apache.commons.lang.StringUtils;


/**
 * 逆向地理编码中的地址组件
 * @author chinamobile
 *
 */
public class GaodeAddressComponent {
	private String country=null;  							//国家
	private String province=null;								//省
	private String city=null;									//城市
	private String district=null;								//区县
	private String township=null;							//镇区/街道
	private GaodeStreetNumber streetNumber=null;	//道路信息
	private GaodeNeighborhood neighborhood=null;	//社区信息
	private GaodeBuilding building=null;					//楼信息列表
	private Location gdLocation=null;						//经纬度信息
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTownship() {
		return township;
	}
	public void setTownship(String township) {
		this.township = township;
	}
	public GaodeStreetNumber getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(GaodeStreetNumber streetNumber) {
		this.streetNumber = streetNumber;
	}
	public GaodeNeighborhood getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(GaodeNeighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}
	public GaodeBuilding getBuilding() {
		return building;
	}
	public void setBuilding(GaodeBuilding building) {
		this.building = building;
	}
	public Location getGdLocation() {
		return gdLocation;
	}
	public void setGdLocation(Location gdLocation) {
		this.gdLocation = gdLocation;
	}
	public void generateLocation() {
		int pos=0;
		if(this.getStreetNumber()!=null&&StringUtils.equals(this.getStreetNumber().getLocation().trim(), "")==false) {
			pos=this.getStreetNumber().getLocation().indexOf(",");
			this.gdLocation=new Location();
			this.gdLocation.setLng(this.getStreetNumber().getLocation().substring(0, pos));
			this.gdLocation.setLat(this.getStreetNumber().getLocation().substring(pos+1));
		}
	}
	public void clearInvalidString() {
		if(StringUtils.equals(country, "[]")==true)country="";
		if(StringUtils.equals(province, "[]")==true)province="";
		if(StringUtils.equals(city, "[]")==true)city="";
		if(StringUtils.equals(district, "[]")==true)district="";
		if(StringUtils.equals(township, "[]")==true)township="";
		if(streetNumber!=null)streetNumber.clearInvalidString();
		if(neighborhood!=null)neighborhood.clearInvalidString();
		if(building!=null)building.clearInvalidString();
	}
}
