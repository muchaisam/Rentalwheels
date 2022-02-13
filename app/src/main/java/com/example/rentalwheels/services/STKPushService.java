package com.example.rentalwheels.services;

import com.example.rentalwheels.models.mpesa.AccessToken;
import com.example.rentalwheels.models.mpesa.STKPush;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface STKPushService {
    @POST("mpesa/stkpush/v1/processrequest")
    Call<STKPush> sendPush(@Body STKPush stkPush);

    @GET("jobs/pending")
    Call<STKPush> getTasks();

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken();
}
