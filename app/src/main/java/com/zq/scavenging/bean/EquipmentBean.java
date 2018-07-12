package com.zq.scavenging.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by AIERXUAN on 2018/6/22.
 */
@Entity
public class EquipmentBean {

    //不能用int
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String name;
    private String nameStr;
    private String postion;
    private int type;
    private int postionType;

    @Generated(hash = 1209560396)
    public EquipmentBean(Long id, String name, String nameStr, String postion,
            int type, int postionType) {
        this.id = id;
        this.name = name;
        this.nameStr = nameStr;
        this.postion = postion;
        this.type = type;
        this.postionType = postionType;
    }

    @Generated(hash = 1113208600)
    public EquipmentBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameStr() {
        return this.nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public String getPostion() {
        return this.postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPostionType() {
        return this.postionType;
    }

    public void setPostionType(int postionType) {
        this.postionType = postionType;
    }
}
