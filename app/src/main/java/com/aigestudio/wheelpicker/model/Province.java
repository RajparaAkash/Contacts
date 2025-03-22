package com.aigestudio.wheelpicker.model;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable {
    public List<City> city;
    public String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<City> getCity() {
        return this.city;
    }

    public void setCity(List<City> list) {
        this.city = list;
    }
}
