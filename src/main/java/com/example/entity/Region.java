package com.example.entity;

import java.util.Date;
import javax.persistence.*;

public class Region {
    @Id
    private Integer id;

    /**
     * 分公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 业务网格名称
     */
    @Column(name = "business_name")
    private String businessName;

    /**
     * 责任田名称
     */
    @Column(name = "duty_name")
    private String dutyName;

    /**
     * 微区域名称
     */
    @Column(name = "region_name")
    private String regionName;

    /**
     * 中心点GPS经纬度
     */
    @Column(name = "ct_gps")
    private String ctGps;

    /**
     * 中心点百度经纬度
     */
    @Column(name = "ct_baidu")
    private String ctBaidu;

    /**
     * 中心点高德经纬度
     */
    @Column(name = "ct_gaode")
    private String ctGaode;

    /**
     * 边界GPS经纬度
     */
    @Column(name = "polygon_gps")
    private String polygonGps;

    /**
     * 边界百度经纬度
     */
    @Column(name = "polygon_baidu")
    private String polygonBaidu;

    /**
     * 边界高德经纬度
     */
    @Column(name = "polygon_gaode")
    private String polygonGaode;

    /**
     * 百度城市
     */
    @Column(name = "baidu_city")
    private String baiduCity;

    /**
     * 百度区县
     */
    @Column(name = "baidu_area")
    private String baiduArea;

    /**
     * 百度镇区街道
     */
    @Column(name = "baidu_town")
    private String baiduTown;

    /**
     * 百度道路
     */
    @Column(name = "baidu_road")
    private String baiduRoad;

    /**
     * 百度实体名称
     */
    @Column(name = "baidu_name")
    private String baiduName;

    /**
     * 高德城市
     */
    @Column(name = "gaode_city")
    private String gaodeCity;

    /**
     * 高德区县
     */
    @Column(name = "gaode_area")
    private String gaodeArea;

    /**
     * 高德镇区街道
     */
    @Column(name = "gaode_town")
    private String gaodeTown;

    /**
     * 高德道路
     */
    @Column(name = "gaode_road")
    private String gaodeRoad;

    /**
     * 高德实体名称
     */
    @Column(name = "gaode_name")
    private String gaodeName;

    /**
     * gps面积指数
     */
    @Column(name = "region_size")
    private Double regionSize;

    /**
     * 责任人姓名
     */
    @Column(name = "duty_user")
    private String dutyUser;

    /**
     * portal账号
     */
    private String portal;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 场景标签
     */
    @Column(name = "scenes_label")
    private String scenesLabel;

    /**
     * 业务标签
     */
    @Column(name = "business_label")
    private String businessLabel;

    /**
     * 通信特征标签
     */
    @Column(name = "feature_label")
    private String featureLabel;

    private Date time;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分公司名称
     *
     * @return company_name - 分公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置分公司名称
     *
     * @param companyName 分公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取业务网格名称
     *
     * @return business_name - 业务网格名称
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * 设置业务网格名称
     *
     * @param businessName 业务网格名称
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * 获取责任田名称
     *
     * @return duty_name - 责任田名称
     */
    public String getDutyName() {
        return dutyName;
    }

    /**
     * 设置责任田名称
     *
     * @param dutyName 责任田名称
     */
    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    /**
     * 获取微区域名称
     *
     * @return region_name - 微区域名称
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * 设置微区域名称
     *
     * @param regionName 微区域名称
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * 获取中心点GPS经纬度
     *
     * @return ct_gps - 中心点GPS经纬度
     */
    public String getCtGps() {
        return ctGps;
    }

    /**
     * 设置中心点GPS经纬度
     *
     * @param ctGps 中心点GPS经纬度
     */
    public void setCtGps(String ctGps) {
        this.ctGps = ctGps;
    }

    /**
     * 获取中心点百度经纬度
     *
     * @return ct_baidu - 中心点百度经纬度
     */
    public String getCtBaidu() {
        return ctBaidu;
    }

    /**
     * 设置中心点百度经纬度
     *
     * @param ctBaidu 中心点百度经纬度
     */
    public void setCtBaidu(String ctBaidu) {
        this.ctBaidu = ctBaidu;
    }

    /**
     * 获取中心点高德经纬度
     *
     * @return ct_gaode - 中心点高德经纬度
     */
    public String getCtGaode() {
        return ctGaode;
    }

    /**
     * 设置中心点高德经纬度
     *
     * @param ctGaode 中心点高德经纬度
     */
    public void setCtGaode(String ctGaode) {
        this.ctGaode = ctGaode;
    }

    /**
     * 获取边界GPS经纬度
     *
     * @return polygon_gps - 边界GPS经纬度
     */
    public String getPolygonGps() {
        return polygonGps;
    }

    /**
     * 设置边界GPS经纬度
     *
     * @param polygonGps 边界GPS经纬度
     */
    public void setPolygonGps(String polygonGps) {
        this.polygonGps = polygonGps;
    }

    /**
     * 获取边界百度经纬度
     *
     * @return polygon_baidu - 边界百度经纬度
     */
    public String getPolygonBaidu() {
        return polygonBaidu;
    }

    /**
     * 设置边界百度经纬度
     *
     * @param polygonBaidu 边界百度经纬度
     */
    public void setPolygonBaidu(String polygonBaidu) {
        this.polygonBaidu = polygonBaidu;
    }

    /**
     * 获取边界高德经纬度
     *
     * @return polygon_gaode - 边界高德经纬度
     */
    public String getPolygonGaode() {
        return polygonGaode;
    }

    /**
     * 设置边界高德经纬度
     *
     * @param polygonGaode 边界高德经纬度
     */
    public void setPolygonGaode(String polygonGaode) {
        this.polygonGaode = polygonGaode;
    }

    /**
     * 获取百度城市
     *
     * @return baidu_city - 百度城市
     */
    public String getBaiduCity() {
        return baiduCity;
    }

    /**
     * 设置百度城市
     *
     * @param baiduCity 百度城市
     */
    public void setBaiduCity(String baiduCity) {
        this.baiduCity = baiduCity;
    }

    /**
     * 获取百度区县
     *
     * @return baidu_area - 百度区县
     */
    public String getBaiduArea() {
        return baiduArea;
    }

    /**
     * 设置百度区县
     *
     * @param baiduArea 百度区县
     */
    public void setBaiduArea(String baiduArea) {
        this.baiduArea = baiduArea;
    }

    /**
     * 获取百度镇区街道
     *
     * @return baidu_town - 百度镇区街道
     */
    public String getBaiduTown() {
        return baiduTown;
    }

    /**
     * 设置百度镇区街道
     *
     * @param baiduTown 百度镇区街道
     */
    public void setBaiduTown(String baiduTown) {
        this.baiduTown = baiduTown;
    }

    /**
     * 获取百度道路
     *
     * @return baidu_road - 百度道路
     */
    public String getBaiduRoad() {
        return baiduRoad;
    }

    /**
     * 设置百度道路
     *
     * @param baiduRoad 百度道路
     */
    public void setBaiduRoad(String baiduRoad) {
        this.baiduRoad = baiduRoad;
    }

    /**
     * 获取百度实体名称
     *
     * @return baidu_name - 百度实体名称
     */
    public String getBaiduName() {
        return baiduName;
    }

    /**
     * 设置百度实体名称
     *
     * @param baiduName 百度实体名称
     */
    public void setBaiduName(String baiduName) {
        this.baiduName = baiduName;
    }

    /**
     * 获取高德城市
     *
     * @return gaode_city - 高德城市
     */
    public String getGaodeCity() {
        return gaodeCity;
    }

    /**
     * 设置高德城市
     *
     * @param gaodeCity 高德城市
     */
    public void setGaodeCity(String gaodeCity) {
        this.gaodeCity = gaodeCity;
    }

    /**
     * 获取高德区县
     *
     * @return gaode_area - 高德区县
     */
    public String getGaodeArea() {
        return gaodeArea;
    }

    /**
     * 设置高德区县
     *
     * @param gaodeArea 高德区县
     */
    public void setGaodeArea(String gaodeArea) {
        this.gaodeArea = gaodeArea;
    }

    /**
     * 获取高德镇区街道
     *
     * @return gaode_town - 高德镇区街道
     */
    public String getGaodeTown() {
        return gaodeTown;
    }

    /**
     * 设置高德镇区街道
     *
     * @param gaodeTown 高德镇区街道
     */
    public void setGaodeTown(String gaodeTown) {
        this.gaodeTown = gaodeTown;
    }

    /**
     * 获取高德道路
     *
     * @return gaode_road - 高德道路
     */
    public String getGaodeRoad() {
        return gaodeRoad;
    }

    /**
     * 设置高德道路
     *
     * @param gaodeRoad 高德道路
     */
    public void setGaodeRoad(String gaodeRoad) {
        this.gaodeRoad = gaodeRoad;
    }

    /**
     * 获取高德实体名称
     *
     * @return gaode_name - 高德实体名称
     */
    public String getGaodeName() {
        return gaodeName;
    }

    /**
     * 设置高德实体名称
     *
     * @param gaodeName 高德实体名称
     */
    public void setGaodeName(String gaodeName) {
        this.gaodeName = gaodeName;
    }

    /**
     * 获取gps面积指数
     *
     * @return region_size - gps面积指数
     */
    public Double getRegionSize() {
        return regionSize;
    }

    /**
     * 设置gps面积指数
     *
     * @param regionSize gps面积指数
     */
    public void setRegionSize(Double regionSize) {
        this.regionSize = regionSize;
    }

    /**
     * 获取责任人姓名
     *
     * @return duty_user - 责任人姓名
     */
    public String getDutyUser() {
        return dutyUser;
    }

    /**
     * 设置责任人姓名
     *
     * @param dutyUser 责任人姓名
     */
    public void setDutyUser(String dutyUser) {
        this.dutyUser = dutyUser;
    }

    /**
     * 获取portal账号
     *
     * @return portal - portal账号
     */
    public String getPortal() {
        return portal;
    }

    /**
     * 设置portal账号
     *
     * @param portal portal账号
     */
    public void setPortal(String portal) {
        this.portal = portal;
    }

    /**
     * 获取电话号码
     *
     * @return telephone - 电话号码
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置电话号码
     *
     * @param telephone 电话号码
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取场景标签
     *
     * @return scenes_label - 场景标签
     */
    public String getScenesLabel() {
        return scenesLabel;
    }

    /**
     * 设置场景标签
     *
     * @param scenesLabel 场景标签
     */
    public void setScenesLabel(String scenesLabel) {
        this.scenesLabel = scenesLabel;
    }

    /**
     * 获取业务标签
     *
     * @return business_label - 业务标签
     */
    public String getBusinessLabel() {
        return businessLabel;
    }

    /**
     * 设置业务标签
     *
     * @param businessLabel 业务标签
     */
    public void setBusinessLabel(String businessLabel) {
        this.businessLabel = businessLabel;
    }

    /**
     * 获取通信特征标签
     *
     * @return feature_label - 通信特征标签
     */
    public String getFeatureLabel() {
        return featureLabel;
    }

    /**
     * 设置通信特征标签
     *
     * @param featureLabel 通信特征标签
     */
    public void setFeatureLabel(String featureLabel) {
        this.featureLabel = featureLabel;
    }

    /**
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }
}