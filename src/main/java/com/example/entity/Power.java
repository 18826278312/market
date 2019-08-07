package com.example.entity;

import javax.persistence.*;

public class Power {
    /**
     * 用户手机号
     */
    @Id
    private String telephone;

    /**
     * 用户姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 所属分公司
     */
    private String company;

    /**
     * 用户职位
     */
    private String position;

    /**
     * 管理的分公司
     */
    @Column(name = "company_power")
    private String companyPower;

    /**
     * 管理的业务网格
     */
    @Column(name = "business_power")
    private String businessPower;

    /**
     * 管理的责任田
     */
    @Column(name = "duty_power")
    private String dutyPower;

    /**
     * 管理员权限。1：能操作权限控制系统，0不能操作
     */
    @Column(name = "manager_power")
    private Integer managerPower;

    /**
     * 获取用户手机号
     *
     * @return telephone - 用户手机号
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置用户手机号
     *
     * @param telephone 用户手机号
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取用户姓名
     *
     * @return user_name - 用户姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户姓名
     *
     * @param userName 用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取所属分公司
     *
     * @return company - 所属分公司
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置所属分公司
     *
     * @param company 所属分公司
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取用户职位
     *
     * @return position - 用户职位
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置用户职位
     *
     * @param position 用户职位
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 获取管理的分公司
     *
     * @return company_power - 管理的分公司
     */
    public String getCompanyPower() {
        return companyPower;
    }

    /**
     * 设置管理的分公司
     *
     * @param companyPower 管理的分公司
     */
    public void setCompanyPower(String companyPower) {
        this.companyPower = companyPower;
    }

    /**
     * 获取管理的业务网格
     *
     * @return business_power - 管理的业务网格
     */
    public String getBusinessPower() {
        return businessPower;
    }

    /**
     * 设置管理的业务网格
     *
     * @param businessPower 管理的业务网格
     */
    public void setBusinessPower(String businessPower) {
        this.businessPower = businessPower;
    }

    /**
     * 获取管理的责任田
     *
     * @return duty_power - 管理的责任田
     */
    public String getDutyPower() {
        return dutyPower;
    }

    /**
     * 设置管理的责任田
     *
     * @param dutyPower 管理的责任田
     */
    public void setDutyPower(String dutyPower) {
        this.dutyPower = dutyPower;
    }

    /**
     * 获取管理员权限。1：能操作权限控制系统，0不能操作
     *
     * @return manager_power - 管理员权限。1：能操作权限控制系统，0不能操作
     */
    public Integer getManagerPower() {
        return managerPower;
    }

    /**
     * 设置管理员权限。1：能操作权限控制系统，0不能操作
     *
     * @param managerPower 管理员权限。1：能操作权限控制系统，0不能操作
     */
    public void setManagerPower(Integer managerPower) {
        this.managerPower = managerPower;
    }
}