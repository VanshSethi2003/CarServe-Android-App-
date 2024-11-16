package com.android.carserve;

public class record {

    String id;
    String name;
    String cost;
    String date;
    String username;

    public record() {
    }

    public record(String id, String name, String cost, String date, String username) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.date = date;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
