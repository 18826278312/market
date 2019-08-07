package com.example.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 逆向地理编码上的道路信息
 * @author chinamobile
 *
 */
public class GaodeStreetNumber {
	private String street=null;   		//道路名称
	private String number=null; 		//道路编号
	private String location=null;		//经纬度，格式是经度，纬度
	private String direction=null;		//坐标点所处街道方位
	private String distance=null;		//门牌地址到请求坐标的距离
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void clearInvalidString() {
		if(StringUtils.equals(street, "[]")==true)street="";
		if(StringUtils.equals(number, "[]")==true)number="";
		if(StringUtils.equals(location, "[]")==true)location="";
		if(StringUtils.equals(direction, "[]")==true)direction="";
		if(StringUtils.equals(distance, "[]")==true)distance="";
	}
}
