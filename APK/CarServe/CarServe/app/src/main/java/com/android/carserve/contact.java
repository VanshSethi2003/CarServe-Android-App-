package com.android.carserve;

public class contact {

    String id;
    String email;
    String query;

    public contact(String id, String email, String query) {
        this.id = id;
        this.email = email;
        this.query = query;
    }

    public contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
