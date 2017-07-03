package com.congtyhai.util;

/**
 * Created by HAI on 7/3/2017.
 */

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.congtyhai.activity.MainActivity;
import com.congtyhai.activity.login.LoginActivationActivity;
import com.congtyhai.model.receive.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Ravi on 04/04/15.
 */
public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            verifyOtp(otp);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResult> call = apiService.loginActivaton(HaiSetting.getInstance().USER, otp);

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {


                if (response.body() != null) {
                    if(response.body().getId().equals("0")) {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else{
                        HaiSetting.getInstance().USER = response.body().getUser();
                        HaiSetting.getInstance().TOKEN = response.body().getToken();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        String oldUser = sharedPref.getString(HaiSetting.getInstance().KEY_USER, null);

                        if (oldUser != null && !oldUser.equals(response.body().getUser())) {
                            RealmController.getInstance().clearCheckInAll();
                            RealmController.getInstance().clearNotificationAll();
                            RealmController.getInstance().clearMsgToHai();
                            HaiSetting.getInstance().resetListProduct();
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString(HaiSetting.getInstance().KEY_USER, response.body().getUser());
                        editor.putString(HaiSetting.getInstance().KEY_TOKEN, response.body().getToken());
                        //  editor.putString(HaiSetting.KEY_FUNCTION, response.body().getFunction());
                        editor.putString(HaiSetting.getInstance().KEY_ROLE, response.body().getRole());
                        HaiSetting.getInstance().ROLE = response.body().getRole();

                        editor.commit();

                        Intent intent = new Intent(HttpService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Thử lại", Toast.LENGTH_LONG).show();
            }
        });
    }

}