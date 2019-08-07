package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.Power;
import com.example.mapper.BusinessMapper;
import com.example.mapper.CompanyMapper;
import com.example.mapper.DutyMapper;
import com.example.mapper.PowerMapper;
import com.example.service.PowerService;
import com.example.vo.PowerVo;

@Service
public class PowerServiceImpl implements PowerService{

	@Resource
	PowerMapper powerMapper;
	@Resource
	CompanyMapper companyMapper;
	@Resource
	BusinessMapper businessMapper;
	@Resource
	DutyMapper dutyMapper;
	
	@Override
	public Power getPower(String telephone) {
		Power power = powerMapper.selectByPrimaryKey(telephone);
		String[] companyPower = null;
		String[] businessPower = null;
		String[] dutyPower = null;
		String company = "";
		String business = "";
		String duty = "";
		if (!power.getCompanyPower().equals("")) {
			companyPower = power.getCompanyPower().split(",");
		}
		if (!power.getBusinessPower().equals("")) {
			businessPower = power.getBusinessPower().split(",");
		}
		if (!power.getDutyPower().equals("")) {
			dutyPower = power.getDutyPower().split(",");
		}
		if (companyPower!=null) {
			for(int i=0;i<companyPower.length;i++){
				companyPower[i]=companyMapper.selectByPrimaryKey(Integer.valueOf(companyPower[i])).getCompanyName();
				company = company + companyPower[i];
				if (i<companyPower.length-1) {
					company = company + ",";
				}
			}
		}
		if (businessPower!=null) {
			for(int i=0;i<businessPower.length;i++){
				businessPower[i]=businessMapper.selectByPrimaryKey(Integer.valueOf(businessPower[i])).getBusinessName();
				business = business + businessPower[i];
				if (i<businessPower.length-1) {
					business = business + ",";
				}
			}
		}
		if (dutyPower!=null) {
			for(int i=0;i<dutyPower.length;i++){
				dutyPower[i]=dutyMapper.selectByPrimaryKey(Integer.valueOf(dutyPower[i])).getDutyName();
				duty = duty + dutyPower[i];
				if (i<dutyPower.length-1) {
					duty = duty + ",";
				}
			}
		}
		power.setBusinessPower(business);
		power.setCompanyPower(company);
		power.setDutyPower(duty);
		return power;
	}

	@Override
	public List<PowerVo> listPower() {
		PowerVo powerVo;
		List<Power> powerList = powerMapper.selectAll();
		List<PowerVo> powerVos = new ArrayList<PowerVo>();
		for(int j=0;j<powerList.size();j++){
			Power power = powerList.get(j);
			String[] companyPower = null;
			String[] businessPower = null;
			String[] dutyPower = null;
			List<String> company = new ArrayList<String>();
			List<String> business = new ArrayList<String>();
			List<String> duty = new ArrayList<String>();
			if (power.getCompanyPower()!=null && !power.getCompanyPower().equals("")) {
				companyPower = power.getCompanyPower().split(",");
			}
			if (power.getBusinessPower()!=null && !power.getBusinessPower().equals("") ) {
				businessPower = power.getBusinessPower().split(",");
			}
			if (power.getDutyPower()!=null&&!power.getDutyPower().equals("")) {
				dutyPower = power.getDutyPower().split(",");
			}
			if (companyPower!=null) {
				for(int i=0;i<companyPower.length;i++){
					companyPower[i]=companyMapper.selectByPrimaryKey(Integer.valueOf(companyPower[i])).getCompanyName();
					company.add(companyPower[i]);
				}
			}
			if (businessPower!=null) {
				for(int i=0;i<businessPower.length;i++){
					businessPower[i]=businessMapper.selectByPrimaryKey(Integer.valueOf(businessPower[i])).getBusinessName();
					business.add(businessPower[i]);
				}
			}
			if (dutyPower!=null) {
				for(int i=0;i<dutyPower.length;i++){
					dutyPower[i]=dutyMapper.selectByPrimaryKey(Integer.valueOf(dutyPower[i])).getDutyName();
					duty.add(dutyPower[i]);
				}
			}
			powerVo = new PowerVo();
			powerVo.setBusinessPower(power.getBusinessPower());
			powerVo.setBusinessValue(business);
			powerVo.setCompany(power.getCompany());
			powerVo.setCompanyPower(power.getCompanyPower());
			powerVo.setCompanyValue(company);
			powerVo.setDutyPower(power.getDutyPower());
			powerVo.setDutyValue(duty);
			powerVo.setPosition(power.getPosition());
			powerVo.setTelephone(power.getTelephone());
			powerVo.setUserName(power.getUserName());
			powerVos.add(powerVo);
		}
		return powerVos;
	}

}
