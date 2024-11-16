package com.android.carserve;

public class booking {

    String bid;
    String packageid;
    String packname;
    String packagecost;
    String username;
    String date;
    String time;
    String datetime;
    String status;
    String phoneno;
    String email;
    boolean pickup;
    String lat;
    String lng;


    public booking() {
    }

    public booking(String bid, String packageid, String packname, String packagecost, String username, String date, String time, String datetime, String status, String phoneno, String email, boolean pickup, String lat, String lng) {
        this.bid = bid;
        this.packageid = packageid;
        this.packname = packname;
        this.packagecost = packagecost;
        this.username = username;
        this.date = date;
        this.time = time;
        this.datetime = datetime;
        this.status = status;
        this.phoneno = phoneno;
        this.email = email;
        this.pickup = pickup;
        this.lat = lat;
        this.lng = lng;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getPackagecost() {
        return packagecost;
    }

    public void setPackagecost(String packagecost) {
        this.packagecost = packagecost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
