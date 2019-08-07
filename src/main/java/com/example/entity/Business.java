package com.example.entity;

import javax.persistence.*;

public class Business {
    @Id
    private Integer id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "business_name")
    private String businessName;

    private String gps;

    private String gaode;

    private String baidu;

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
     * @return company_name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return business_name
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * @param businessName
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * @return gps
     */
    public String getGps() {
        return gps;
    }

    /**
     * @param gps
     */
    public void setGps(String gps) {
        this.gps = gps;
    }

    /**
     * @return gaode
     */
    public String getGaode() {
        return gaode;
    }

    /**
     * @param gaode
     */
    public void setGaode(String gaode) {
        this.gaode = gaode;
    }

    /**
     * @return baidu
     */
    public String getBaidu() {
        return baidu;
    }

    /**
     * @param baidu
     */
    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }
}