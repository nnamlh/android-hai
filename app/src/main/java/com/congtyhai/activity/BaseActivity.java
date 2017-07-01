package com.congtyhai.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.congtyhai.model.receive.AgencyInfo;
import com.congtyhai.model.HaiLocation;
import com.congtyhai.model.receive.ProductCodeInfo;
import com.congtyhai.model.receive.ReceiveInfo;
import com.congtyhai.service.GPSTracker;
import com.congtyhai.service.HaiService;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.NotificationUtils;
import com.congtyhai.util.RealmController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by NAM on 6/8/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private android.support.v7.app.AlertDialog.Builder dNotification;
    private ProgressDialog pDialog;
    protected Toolbar toolbar;
    protected LocationManager locationManager;

    protected GPSTracker gps;

    private static final String TAG = BaseActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    SharedPreferences sharedPref;

    protected Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(HaiSetting.getInstance().REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(HaiSetting.getInstance().TOPIC_GLOBAL);
                } else if (intent.getAction().equals(HaiSetting.getInstance().PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");
                    showNotification(title, message);
                }
            }
        };

        // show dialog notification
        dNotification = new android.support.v7.app.AlertDialog.Builder(BaseActivity.this);
        dNotification.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setMessage("Đang xử lý..");
        pDialog.setCancelable(false);

        realm = RealmController.getInstance().getRealm();


        //
        if (!isMyServiceRunning(HaiService.class)) {
            Intent intentSv = new Intent(BaseActivity.this, HaiService.class);
            startService(intentSv);
        }
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps = new GPSTracker(BaseActivity.this);
    }

    protected HaiLocation getCurrentLocation() {
        double latitude = 0;
        double longitude = 0;

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        if (latitude == 0 && longitude == 0) {

            if (gps != null) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
        }

        return new HaiLocation(latitude, longitude);
    }

    protected String getListMainFunction() {
        return sharedPref.getString(HaiSetting.getInstance().KEY_FUNCTION, "");
    }

    protected void setListMainFunction(String funcs) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HaiSetting.getInstance().KEY_FUNCTION, funcs);
        editor.commit();
    }

    protected List<AgencyInfo> getListAgency() {
        //  return RealmController.getInstance().getAgency();
        Gson gson = new Gson();
        try {

            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");
            BufferedReader br = new BufferedReader(
                    new FileReader(file.getAbsoluteFile() +  HaiSetting.getInstance().PATH_AGENCY_JSON));

            Type listType = new TypeToken<List<AgencyInfo>>() {
            }.getType();
            return gson.fromJson(br, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected void getListProduct() {
        //  return RealmController.getInstance().getAgency();
        Gson gson = new Gson();
        try {

            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");
            BufferedReader br = new BufferedReader(
                    new FileReader(file.getAbsoluteFile() +  HaiSetting.getInstance().PATH_PRODUCT_JSON));

            Type listType = new TypeToken<List<ProductCodeInfo>>() {
            }.getType();


            List<ProductCodeInfo> productCodeInfos = gson.fromJson(br, listType);

            for(ProductCodeInfo info: productCodeInfos) {
                HaiSetting.getInstance().addProductCodeMap(info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String json, String path) {
        try {
            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");

            if (!file.exists()) {
                file.mkdirs();
            }



            FileWriter writer = new FileWriter(file.getAbsoluteFile() + path);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveListAgency(AgencyInfo[] agencies) {
        Gson gson = new Gson();
        writeFile(gson.toJson(agencies), HaiSetting.getInstance().PATH_AGENCY_JSON);
    }

    protected List<ReceiveInfo> getListReceive() {
        Gson gson = new Gson();
        try {

            File file = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "HAI");
            BufferedReader br = new BufferedReader(
                    new FileReader(file.getAbsoluteFile() +  HaiSetting.getInstance().PATH_RECEIVE_JSON));

            Type listType = new TypeToken<List<ReceiveInfo>>() {
            }.getType();
            return gson.fromJson(br, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected void saveListReceive(ReceiveInfo[] receiveInfo) {
        Gson gson = new Gson();
        writeFile(gson.toJson(receiveInfo), HaiSetting.getInstance().PATH_RECEIVE_JSON);
    }

    protected void saveListProduct(final ProductCodeInfo[] productCodeInfos) {
        Gson gson = new Gson();
        writeFile(gson.toJson(productCodeInfos), HaiSetting.getInstance().PATH_PRODUCT_JSON);
    }

    protected void updateDaily() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy",
                Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HaiSetting.getInstance().KEY_UPDATE_DAILY, timeStamp);
        editor.commit();
    }

    protected int needUpdateDaily() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy",
                Locale.getDefault()).format(new Date());
        SharedPreferences.Editor editor = sharedPref.edit();
        String data = sharedPref.getString(HaiSetting.getInstance().KEY_UPDATE_DAILY, "");
        if (timeStamp.equals(data))
            return 0;
        else
            return 1;

        // 1:
    }

    protected void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    protected void createToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    protected boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    protected boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    protected void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
        dialog.setTitle("Enable Location")
                .setMessage("Cho phép lấy thông tin GPS từ điện thoại.")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HaiSetting.getInstance().REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(HaiSetting.getInstance().PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        getLastLocation();
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();


                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    private void showNotification(String title, String messenge) {

        dNotification.setTitle(title);
        dNotification.setMessage(messenge);
        dNotification.show();
    }

    protected void showAlert(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
        dialog.setTitle(tile)
                .setMessage(content)
                .setPositiveButton("Đồng ý", listener);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void showAlertCancel(String tile, String content, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
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
