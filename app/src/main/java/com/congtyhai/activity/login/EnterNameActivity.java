package com.congtyhai.activity.login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class EnterNameActivity extends AppCompatActivity {


    @BindView(R.id.send)
    Button btnSend;
    private ProgressDialog pDialog;
    @BindView(R.id.euser)
    EditText eUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(EnterNameActivity.this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCancelable(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(eUser.getText().toString())) {
                    showAlertCancel("Cảnh báo", "Nhập tên tài khoản", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eUser.forceLayout();
                        }
                    });
                } else {
                    makeRequest();
                }
            }
        });
    }

    private void makeRequest() {
        showpDialog();
        // ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HaiSetting.getInstance().USER = eUser.getText().toString();

        LoginService apiService = ServiceGenerator.createService(LoginService.class, eUser.getText().toString(), getPhone());
        Call<CheckUserLoginResult> call = apiService.checkUserLogin();

        call.enqueue(new Callback<CheckUserLoginResult>() {
            @Override
            public void onResponse(Call<CheckUserLoginResult> call, Response<CheckUserLoginResult> response) {


                if (response.body() != null) {
                    if (response.body().getId().equals("0")) {
                        showAlert("Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else if (response.body().getId().equals("1")) {
                        Intent intent = new Intent(EnterNameActivity.this, LoginActivity.class);
                        intent.putExtra("username", eUser.getText().toString());
                        startActivity(intent);
                        finish();
                    } else if (response.body().getId().equals("2")) {
                        Intent intent = new Intent(EnterNameActivity.this, LoginActivationActivity.class);
                        intent.putExtra("username", eUser.getText().toString());
                        intent.putExtra("fullname", response.body().getName());
                        intent.putExtra("storename", response.body().getStore());
                        intent.putExtra("code", response.body().getCode());
                        intent.putExtra("phone", response.body().getPhone());

                        startActivity(intent);
                        finish();
                    } else if (response.body().getId().equals("3")) {

                        HaiSetting.getInstance().USER = response.body().getUser();
                        HaiSetting.getInstance().TOKEN = response.body().getToken();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        String oldUser = sharedPref.getString(HaiSetting.getInstance().KEY_USER, null);

                        if (oldUser != null && !oldUser.equals(response.body().getUser())) {
                            // RealmController.getInstance().clearCheckInAll();
                            // RealmController.getInstance().clearNotificationAll();
                            //  RealmController.getInstance().clearMsgToHai();
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

                        Intent intent = new Intent(EnterNameActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<CheckUserLoginResult> call, Throwable t) {
                hidepDialog();
            }
        });
    }

    private String getPhone() {
        TelephonyManager phoneManager = (TelephonyManager)
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
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
    protected void showAlert(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(EnterNameActivity.this);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void showAlertCancel(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(EnterNameActivity.this);
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
}
