package com.companysign.po;

import java.util.Date;

public class Sign {
    private Integer sid;
    private String address;
    private String contactTag;
    private String statusTag;
    private String temperature;
    private Integer userId;
    private Date time;

    @Override
    public String toString() {
        return "Sign{" +
                "sid=" + sid +
                ", address='" + address + '\'' +
                ", contactTag='" + contactTag + '\'' +
                ", statusTag='" + statusTag + '\'' +
                ", temperature='" + temperature + '\'' +
                ", userId=" + userId +
                ", time=" + time +
                '}';
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactTag() {
        return contactTag;
    }

    public void setContactTag(String contactTag) {
        this.contactTag = contactTag;
    }

    public String getStatusTag() {
        return statusTag;
    }

    public void setStatusTag(String statusTag) {
        this.statusTag = statusTag;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
