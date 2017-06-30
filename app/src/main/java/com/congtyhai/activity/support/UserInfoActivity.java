package com.congtyhai.activity.support;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.model.send.AuthInfo;
import com.congtyhai.model.receive.ResultUserInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity {

    private TextView txtName;
    private TextView txtPosition;
    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtArea;
    private TextView txtUser;
    private TextView txtBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        createToolbar();


        txtName = (TextView) findViewById(R.id.fullname);
        txtAddress = (TextView) findViewById(R.id.address);
        txtArea = (TextView) findViewById(R.id.area);
        txtPhone = (TextView) findViewById(R.id.phone);
        txtPosition = (TextView) findViewById(R.id.position);
        txtUser = (TextView) findViewById(R.id.user);
        txtBirthday = (TextView) findViewById(R.id.birthday);

        getInfo();
    }


    private void getInfo() {

        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        AuthInfo info = new AuthInfo(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN);

        Call<ResultUserInfo> call = apiService.userInfo(info);

        call.enqueue(new Callback<ResultUserInfo>() {
            @Override
            public void onResponse(Call<ResultUserInfo> call,
                                   Response<ResultUserInfo> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        txtName.setText(response.body().getFullname());
                        txtUser.setText(response.body().getUser());
                        txtBirthday.setText(response.body().getBirthday());
                        txtPosition.setText(response.body().getType());
                        txtPhone.setText(response.body().getPhone());
                        txtAddress.setText(response.body().getAddress());
                        txtArea.setText(response.body().getArea());

                    } else {
                        showAlert("Thông báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                }

                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultUserInfo> call, Throwable t) {
                hidepDialog();
                showAlert("Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }
}
