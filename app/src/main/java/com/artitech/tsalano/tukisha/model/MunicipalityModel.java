package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/09/30.
 */

public class MunicipalityModel {

    private String name;
    private String imgurl;
    private int prevend;
    private int tokenmeter;
    private String endpoint;

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

    public int getPreVend() {
        return prevend;
    }

    public void setPreVend(int prevend) {
        this.prevend = prevend;
    }

    public int getTokenMeter() {
        return tokenmeter;
    }

    public void setTokenMeter(int tokenmeter) {
        this.tokenmeter = tokenmeter;
    }

    public String getEndPoint() {
        return endpoint;
    }

    public void setEndPoint(String endpoint) {
        this.endpoint = endpoint;
    }


}