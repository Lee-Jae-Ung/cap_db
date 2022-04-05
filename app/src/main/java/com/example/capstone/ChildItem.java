package com.example.capstone;

public class ChildItem {
    private String pointnum;
    private String pointid;


    public ChildItem() {
    }

    public ChildItem(String pointnum,String pointid) {
        this.pointnum = pointnum;
        this.pointid = pointid;
    }

    public String getPointnum() { return pointnum; }

    public void setPointnum(String device) {
        this.pointnum = pointnum;
    }

    public String getPointid() {
        return pointid;
    }

    public void setPointid(String pointid) {
        this.pointid = pointid;
    }



}

