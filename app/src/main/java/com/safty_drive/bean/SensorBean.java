package com.safty_drive.bean;

import java.util.Date;

/**
 * Created by fanwenbin on 2017/7/22.
 */

public class SensorBean {
    private Integer xValue;
    private Integer yValue;
    private Integer zValue;
    private Date ctime;
    private Double latitude;
    private Double longitude;
    private Float speed;

    public Integer getxValue() {
        return xValue;
    }

    public void setxValue(Integer xValue) {
        this.xValue = xValue;
    }

    public Integer getyValue() {
        return yValue;
    }

    public void setyValue(Integer yValue) {
        this.yValue = yValue;
    }

    public Integer getzValue() {
        return zValue;
    }

    public void setzValue(Integer zValue) {
        this.zValue = zValue;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}
