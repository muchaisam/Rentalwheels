package com.example.rentalwheels.models;

import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class AppUser {

    String username, useremail, usermobilenumber;


    public AppUser (String username, String useremail, String usermobilenumber){
        this.username = username;
        this.useremail = useremail;
        this.usermobilenumber = usermobilenumber;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setUseremail(String useremail){
        this.useremail = useremail;
    }

    public String getUsermobilenumber(){
        return usermobilenumber;
    }

    public void setUsermobilenumber(String usermobilenumber){
        this.usermobilenumber = usermobilenumber;
    }
}


