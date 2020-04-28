package com.example.demo.dto;

import java.io.Serializable;

public class QueryUser implements Serializable {

    private String userId;
    private String password;
    //记住我
    private String readMe;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReadMe() {
        return readMe;
    }

    public void setReadMe(String readMe) {
        this.readMe = readMe;
    }
}
