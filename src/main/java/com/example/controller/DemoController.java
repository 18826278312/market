package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.GeocoderDto;
import com.example.entity.Business;
import com.example.entity.Company;
import com.example.entity.Duty;
import com.example.entity.Log;
import com.example.entity.LoginUser;
import com.example.entity.Power;
import com.example.entity.Region;
import com.example.mapper.BusinessMapper;
import com.example.mapper.CompanyMapper;
import com.example.mapper.DutyMapper;
import com.example.mapper.LogMapper;
import com.example.mapper.LoginUserMapper;
import com.example.mapper.PowerMapper;
import com.example.mapper.RegionMapper;
import com.example.service.PowerService;
import com.example.service.RegionService;
import com.example.util.CommonMethods;
import com.example.util.FileUtil;
import com.example.util.HttpUtil;
import com.example.vo.PoiVo;
import com.example.vo.PowerVo;
import com.example.vo.SendVo;

@Controller
@RequestMapping("/DemoController")
public class DemoController {

	@Resource
	LoginUserMapper loginUserMapper;
	@Resource
	PowerMapper powerMapper;
	@Resource
	CompanyMapper companyMapper;
	@Resource
	BusinessMapper businessMapper;
	@Resource
	DutyMapper dutyMapper;
	@Resource
	RegionMapper regionMapper;
	@Resource
	RegionService regionService;
	@Resource
	LogMapper logMapper;
	@Resource
	PowerService powerService;
//	String path = "E:/MarketGrid/";
	String path = "D:/微区域数据/";
	@RequestMapping(value="/smallRegion")
	public String smallRegion(HttpServletRequest request,HttpServletResponse response){
		if(request.getSession().getAttribute("telephone")==null){
			try {
				response.sendRedirect("login");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "smallRegion";
	}
	
	@RequestMapping(value="/power")
	public String power(HttpServletRequest request,HttpServletResponse response){
		String telephone = (String) request.getSession().getAttribute("telephone");
		if(telephone==null){
			try {
				response.sendRedirect("login");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Power power = powerMapper.selectByPrimaryKey(telephone);
		if (power!=null&&power.getManagerPower()==1) {
			return "power";
		}else{
			return "smallRegion";
		}
		
	}
	
	@RequestMapping(value="/log")
	public String log(HttpServletRequest request,HttpServletResponse response){
		String telephone = (String) request.getSession().getAttribute("telephone");
		if(telephone==null){
			try {
				response.sendRedirect("login");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Power power = powerMapper.selectByPrimaryKey(telephone);
		if (power!=null&&power.getManagerPower()==1) {
			return "log";
		}else{
			return "smallRegion";
		}
	}
	
	@RequestMapping(value="/login")
	public String login(HttpServletRequest request){
		return "login";
	}
	
	/**
	 * 获取分公司、业务网格、责任田的列表
	 * @return
	 */
	@RequestMapping(value="getLists")
	@ResponseBody
	public Map<String, Object> getLists(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String telephone = (String) request.getSession().getAttribute("telephone");
			Power power = powerService.getPower(telephone);
			List<Company> companyList = companyMapper.selectAll();
			List<Business> businessList = businessMapper.selectAll();
			List<Duty> dutyList = dutyMapper.selectAll();
			map.put("power", power);
			map.put("companyList", companyList);
			map.put("businessList", businessList);
			map.put("dutyList", dutyList);
			map.put("status", 0);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 获取责任田中所有的微区域
	 * @param companyName
	 * @param businessName
	 * @param dutyName
	 * @return
	 */
	@RequestMapping(value="listRegion")
	@ResponseBody
	public Map<String, Object> listRegion(HttpServletRequest request,String companyName,String businessName,String dutyName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Region> list = regionMapper.selectRegion(companyName, businessName, dutyName);
			for(int i=0;i<list.size();i++){
				String[] array = list.get(i).getPolygonGaode().split(";");
				list.get(i).setPolygonGaode(list.get(i).getPolygonGaode().replaceAll(";"+array[array.length-1], ""));
			}
			map.put("regionList", list);
			map.put("status", 0);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 新增微区域
	 * @param lnglat
	 * @param companyName
	 * @param businessName
	 * @param dutyName
	 * @param regionName
	 * @param dutyUser
	 * @param portal
	 * @param telephone
	 * @param kpi
	 * @return
	 */
	@RequestMapping(value="save")
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request,String lnglat,String companyName,String businessName,String dutyName,String regionName,
			String dutyUser,String portal,String telephone,String scenesLabel,String businessLabel,String featureLabel){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (scenesLabel!=null&&scenesLabel.length()>0) {
				scenesLabel = scenesLabel.substring(1);
			}
			if (businessLabel!=null&&businessLabel.length()>0) {
				businessLabel = businessLabel.substring(1);
			}
			if (featureLabel!=null&&featureLabel.length()>0) {
				featureLabel = featureLabel.substring(1);
			}
			String result = regionService.saveRegion(lnglat, companyName, businessName, dutyName, regionName, dutyUser, portal, telephone, scenesLabel,businessLabel,featureLabel);
			if (result.equals("0")) {
				this.addLog(request, "添加微区域："+regionName);
				map.put("status", 0);
			}else{
				this.addLog(request, "添加微区域异常");
				map.put("status", 1);
				map.put("info", result);
			}
			return map;
		} catch (Exception e) {
			this.addLog(request, "添加微区域异常："+e.toString());
			e.printStackTrace();
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 删除微区域
	 * @param regionId
	 * @return
	 */
	@RequestMapping(value="delete")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request,Integer regionId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Region region = regionMapper.selectByPrimaryKey(regionId);
			regionMapper.deleteByPrimaryKey(regionId);
			this.addLog(request, "删除微区域:"+region.getRegionName());
			map.put("status", 0);
			return map;
		} catch (Exception e) {
			this.addLog(request, "删除微区域异常："+e.toString());
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 更新微区域
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
	@RequestMapping(value="edit")
	@ResponseBody
	public Map<String, Object> edit(HttpServletRequest request,String lnglat,String companyName,String businessName,String dutyName,String regionName,
			String dutyUser,String portal,String telephone,String scenesLabel,String businessLabel,String featureLabel,Integer regionId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (scenesLabel!=null&&scenesLabel.length()>0) {
				scenesLabel = scenesLabel.substring(1);
			}
			if (businessLabel!=null&&businessLabel.length()>0) {
				businessLabel = businessLabel.substring(1);
			}
			if (featureLabel!=null&&featureLabel.length()>0) {
				featureLabel = featureLabel.substring(1);
			}
			String result = regionService.updateRegion(lnglat, companyName, businessName, dutyName, regionName, dutyUser, portal, telephone, scenesLabel,businessLabel,featureLabel,regionId);
			if (result.equals("0")) {
				map.put("status", 0);
				this.addLog(request, "编辑微区域："+regionName);
			}else{
				map.put("status", 1);
				map.put("info", result);
				this.addLog(request, "编辑微区域异常");
			}
			return map;
		} catch (Exception e) {
			this.addLog(request, "编辑微区域异常："+e.toString());
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 获取责任田中所有的微网格
	 * @param lnglat 责任田边界
	 * @param businessName 业务网格
	 * @return
	 */
	@RequestMapping(value="getGrid")
	@ResponseBody
	public Map<String, Object> getGrid(HttpServletRequest request,String lnglat,String businessName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> list = FileUtil.readTxtFile(path+"细粒度网格.txt");
			List<String> gridList = new ArrayList<String>();
			for(int i=1;i<list.size();i++){
				if (list.get(i).split("\\|")[20].indexOf(businessName)!=-1) {
					if (CommonMethods.isTwoPolygonsHasContainedRelation(lnglat, list.get(i).split("\\|")[8])) {
						gridList.add(list.get(i));
					}
				}
			}
			map.put("gridList", gridList);
			map.put("status", 0);
			return map;
		} catch (Exception e) {
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	 /**
     * 查找经纬度点所在网格
     * @param lng
     * @param lat
     * @return
     */
    @RequestMapping(value="/getGridByLocation",method= RequestMethod.POST)
   	@ResponseBody
   	public Map<String, Object> getGridByLocation(HttpServletRequest request,String lng,String lat){
       	Map<String,Object> map = new HashMap<String,Object>();
       	try {
       		GeocoderDto geocoderDto= CommonMethods.getBaiduInfo(lng+","+lat);
       		if (geocoderDto.getStatus()==0) {
       			String rightAddress = geocoderDto.getResult().getAddressComponent().getCity() + geocoderDto.getResult().getAddressComponent().getDistrict() + 
    				geocoderDto.getResult().getAddressComponent().getTown() + geocoderDto.getResult().getAddressComponent().getStreet() + geocoderDto.getResult().getSematic_description();
       			map.put("rightAddress", rightAddress);
       			List<Region> regionList = regionMapper.selectAll();
       			for(int i=0;i<regionList.size();i++){
       				if(CommonMethods.isPointInPolygon(lng+","+lat, regionList.get(i).getPolygonGaode())){
       					map.put("region", regionList.get(i));
       					break;
       				}
       			}
       			List<Duty> dutyList = dutyMapper.selectAll();
       			for(int i=0;i<dutyList.size();i++){
       				if(CommonMethods.isPointInPolygon(lng+","+lat, dutyList.get(i).getGaode())){
       					map.put("duty", dutyList.get(i));
       					break;
       				}
       			}
       			map.put("status", 0);
    		}else{
    			map.put("status", 1);
       			map.put("info", "反查百度信息异常");
    		}
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			map.put("status", 2);
   			map.put("info", e.toString());
   			e.printStackTrace();
   		}
       	return map;
    }
    
    /**
	 * 查找高德poi列表
	 * @return
	 */
	@RequestMapping(value="getPois")
	@ResponseBody
	public Map<String, Object> getPois(HttpServletRequest request,String name){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<PoiVo> list = CommonMethods.getPois(name);
			if (list!=null) {
				map.put("status", "0");
				map.put("list", list);
			}else{
				map.put("status", "1");
				map.put("info", "地址不存在");
			}
		} catch (Exception e) {
			map.put("status", "2");
			map.put("info", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取用户权限列表
	 * @return
	 */
	@RequestMapping(value="getPower")
	@ResponseBody
	public Map<String, Object> getPower(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<PowerVo> powerList = powerService.listPower();
			List<Company> companyList = companyMapper.selectAll();
			List<Business> businessList = businessMapper.selectAll();
			List<Duty> dutyList = dutyMapper.selectAll();
			map.put("powerList", powerList);
			map.put("companyList", companyList);
			map.put("businessList", businessList);
			map.put("dutyList", dutyList);
			map.put("status", 0);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 1);
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 修改用户权限
	 * @param powerString
	 * @return
	 */
	@RequestMapping(value="updatePower")
	@ResponseBody
	public Map<String, Object> updatePower(HttpServletRequest request,String powerString){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Company> companyList = companyMapper.selectAll();
			List<Business> businessList = businessMapper.selectAll();
			List<Duty> dutyList = dutyMapper.selectAll();
			PowerVo powerVo = JSONObject.parseObject(powerString, PowerVo.class);
			Power power = powerMapper.selectByPrimaryKey(powerVo.getTelephone());
			String company = "";
			for(int i=0;i<powerVo.getCompanyValue().size();i++){
				for(int j=0;j<companyList.size();j++){
					if (companyList.get(j).getCompanyName().equals(powerVo.getCompanyValue().get(i))) {
						company = company + companyList.get(j).getId() + ",";
						break;
					}
				}
			}
			if (company.length()>0) {
				company = company.substring(0, company.length()-1);
			}
			power.setCompanyPower(company);
			String business = "";
			for(int i=0;i<powerVo.getBusinessValue().size();i++){
				for(int j=0;j<businessList.size();j++){
					if (businessList.get(j).getBusinessName().equals(powerVo.getBusinessValue().get(i))) {
						business = business + businessList.get(j).getId() + ",";
						break;
					}
				}
			}
			if (business.length()>0) {
				business = business.substring(0, business.length()-1);
			}
			power.setBusinessPower(business);
			String duty = "";
			for(int i=0;i<powerVo.getDutyValue().size();i++){
				for(int j=0;j<dutyList.size();j++){
					if (dutyList.get(j).getDutyName().equals(powerVo.getDutyValue().get(i))) {
						duty = duty + dutyList.get(j).getId() + ",";
						break;
					}
				}
			}
			if (duty.length()>0) {
				duty = duty.substring(0, duty.length()-1);
			}
			power.setDutyPower(duty);
			powerMapper.updateByPrimaryKey(power);
			List<PowerVo> powerList = powerService.listPower();
			map.put("powerList", powerList);
			map.put("status", "0");
		} catch (Exception e) {
			map.put("status", "2");
			map.put("info", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 删除用户
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value="deletePower")
	@ResponseBody
	public Map<String, Object> deletePower(HttpServletRequest request,String telephone){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			powerMapper.deleteByPrimaryKey(telephone);
			List<PowerVo> powerList = powerService.listPower();
			map.put("powerList", powerList);
			map.put("status", "0");
		} catch (Exception e) {
			map.put("status", "2");
			map.put("info", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 新增用户
	 * @param telephone
	 * @param userName
	 * @param company
	 * @param position
	 * @return
	 */
	@RequestMapping(value="savePower")
	@ResponseBody
	public Map<String, Object> savePower(HttpServletRequest request,String telephone,String userName,String company,String position){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Power power = new Power();
			power.setTelephone(telephone);
			power.setUserName(userName);
			power.setCompany(company);
			power.setPosition(position);
			powerMapper.insert(power);
			List<PowerVo> powerList = powerService.listPower();
			map.put("powerList", powerList);
			map.put("status", "0");
		} catch (Exception e) {
			map.put("status", "2");
			map.put("info", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 记录日志
	 * @param request
	 * @param action 操作内容
	 */
	public void addLog(HttpServletRequest request,String action){
		String telephone = (String) request.getSession().getAttribute("telephone");
		Power power = powerMapper.selectByPrimaryKey(telephone);
		Log log = new Log();
		log.setAction(action);
		log.setTelephone(telephone);
		log.setCompany(power.getCompany());
		log.setUserName(power.getUserName());
		log.setPosition(power.getPosition());
		log.setTime(new Date());
		logMapper.insert(log);
	}
	
	/**
	 * 获取日志
	 * @param request
	 * @param action
	 */
	@RequestMapping(value="getLog")
	@ResponseBody
	public Map<String, Object> getLog(String name){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Log> list = logMapper.selectLog(name);
			map.put("list", list);
			map.put("status", "0");
		} catch (Exception e) {
			map.put("status", "1");
			map.put("info", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 登陆验证
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value="loginCheck")
	@ResponseBody
	public Map<String, Object> loginCheck(String telephone,String checkCode,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (checkCode.equals(request.getSession().getAttribute(telephone))) {
				request.getSession().setAttribute("telephone", telephone);
				request.getSession().removeAttribute(telephone);
				map.put("status","0");
			}else{
				map.put("status","1");
				map.put("info", "验证码错误");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "2");
			map.put("info", e.toString());
			return map;
		}
	}
	
	/**
	 * 获取手机验证码
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value="getCheckCode")
	@ResponseBody
	public Map<String, Object> getCheckCode(String telephone,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LoginUser user = loginUserMapper.selectByPhone(telephone);
			if (user!=null) {
				String checkCode = generateCheckCode();
				System.out.println(telephone);
				System.out.println(checkCode);
				request.getSession().setAttribute(telephone, checkCode);
				map.put("status","0");
//				String url = "http://localhost:8080/UserLoginManager/sendSms.json?phoneNum="+telephone+"&idCode="+checkCode;
//				System.out.println(url);
//				String json = HttpUtil.sendGetCloseProxy(url);
//				System.out.println("json:"+json);
//				SendVo sendVo = JSONObject.parseObject(json,SendVo.class);
//				if (sendVo.getStatus().equals("0")) {
//					request.getSession().setAttribute(telephone, checkCode);
//					map.put("status","0");
//				}else{
//					map.put("status", "1");	
//					map.put("info", "短信发送异常");
//				}
			}else{
				map.put("status", "1");
				map.put("info", "该手机号没有登陆权限");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "2");
			map.put("info", e.toString());
			return map;
		}
	}
	
	public static String generateCheckCode(){
    	String checkCode = "";
    	Random rand = new Random();
    	checkCode = String.valueOf(rand.nextInt(9))+String.valueOf(rand.nextInt(9))+String.valueOf(rand.nextInt(9))+String.valueOf(rand.nextInt(9));
    	return checkCode;
    }
}
