package com.android.carserve;

public class packagedata {

    String pid;
    String name;
    String des;
    String photo;
    String cost;
    String packagetype;

    public packagedata() {
    }

    public packagedata(String pid, String name, String des, String photo, String cost, String packagetype) {
        this.pid = pid;
        this.name = name;
        this.des = des;
        this.photo = photo;
        this.cost = cost;
        this.packagetype = packagetype;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPackagetype() {
        return packagetype;
    }

    public void setPackagetype(String packagetype) {
        this.packagetype = packagetype;
    }
}
