package com.example.bankingapp;

public class CustomerDetails {
    private String type;
    private String name;
    private String email;
    private String mobile;
    private String city;

    public CustomerDetails(String type, String name, String email,
                           String mobile, String city) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCity() {
        return city;
    }
}
