package com.zq.scavenging.bean;

/**
 * Created by AIERXUAN on 2018/6/22.
 */

public class EquipmentBean {

    private String name;
    private String postion;

    public EquipmentBean() {
    }

    public EquipmentBean(String name, String postion) {
        this.name = name;
        this.postion = postion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }
}
