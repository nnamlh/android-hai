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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

    SharedPreferences sharedPref ;

   protected Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

         if(gps != null) {
             latitude = gps.getLatitude();
             longitude = gps.getLongitude();
         }
        }

        return new HaiLocation(latitude, longitude);
    }

    protected String getListMainFunction() {
       return sharedPref.getString(HaiSetting.KEY_FUNCTION, "");
    }

    protected void setListMainFunction(String funcs) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HaiSetting.KEY_FUNCTION, funcs);
        editor.commit();
    }

    protected List<AgencyInfo> getListAgency() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = sharedPref.getString(HaiSetting.KEY_LIST_AGENCY, "");
        Type listType = new TypeToken<List<AgencyInfo>>() {}.getType();
        return new Gson().fromJson(str, listType);
    }

    protected void setListAgency (String agencies) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HaiSetting.KEY_LIST_AGENCY, agencies);
        editor.commit();
    }

    protected List<AgencyInfo> getListReceive() {
       // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = sharedPref.getString(HaiSetting.KEY_LIST_RECEIVE, "");
        Type listType = new TypeToken<List<AgencyInfo>>() {}.getType();
        return new Gson().fromJson(str, listType);
    }

    protected void setListReceive (String agencies) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HaiSetting.KEY_LIST_RECEIVE, agencies);
        editor.commit();
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
