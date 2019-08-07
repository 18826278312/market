package com.example.service;

public interface RegionService {
	
	/**
	 * 保存微区域信息
	 * @param lnglat
	 * @param companyName
	 * @param businessName
	 * @param dutyName
	 * @param regionName
	 * @param dutyUser
	 * @param portal
	 * @param telephone
	 * @param kpi
	 * @return "0"表示保存成功
	 */
	String saveRegion(String lnglat,String companyName,String businessName,String dutyName,String regionName,String dutyUser,String portal,String telephone,String scenesLabel,String businessLabel,String featureLabel);
	
	/**
	 * 更新微区域信息
	 * @param lnglat
	 * @param companyName
	 * @param businessName
	 * @param dutyName
	 * @param regionName
	 * @param dutyUser
	 * @param portal
	 * @param telephone
	 * @param kpi
	 * @param regionId
	 * @return
	 */
	String updateRegion(String lnglat,String companyName,String businessName,String dutyName,String regionName,String dutyUser,String portal,String telephone,String scenesLabel,String businessLabel,String featureLabel,Integer regionId);
}
