package com.congtyhai.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.congtyhai.app.AppController;
import com.congtyhai.util.HaiSetting;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    private String TAG = SplashScreen.class.getSimpleName();

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkPermission();

    }

    private void checkPermission() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(SplashScreen.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplashScreen.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplashScreen.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("Cho phép truy cập");
                builder.setMessage("Ứng dụng cần cho phép để truy cập: camera, thẻ nhớ, gps.");
                builder.setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("Cho phép truy cập");
                builder.setMessage("Ứng dụng cần cho phép để truy cập: camera, thẻ nhớ, gps.");
                builder.setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("Cho phép truy cập");
                builder.setMessage("Ứng dụng cần cho phép để truy cập: camera, thẻ nhớ, gps.");
                builder.setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
                dialog.setTitle("Thông báo")
                        .setMessage("Bạn phải cho phép truy cập các điều kiện chúng tôi đưa ra mới có thể sử dụng..")
                        .setPositiveButton("Thử lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                finish();
                            }
                        });
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(SplashScreen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String user = sharedPref.getString(HaiSetting.KEY_USER, null);
                String token = sharedPref.getString(HaiSetting.KEY_TOKEN, null);
                String role = sharedPref.getString(HaiSetting.KEY_ROLE, null);

                if (token == null) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    HaiSetting.getInstance().USER = user;
                    HaiSetting.getInstance().TOKEN = token;
                    HaiSetting.getInstance().ROLE = role;
                    makeJsonRequest(user, token);
                }
            }
        }, 100);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(SplashScreen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void makeJsonRequest(String user, String token) {

        String URL = HaiSetting.BASEURL + "rest/loginsession?user=" + user + "&token=" + token;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    String result = response.getString("id");
                    if ("1".equals(result)) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showAlert();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // showErrorConnect();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void showErrorConnect() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
        dialog.setTitle("Thông báo")
                .setMessage("Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G trên máy.")
                .setPositiveButton("Thử lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        makeJsonRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN);
                    }
                })
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
        dialog.setTitle("Thông báo")
                .setMessage("Tài khoản của bạn đã được đăng nhập ở một thiết bị khác, vui lòng đăng nhập lại hoặt ngưng sử dụng trên thiết bị này.")
                .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

}
