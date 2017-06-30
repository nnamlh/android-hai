package com.congtyhai.activity.checkin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.activity.search.ShowAgencyActivity;
import com.congtyhai.activity.util.ShowCameraActivity;
import com.congtyhai.model.send.CheckIn;
import com.congtyhai.model.data.DCheckIn;
import com.congtyhai.model.HaiLocation;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiClientUpload;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInActivity extends BaseActivity {

    private static final String TAG = CheckInActivity.class.getSimpleName();

    ImageView imgTake;
    Button btnTake;
    Button btnSend;
    EditText eContent;
    EditText eAgency;
    ImageView imgAgency;

    // Camera activity request codes
    static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        createToolbar();

        imgTake = (ImageView) findViewById(R.id.image);

        imgTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    Uri imageUri = Uri.parse(filePath);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(imageUri, "image/*");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Không có ảnh để hiển thị!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        eContent = (EditText) findViewById(R.id.content);

        btnTake = (Button) findViewById(R.id.takeimage);

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        btnSend = (Button) findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkin();
            }
        });

        // find agency
        imgAgency = (ImageView) findViewById(R.id.btnfind);
        imgAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckInActivity.this, ShowAgencyActivity.class);
                startActivity(intent);
            }
        });

        eAgency = (EditText) findViewById(R.id.agency);
        HaiSetting.getInstance().setAGENCY("");
        HaiSetting.getInstance().setAGENCYNAME("");
    }

    private void checkin() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(CheckInActivity.this);
        dialog.setTitle("Cập nhật")
                .setMessage("Cập nhật thông tin checkin.")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (checkLocation()) {
                            if (filePath != null) {
                                if (TextUtils.isEmpty(eContent.getText().toString()) || TextUtils.isEmpty(eAgency.getText().toString()))
                                    Toast.makeText(CheckInActivity.this, "Nhập đầy đủ thông tin.", Toast.LENGTH_LONG).show();
                                else
                                    uploadImage();
                            } else
                                Toast.makeText(getApplicationContext(), "Chụp ảnh.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void checkinFail(final int stt, final String image) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(CheckInActivity.this);
        dialog.setTitle("Thông báo")
                .setMessage("Lỗi không thể cập nhật do mất mạng.")
                .setPositiveButton("Lưu lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        saveCheckIn(stt, image);
                    }
                })
                .setNegativeButton("Thôi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void uploadImage() {

        showpDialog();

        File sourceFile = null;
        RequestBody requestFile = null;
        MultipartBody.Part body = null;
        if (filePath != null) {
            Uri imageUri = Uri.parse(filePath);
            sourceFile = new File(imageUri.getPath());

            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
            body =
                    MultipartBody.Part.createFormData("image", sourceFile.getName(), requestFile);

        }


        RequestBody user =
                RequestBody.create(
                        MediaType.parse("text/plain"), HaiSetting.getInstance().USER);


        RequestBody token =
                RequestBody.create(
                        MediaType.parse("text/plain"), HaiSetting.getInstance().TOKEN);

        RequestBody extension =
                RequestBody.create(
                        MediaType.parse("text/plain"), ".jpg");

        ApiInterface apiService =
                ApiClientUpload.getClient().create(ApiInterface.class);

        Call<ResultInfo> call = apiService.uploadImage(body, user, token, extension);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                if (response.body()!= null) {
                    if (response.body().getId().equals("1")) {
                        sendInfo(response.body().getMsg());
                    } else {
                        Toast.makeText(CheckInActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        hidepDialog();
                    }
                } else{
                    checkinFail(0, "");

                    hidepDialog();
                }

            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {
                checkinFail(0, "");

                hidepDialog();
            }
        });

    }


    private void saveCheckIn(final int stt, final String image) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DCheckIn checkInSave = realm.createObject(DCheckIn.class, RealmController.getInstance().getNextKey(DCheckIn.class));
                checkInSave.setComment(eContent.getText().toString());
                checkInSave.setUser(HaiSetting.getInstance().USER);
                checkInSave.setFilePath(filePath);
                HaiLocation location = getCurrentLocation();
                checkInSave.setLatitude(location.getLatitude());
                checkInSave.setLongitude(location.getLongitude());
                checkInSave.setAgency(eAgency.getText().toString());
                String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm",
                        Locale.getDefault()).format(new Date());
                checkInSave.setDate(timeStamp);
                checkInSave.setImage(image);
                checkInSave.setStt(stt);
            }
        });
        eContent.setText("");
        eAgency.setText("");

        imgTake.setImageResource(R.mipmap.image_checkin);
        filePath = null;
        Toast.makeText(CheckInActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();

    }

    private void sendInfo(final String urlImage) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        SimpleDateFormat ft =
                new SimpleDateFormat("MM/dd/yyyy HH:mm");


        HaiLocation location = getCurrentLocation();

        CheckIn info = new CheckIn(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, eContent.getText().toString(), urlImage, location.getLatitude(), location.getLongitude(), ft.format(d), eAgency.getText().toString());

        Call<ResultInfo> call = apiService.checkIn(info);

        call.enqueue(new Callback<ResultInfo>()

                     {
                         @Override
                         public void onResponse(Call<ResultInfo> call,
                                                Response<ResultInfo> response) {

                             if(response.body() != null) {
                                 if ("1".equals(response.body().getId())) {
                                     showAlert("Thông báo", "Đã gửi dữ liệu lên server.", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                             saveCheckIn(2, urlImage);
                                         }
                                     });

                                 } else {
                                     showAlert("Thông báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {

                                         }
                                     });
                                 }
                             } else {
                                 checkinFail(1, urlImage);
                             }


                             hidepDialog();
                         }

                         @Override
                         public void onFailure(Call<ResultInfo> call, Throwable t) {
                             checkinFail(1, urlImage);
                             hidepDialog();
                         }
                     }

        );
    }





    @Override
    protected void onResume() {
        super.onResume();
        eAgency.setText(HaiSetting.getInstance().getAGENCY());

    }

    // camere
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {


        Intent intent = new Intent(CheckInActivity.this, ShowCameraActivity.class);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia() {
        Uri imageUri = Uri.parse(filePath);
        File file = new File(imageUri.getPath());

        try {
            InputStream ims = new FileInputStream(file);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            final Bitmap bitmap = BitmapFactory.decodeStream(ims, null, options);

            imgTake.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                filePath = data.getStringExtra("result");
                previewMedia();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checkin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_history:
                Intent intent = new Intent(CheckInActivity.this, CheckInHistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
