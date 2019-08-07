package com.example.entity;

import java.util.Date;
import javax.persistence.*;

public class Log {
    @Id
    private Integer id;

    /**
     * 操作人手机号
     */
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
     * 完成动作
     */
    private String action;

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
     * 获取操作人手机号
     *
     * @return telephone - 操作人手机号
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置操作人手机号
     *
     * @param telephone 操作人手机号
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
     * 获取完成动作
     *
     * @return action - 完成动作
     */
    public String getAction() {
        return action;
    }

    /**
     * 设置完成动作
     *
     * @param action 完成动作
     */
    public void setAction(String action) {
        this.action = action;
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