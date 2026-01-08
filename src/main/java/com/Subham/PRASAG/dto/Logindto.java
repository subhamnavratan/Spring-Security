package com.Subham.PRASAG.dto;

public class Logindto
{
    private String data;
    private String password;

    public Logindto(String data, String password) {
        this.data = data;
        this.password = password;
    }

    public String getData() {
        return data;
    }



    public String getPassword() {
        return password;
    }


}

