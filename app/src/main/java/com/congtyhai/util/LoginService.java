package com.congtyhai.util;

import com.congtyhai.model.receive.LoginResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by NAM on 10/25/2016.
 */

public interface LoginService {
    @GET("rest/login")
    Call<LoginResult> basicLogin(@Query("imei") String imei);
}