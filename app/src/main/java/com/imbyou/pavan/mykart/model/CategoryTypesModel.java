package com.imbyou.pavan.mykart.model;

import java.io.Serializable;

/**
 * Created by Lenovo on 17-Apr-17.
 */

public class CategoryTypesModel implements Serializable {

    private String description;
    private String name;
    private String imgurl;
    private String rs;
    private String type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
