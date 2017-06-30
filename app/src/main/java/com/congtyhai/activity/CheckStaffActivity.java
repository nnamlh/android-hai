package com.congtyhai.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.congtyhai.activity.util.SimpleScanActivity;
import com.congtyhai.app.AppController;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckStaffActivity extends AppCompatActivity {

    private String barcodeScan;

    private String staffCode;

    private ImageView staffImage;

    private Button btnCheck;

    private TextView txtName;
    private TextView txtCode;
    private TextView txtStt;
    private TextView txtBranch;
    private TextView txtPosition;
    private TextView txtAddress;

    private ImageView staffSignture;
    private String TAG = CheckStaffActivity.class.getName();

    private View mProgressView;

    private android.support.v7.app.AlertDialog.Builder dNotification;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        staffImage = (ImageView) findViewById(R.id.staff_image);

        btnCheck = (Button) findViewById(R.id.staff_check);
        txtName = (TextView) findViewById(R.id.staff_name);
        txtCode = (TextView) findViewById(R.id.staff_code);
        txtBranch = (TextView) findViewById(R.id.staff_branch);
        txtPosition = (TextView) findViewById(R.id.staff_position);
        txtStt = (TextView) findViewById(R.id.staff_stt);
        txtAddress = (TextView) findViewById(R.id.staff_address);
        staffSignture = (ImageView) findViewById(R.id.staff_signture);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(CheckStaffActivity.this,SimpleScanActivity.class );
                intent.putExtra("ScreenKey", "staff");
                startActivityForResult(intent, 1);
            }
        });
        mProgressView = (View) findViewById(R.id.staff_progress);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(HaiSetting.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(HaiSetting.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(HaiSetting.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");
                    showNotification(title, message);
                }
            }
        };

        // show dialog notification
        dNotification = new android.support.v7.app.AlertDialog.Builder(CheckStaffActivity.this);
        dNotification.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

    }
    private void showNotification(String title, String messenge) {

        dNotification.setTitle(title);
        dNotification.setMessage(messenge);
        dNotification.show();
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        btnCheck.setVisibility(show?View.GONE:View.VISIBLE);
    }
    private void makeJsonRequest(String user, String token, String code) {

        String URL = HaiSetting.BASEURL + "rest/checkstaff?code=" +code+"&user=" + user + "&token=" + token;
        showProgress(true);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    String result = response.getString("id");
                    if ("1".equals(result)) {
                        Glide.with(getApplicationContext()).load(response.getString("avatar"))
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(staffImage);
                        Glide.with(getApplicationContext()).load(response.getString("signature"))
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(staffSignture);
                        txtStt.setText(response.getString("status"));
                    } else {
                        showAlert(response.getString("msg"));
                    }
                    showProgress(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    showProgress(false);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Kết nối không thành công.",
                        Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showAlert(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(CheckStaffActivity.this);
        dialog.setTitle("Thông báo")
                .setMessage(msg)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (Activity.RESULT_OK == resultCode) {
                barcodeScan = data.getStringExtra("Content");

                try {
                    String[] arr = barcodeScan.split("\n");

                    if (arr.length == 5) {
                        staffCode = arr[0];
                        txtName.setText(arr[1]);
                        txtCode.setText(arr[0]);
                        txtPosition.setText(arr[2]);
                        txtBranch.setText(arr[3]);
                        txtAddress.setText(arr[4]);

                        makeJsonRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, staffCode);

                    } else {
                        Toast.makeText(getApplicationContext(), "Mã quét không đúng.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Mã quét không đúng.", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HaiSetting.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HaiSetting.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
