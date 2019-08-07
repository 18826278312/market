package com.example.entity;

import javax.persistence.*;

public class Label {
    @Id
    private Integer id;

    /**
     * 标签名
     */
    @Column(name = "label_name")
    private String labelName;

    /**
     * 标签值：多个标签用,隔开
     */
    @Column(name = "label_value")
    private String labelValue;

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
     * 获取标签名
     *
     * @return label_name - 标签名
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * 设置标签名
     *
     * @param labelName 标签名
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * 获取标签值：多个标签用,隔开
     *
     * @return label_value - 标签值：多个标签用,隔开
     */
    public String getLabelValue() {
        return labelValue;
    }

    /**
     * 设置标签值：多个标签用,隔开
     *
     * @param labelValue 标签值：多个标签用,隔开
     */
    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }
}