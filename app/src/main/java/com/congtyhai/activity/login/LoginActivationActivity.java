package com.congtyhai.activity.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.MainActivity;
import com.congtyhai.activity.R;
import com.congtyhai.model.receive.CheckUserLoginResult;
import com.congtyhai.model.receive.LoginResult;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.RealmController;
import com.congtyhai.util.ServiceGenerator;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivationActivity extends AppCompatActivity {


    @BindView(R.id.txtinfo)
    TextView txtInfo;
    @BindView(R.id.eotp)
    EditText eOtp;
    @BindView(R.id.btnsend)
    Button btnSend;
    @BindView(R.id.btnback)
    Button btnBack;
     ProgressDialog pDialog;

    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activation);

        ButterKnife.bind(this);
        pDialog = new ProgressDialog(LoginActivationActivity.this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCancelable(false);

        Intent intent = getIntent();

        txtInfo.setText(intent.getStringExtra("storename") + "\nĐiện thoại: " + intent.getStringExtra("phone") + "\nĐể tiếp tục vui lòng nhập mã kích hoặt được gửi đến số điện thoại mã quý khách đã đăng kí");

        user = intent.getStringExtra("username");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertCancel("Cảnh báo", "Thoát màn hình này", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LoginActivationActivity.this, EnterNameActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(eOtp.getText().toString())) {
                    showAlert("Cảnh báo", "Nhập mã kich hoạt", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else
                {
                    makeRequest();
                }
            }
        });
    }

    private void makeRequest() {
        showpDialog();

        LoginService apiService = ServiceGenerator.createService(LoginService.class,user, eOtp.getText().toString());

        Call<LoginResult> call = apiService.loginActivaton();

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {


                if (response.body() != null) {
                    if(response.body().getId().equals("0")) {
                        showAlert("Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else{
                        HaiSetting.getInstance().USER = response.body().getUser();
                        HaiSetting.getInstance().TOKEN = response.body().getToken();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        String oldUser = sharedPref.getString(HaiSetting.getInstance().KEY_USER, null);

                        if (oldUser != null && !oldUser.equals(response.body().getUser())) {
                           // RealmController.getInstance().clearCheckInAll();
                           // RealmController.getInstance().clearNotificationAll();
                           // RealmController.getInstance().clearMsgToHai();
                            RealmController.getInstance().clearAllData();
                            HaiSetting.getInstance().resetListProduct();
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString(HaiSetting.getInstance().KEY_USER, response.body().getUser());
                        editor.putString(HaiSetting.getInstance().KEY_TOKEN, response.body().getToken());
                        //  editor.putString(HaiSetting.KEY_FUNCTION, response.body().getFunction());
                        editor.putString(HaiSetting.getInstance().KEY_ROLE, response.body().getRole());
                        HaiSetting.getInstance().ROLE = response.body().getRole();

                        editor.commit();

                        Intent intent = new Intent(LoginActivationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    private String getPhone() {
        TelephonyManager phoneManager = (TelephonyManager)
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = phoneManager.getLine1Number();


        return phoneNumber;
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showAlertCancel(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivationActivity.this);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener).setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    protected void showAlert(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivationActivity.this);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener);
        dialog.setCancelable(false);
        dialog.show();
    }

}
