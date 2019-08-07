package com.example.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 逆向地理编码的社区信息列表
 * @author chinamobile
 *
 */
public class GaodeNeighborhood {
	private String name=null;
	private String type=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void clearInvalidString() {
		if(StringUtils.equals(name, "[]")==true)name="";
		if(StringUtils.equals(type, "[]")==true)type="";
	}
}
