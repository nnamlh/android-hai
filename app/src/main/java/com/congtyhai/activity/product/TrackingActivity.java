package com.congtyhai.activity.product;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.activity.util.SimpleScanActivity;
import com.congtyhai.adapter.TrackingAdapter;
import com.congtyhai.model.send.RequestTracking;
import com.congtyhai.model.receive.TrackingInfo;
import com.congtyhai.model.receive.TrackingResukt;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.DividerItemDecoration;
import com.congtyhai.util.HaiSetting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends BaseActivity {


    private List<TrackingInfo> trackingInfoList;
    private RecyclerView recyclerView;
    private TrackingAdapter adapter;
    private EditText editText;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        createToolbar();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        editText = (EditText) findViewById(R.id.event_input);
        trackingInfoList = new ArrayList<>();
        adapter = new TrackingAdapter(this, trackingInfoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        txtStatus = (TextView) findViewById(R.id.trackingstt);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.input_text || id == EditorInfo.IME_NULL) {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        showAlert("Cảnh báo", "Nhập mã cần kiểm tra.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else if (editText.getText().length() < 17) {
                        showAlert("Cảnh báo", "Mã không hợp lệ.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    } else {
                        tracking(editText.getText().toString());
                    }


                    return true;
                }
                return false;
            }
        });

    }


    public void scanCodeClick(View view) {
        Intent intent = new Intent(TrackingActivity.this, SimpleScanActivity.class);
        intent.putExtra("ScreenKey", "event");
        startActivityForResult(intent, 1);
    }

    public void sendCodeClick(View view) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            showAlert("Cảnh báo", "Nhập mã cần kiểm tra.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else if (editText.getText().length() < 17) {
            showAlert("Cảnh báo", "Mã không hợp lệ.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {
            tracking(editText.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                String code = data.getStringExtra("Content");

                if (code.length() < 17)
                    Toast.makeText(TrackingActivity.this, "Mã không hợp lệ.", Toast.LENGTH_SHORT).show();
                else
                    tracking(code);
            } else
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }

    }

    private void tracking(String code) {
        txtStatus.setVisibility(View.GONE);
        showpDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        RequestTracking info = new RequestTracking(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, code);

        Call<TrackingResukt> call = apiService.tracking(info);

        call.enqueue(new Callback<TrackingResukt>() {
            @Override
            public void onResponse(Call<TrackingResukt> call,
                                   Response<TrackingResukt> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        trackingInfoList.clear();
                        if (response.body().getTracking().length == 0) {
                            txtStatus.setText("Không tìm thấy thông tin sản phẩm này.");
                            txtStatus.setVisibility(View.VISIBLE);
                        } else {
                            txtStatus.setText(response.body().getName());
                            txtStatus.setVisibility(View.VISIBLE);
                        }

                        for (TrackingInfo info : response.body().getTracking()) {
                            trackingInfoList.add(info);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        showAlert("Kết quả", response.body().getMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }

                }
                hidepDialog();
            }

            @Override
            public void onFailure(Call<TrackingResukt> call, Throwable t) {
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
