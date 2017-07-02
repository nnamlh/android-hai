package com.congtyhai.util;

import com.congtyhai.model.receive.CheckUserLoginResult;
import com.congtyhai.model.receive.LoginResult;
import com.congtyhai.model.send.AgencyRequest;
import com.congtyhai.model.receive.AgencyResult;
import com.congtyhai.model.send.AuthInfo;
import com.congtyhai.model.send.CheckIn;
import com.congtyhai.model.send.CheckLocationRequest;
import com.congtyhai.model.send.CodeSendInfo;
import com.congtyhai.model.receive.CodeSendResult;
import com.congtyhai.model.send.EventInfoSend;
import com.congtyhai.model.send.FirebaseReg;
import com.congtyhai.model.send.MsgToHai;
import com.congtyhai.model.send.NotificationRequet;
import com.congtyhai.model.receive.NotificationResult;
import com.congtyhai.model.receive.ProductFunction;
import com.congtyhai.model.send.ReceiverRequest;
import com.congtyhai.model.receive.ReceiverResult;
import com.congtyhai.model.send.RequestTracking;
import com.congtyhai.model.receive.ResultEvent;
import com.congtyhai.model.receive.ResultEventDetail;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.model.receive.ResultTopic;
import com.congtyhai.model.receive.ResultUpdate;
import com.congtyhai.model.receive.ResultUserInfo;
import com.congtyhai.model.send.StaffHelpRequest;
import com.congtyhai.model.receive.TrackingResukt;
import com.congtyhai.model.send.UpdateProductInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by NAM on 10/19/2016.
 */

public interface ApiInterface {


    @POST("rest/functionproduct")
    Call<ProductFunction> functionProduct(@Body AuthInfo info);

    @POST("rest/getnotification")
    Call<NotificationResult> notification(@Body NotificationRequet info);

    @POST("rest/findagency")
    Call<AgencyResult> findAgency(@Body AgencyRequest info);

    @POST("rest/findreceiver")
    Call<ReceiverResult> findReceiver(@Body ReceiverRequest info);

    @POST("rest/userinfo")
    Call<ResultUserInfo> userInfo(@Body AuthInfo auth);

    @POST("rest/tracking")
    Call<TrackingResukt> tracking(@Body RequestTracking info);

    @POST("rest/sendcodeevent")
    Call<CodeSendResult> sendCode(@Body CodeSendInfo info);

    @POST("rest/loyaltyevent")
    Call<ResultEvent> loyaltyEvent(@Body AuthInfo auth);


    @POST("rest/eventdetail")
    Call<ResultEventDetail> eventDetail(@Body EventInfoSend info);


    @POST("rest/logout")
    Call<ResultInfo> logout(@Body AuthInfo auth);

    @POST("rest/msgtohai")
    Call<ResultInfo> msgToHai(@Body MsgToHai msgToHai);

    @POST("rest/checkin")
    Call<ResultInfo> checkIn(@Body CheckIn checkIn);

    @POST("rest/updateproduct")
    Call<ResultUpdate> updateProduct(@Body UpdateProductInfo info);

    @POST("rest/checklocationdistance")
    Call<ResultInfo> checkLocationDistance(@Body CheckLocationRequest info);

    @POST("rest/helpagencyimport")
    Call<ResultUpdate> updateAgencyimport(@Body StaffHelpRequest info);

    @POST("rest/getMainInfo")
    Call<ResultTopic> updateReg(@Body FirebaseReg firebaseReg);

    @GET("rest/checkuserlogin")
    Call<CheckUserLoginResult> checkUserLogin(
            @Query("user") String user,
            @Query("phone") String phone);

    @GET("rest/loginactivaton")
    Call<LoginResult> loginActivaton(
            @Query("user") String user,
            @Query("otp") String phone);


    // check sesssion
    @GET("rest/loginsession")
    Call<ResultInfo> checkSession(
            @Query("user") String user,
            @Query("token") String token);

    @Multipart
    @POST("upload/checkin")
    Call<ResultInfo> uploadImage(@Part MultipartBody.Part file, @Part("user") RequestBody user,@Part("token") RequestBody token, @Part("extension") RequestBody extension);


}
