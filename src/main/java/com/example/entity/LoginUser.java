package com.example.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "login_user")
public class LoginUser {
    private String phone;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "check_code")
    private String checkCode;

    @Column(name = "login_time")
    private Date loginTime;

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return check_code
     */
    public String getCheckCode() {
        return checkCode;
    }

    /**
     * @param checkCode
     */
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    /**
     * @return login_time
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * @param loginTime
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}