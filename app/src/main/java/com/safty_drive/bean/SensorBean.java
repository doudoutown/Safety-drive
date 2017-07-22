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
}
