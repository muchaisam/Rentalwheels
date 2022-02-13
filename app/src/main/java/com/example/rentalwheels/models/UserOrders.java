package com.example.rentalwheels.models;

public class UserOrders {


    int Hour;
    int Days;
    int Audi;
    int BMW;
    int LandCruiser;
    int cart_total;
    int date;
    String time;

    String email;

    int id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getHour(){
        return Hour;
    }

    public void setHour(int Hour){
        this.Hour = Hour;
    }

    public int getDays(){
        return Days;
    }

    public void setDays(int Daily){
        this.Days = Days;
    }

    public int getAudi(){
        return Audi;
    }

    public void setAudi(int Audi){
        this.Audi = Audi;
    }

    public int getBMW(){
        return BMW;
    }

    public void setBMW(int BMW){
        this.BMW = BMW;
    }

    public int getLandCruiser(){
        return LandCruiser;
    }

    public void setLandCruiser(int LandCruiser){
        this.LandCruiser = LandCruiser;
    }

    public int getCart_total(){
        return cart_total;
    }

    public void setCart_total(int cart_total){
        this.cart_total = cart_total;
    }

    public int getDate(){
        return date;
    }

    public void setDate(int date){
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }
}
