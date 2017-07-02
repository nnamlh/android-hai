package com.congtyhai.activity.support;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.login.EnterNameActivity;
import com.congtyhai.activity.login.LoginActivity;
import com.congtyhai.activity.R;
import com.congtyhai.model.send.AuthInfo;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.service.HaiService;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        createToolbar();

    }

    public void exitHai(View view) {
        showAlertExit();
    }


    public void contactClick(View view) {
        Intent intent = new Intent(SettingActivity.this, ContactActivity.class);
        startActivity(intent);
    }


    private void showAlertExit() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle("Thoát ứng dụng")
                .setMessage("Ứng dụng thoát sẽ không nhận được bất kỳ thông báo từ Hai. Bạn sẽ phải đăng nhập lại.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        sendInfo();
                    }
                })
                .setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void unTopics() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(HaiSetting.getInstance().SHARED_PREF, 0);
        String topics = pref.getString("topics", null);

        if (!TextUtils.isEmpty(topics)) {
            String[] arrayTopic = topics.split("-");

            for (String topic : arrayTopic) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
            }
        } else
            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
    }

    private void sendInfo() {
        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        AuthInfo info = new AuthInfo(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN);

        Call<ResultInfo> call = apiService.logout(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   Response<ResultInfo> response) {
                hidepDialog();
                if (response.body() != null) {
                    unTopics();
                    if (isMyServiceRunning(HaiService.class)) {
                        Intent intentsv = new Intent(SettingActivity.this, HaiService.class);
                        stopService(intentsv);
                    }

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    // editor.putString(HaiSetting.KEY_USER, "");
                    editor.putString(HaiSetting.getInstance().KEY_TOKEN, null);
                    editor.putString(HaiSetting.getInstance().KEY_ROLE, null);
                    editor.putString(HaiSetting.getInstance().KEY_UPDATE_DAILY, null);
                    editor.commit();

                    Intent intent2 = new Intent(SettingActivity.this, EnterNameActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                hidepDialog();
            }
        });
    }


    public void userInfoClick(View view) {
        Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }


    public void sendToHaiClick(View view) {
        Intent intent = new Intent(SettingActivity.this, MsgToHaiActivity.class);
        startActivity(intent);
    }
}
