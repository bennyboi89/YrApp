package com.example.benny.yrapp;

/**
 * Created by benny on 12/03/2015.
 */
public class City {

        public String cityName;
        public String cityURL;
        public String cityLat;
        public String cityLon;

    public City(String cityName,String cityURL, String cityLat, String cityLon) {
        this.cityURL = cityURL;
        this.cityLat = cityLat;
        this.cityLon = cityLon;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityURL() {
        return cityURL;
    }

    public void setCityURL(String cityURL) {
        this.cityURL = cityURL;
    }

    public String getCityLat() {
        return cityLat;
    }

    public void setCityLat(String cityLat) {
        this.cityLat = cityLat;
    }

    public String getCityLon() {
        return cityLon;
    }

    public void setCityLon(String cityLon) {
        this.cityLon = cityLon;
    }
}
