package com.congtyhai.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.model.receive.LoginResult;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.LoginService;
import com.congtyhai.util.RealmController;
import com.congtyhai.util.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private EditText ePass;
    private EditText eUser;
    private Button btnOk;

    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ePass = (EditText) findViewById(R.id.login_password);
        eUser = (EditText) findViewById(R.id.login_user);

        mProgressView = findViewById(R.id.login_progress);
        btnOk = (Button) findViewById(R.id.login_ok);

        ePass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (attemptLogin()) {
                        loginCheck();
                    }
                    return true;
                }
                return false;
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attemptLogin()) {
                    loginCheck();
                }
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean attemptLogin() {

        String email = eUser.getText().toString();
        String password = ePass.getText().toString();

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {

            Toast.makeText(getApplicationContext(), "Mật khẩu phải > 4 kí tự, và không được để trống !", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Không để trống tài khoản!", Toast.LENGTH_SHORT).show();

            return false;
        }


        return  true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private String getImei() {
      //  TelephonyManager phoneManager = (TelephonyManager)
             //   getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
      //  String phoneNumber = phoneManager.getLine1Number();

       /// TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

       // String imsiSIM1 = telephonyInfo.getImsiSIM1();

        return "";
    }

    private void loginCheck() {
        showProgress(true);

        LoginService apiService = ServiceGenerator.createService(LoginService.class, eUser.getText().toString(), ePass.getText().toString());

        Call<LoginResult> call = apiService.basicLogin(getImei());

        call.enqueue(new Callback<LoginResult>() {

            @Override
            public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {
                showProgress(false);

              if (response.body() != null){
                  if ("1".equals(response.body().getId())) {
                      HaiSetting.getInstance().USER = eUser.getText().toString();
                      HaiSetting.getInstance().TOKEN = response.body().getToken();

                      SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                      String oldUser = sharedPref.getString(HaiSetting.KEY_USER, null);

                      if (oldUser != null && !oldUser.equals(response.body().getUser())) {
                          RealmController.getInstance().clearCheckInAll();
                          RealmController.getInstance().clearNotificationAll();
                          RealmController.getInstance().clearMsgToHai();
                          HaiSetting.getInstance().resetListProduct();
                      }

                      SharedPreferences.Editor editor = sharedPref.edit();

                      editor.putString(HaiSetting.KEY_USER, response.body().getUser());
                      editor.putString(HaiSetting.KEY_TOKEN, response.body().getToken());
                      //  editor.putString(HaiSetting.KEY_FUNCTION, response.body().getFunction());
                      editor.putString(HaiSetting.KEY_ROLE, response.body().getRole());
                      HaiSetting.getInstance().ROLE = response.body().getRole();

                      editor.commit();

                      Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                      startActivity(intent);
                      finish();

                  } else {
                      Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                  }
              }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e(LoginActivity.class.getName(), t.toString());
                showProgress(false);
            }
        });
    }


}
