package com.example.rentalwheels.models;

public class DBModel {


    int Hours;
    int Days;
    int Audi;
    int BMW;
    int LandCruiser;
    int final_price;
    String date, time;

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int Hours) {
        this.Hours = Hours;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int Days) {
        this.Days = Days;
    }


    public int getAudi() {
        return Audi;
    }

    public void setAudi(int wash_only) {
        this.Audi = wash_only;
    }

    public int getBMW() {
        return BMW;
    }

    public void setBMW(int BMW) {
        this.BMW = BMW;
    }

    public int getLandCruiser() {
        return LandCruiser;
    }

    public void setLandCruiser(int LandCruiser) {
        this.LandCruiser = LandCruiser;
    }

    public int getFinal_price() {
        return final_price;
    }

    public void setFinal_price(int final_price) {
        this.final_price = final_price;
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


}

