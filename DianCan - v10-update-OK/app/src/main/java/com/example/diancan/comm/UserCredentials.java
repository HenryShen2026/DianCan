package com.example.diancan.comm;

public class UserCredentials {
    private String email;
    private String passwd;

    public UserCredentials(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }
}
