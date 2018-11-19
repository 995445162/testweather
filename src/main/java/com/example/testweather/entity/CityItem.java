package com.example.testweather.entity;

/**
 * Created by tujianhua on 2017/10/29.
 */

public class CityItem {
    private int id;
    private String code;
    private String name;

    public CityItem(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
