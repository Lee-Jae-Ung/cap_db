package com.example.capstone;

public class ParentItem {
    private String section;
    private String location;
    private String ip;
    private String count;

    public ParentItem() {
    }

    public ParentItem(String section, String location, String ip, String count) {
        this.section = section;
        this.location = location;
        this.ip=ip;
        this.count=count;
    }

    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }


    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }


    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String ip) {
        this.count = count;
    }
}