package com.example.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.dto.GaodeReverseGeoCodeEntity;
import com.example.dto.GeocoderDto;
import com.example.entity.Region;
import com.example.mapper.RegionMapper;
import com.example.service.RegionService;
import com.example.util.BDToGPS;
import com.example.util.CommonMethods;

@Service
public class RegionServiceImpl implements RegionService{

	
	@Resource
	RegionMapper regionMapper;
	
	@Override
	public String saveRegion(String lnglat, String companyName, String businessName, String dutyName, String regionName,
			String dutyUser, String portal, String telephone, String scenesLabel,String businessLabel,String featureLabel) {
		Region region = new Region();
		region.setCompanyName(companyName);
		region.setBusinessName(businessName);
		region.setDutyName(dutyName);
		region.setRegionName(regionName);
		region.setDutyUser(dutyUser);
		region.setPortal(portal);
		region.setTelephone(telephone);
		region.setBusinessLabel(businessLabel);
		region.setScenesLabel(scenesLabel);
		region.setFeatureLabel(featureLabel);
		region.setTime(new Date());
		//计算中心点的高德、gps、百度经纬度
		List<String> list = CommonMethods.getCenterPoint(lnglat);
		region.setCtGaode(list.get(0));
		region.setCtGps(list.get(1));
		region.setCtBaidu(list.get(2));
		lnglat = lnglat + ";" + lnglat.split(";")[0];
		region.setPolygonGaode(lnglat);
		region.setPolygonGps(BDToGPS.gaodeToGps(lnglat));
		region.setPolygonBaidu(BDToGPS.gaodeToBaidu(lnglat));
		region.setRegionSize(CommonMethods.computePolygonArea(BDToGPS.gaodeToGps(lnglat))*100000000);
		//反查百度地址信息
		GeocoderDto geocoderDto = CommonMethods.getBaiduInfo(list.get(2));
		if (geocoderDto.getStatus()==0) {
			region.setBaiduCity(geocoderDto.getResult().getAddressComponent().getCity());
			region.setBaiduArea(geocoderDto.getResult().getAddressComponent().getDistrict());
			region.setBaiduTown(geocoderDto.getResult().getAddressComponent().getTown());
			region.setBaiduRoad(geocoderDto.getResult().getAddressComponent().getStreet());
			region.setBaiduName(geocoderDto.getResult().getSematic_description());
		}else{
			return "反查百度信息异常";
		}
		//反查高德地址信息
		GaodeReverseGeoCodeEntity geoCodeEntity = CommonMethods.getGaoDeInfo(list.get(0));
		if (geoCodeEntity.getStatus().equals("1")) {
			region.setGaodeCity(geoCodeEntity.getRegeocode().getAddressComponent().getCity());
			region.setGaodeArea(geoCodeEntity.getRegeocode().getAddressComponent().getDistrict());
			region.setGaodeTown(geoCodeEntity.getRegeocode().getAddressComponent().getTownship());
			region.setGaodeRoad(geoCodeEntity.getRegeocode().getAddressComponent().getStreetNumber().getStreet());
			region.setGaodeName(geoCodeEntity.getRegeocode().getFormatted_address());
		}else{
			return "反查高德信息异常";
		}
		regionMapper.insert(region);
		return "0";
	}

	@Override
	public String updateRegion(String lnglat, String companyName, String businessName, String dutyName,
			String regionName, String dutyUser, String portal, String telephone, String scenesLabel,String businessLabel,String featureLabel, Integer regionId) {
		Region region = regionMapper.selectByPrimaryKey(regionId);
		region.setCompanyName(companyName);
		region.setBusinessName(businessName);
		region.setDutyName(dutyName);
		region.setRegionName(regionName);
		region.setDutyUser(dutyUser);
		region.setPortal(portal);
		region.setTelephone(telephone);
		region.setTime(new Date());
		region.setBusinessLabel(businessLabel);
		region.setScenesLabel(scenesLabel);
		region.setFeatureLabel(featureLabel);
		//计算中心点的高德、gps、百度经纬度
		List<String> list = CommonMethods.getCenterPoint(lnglat);
		region.setCtGaode(list.get(0));
		region.setCtGps(list.get(1));
		region.setCtBaidu(list.get(2));
		lnglat = lnglat + ";" + lnglat.split(";")[0];
		region.setPolygonGaode(lnglat);
		region.setPolygonGps(BDToGPS.gaodeToGps(lnglat));
		region.setPolygonBaidu(BDToGPS.gaodeToBaidu(lnglat));
		region.setRegionSize(CommonMethods.computePolygonArea(BDToGPS.gaodeToGps(lnglat))*100000000);
		//反查百度地址信息
		GeocoderDto geocoderDto = CommonMethods.getBaiduInfo(list.get(2));
		if (geocoderDto.getStatus()==0) {
			region.setBaiduCity(geocoderDto.getResult().getAddressComponent().getCity());
			region.setBaiduArea(geocoderDto.getResult().getAddressComponent().getDistrict());
			region.setBaiduTown(geocoderDto.getResult().getAddressComponent().getTown());
			region.setBaiduRoad(geocoderDto.getResult().getAddressComponent().getStreet());
			region.setBaiduName(geocoderDto.getResult().getSematic_description());
		}else{
			return "反查百度信息异常";
		}
		//反查高德地址信息
		GaodeReverseGeoCodeEntity geoCodeEntity = CommonMethods.getGaoDeInfo(list.get(0));
		if (geoCodeEntity.getStatus().equals("1")) {
			region.setGaodeCity(geoCodeEntity.getRegeocode().getAddressComponent().getCity());
			region.setGaodeArea(geoCodeEntity.getRegeocode().getAddressComponent().getDistrict());
			region.setGaodeTown(geoCodeEntity.getRegeocode().getAddressComponent().getTownship());
			region.setGaodeRoad(geoCodeEntity.getRegeocode().getAddressComponent().getStreetNumber().getStreet());
			region.setGaodeName(geoCodeEntity.getRegeocode().getFormatted_address());
		}else{
			return "反查高德信息异常";
		}
		regionMapper.updateByPrimaryKey(region);
		return "0";
	}

}
