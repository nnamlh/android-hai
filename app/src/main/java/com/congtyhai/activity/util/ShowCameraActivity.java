package com.congtyhai.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.congtyhai.activity.R;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowCameraActivity extends AppCompatActivity {

    private static final String TAG = "ShowCamera";

    @BindView(R.id.camera)
    CameraView cameraView;

    @BindView(R.id.take_picture)
    FloatingActionButton fab;

    String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_camera);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.captureImage();
            }
        });

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);
                Intent returnIntent = new Intent();

                boolean isSave = saveBitmapToFile(picture);

                if (isSave) {
                    returnIntent.putExtra("result", filePath);
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }

                finish();

            }
        });
    }

    public boolean saveBitmapToFile(byte[] picture) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 8;
            // factor of downsizing the image
            ByteArrayInputStream buffer = new ByteArrayInputStream(picture);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(buffer, null, o);
            buffer.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            buffer = new ByteArrayInputStream(picture);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(buffer, null, o2);
            buffer.close();

            // here i override the original image file
            File file = getOutputMediaFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            selectedBitmap.recycle();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "NDH");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;

        String imageFileName = "hai_" + timeStamp;
        try {
            mediaFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    mediaStorageDir
            );
        } catch (IOException e) {
            return null;
        }
        filePath = "file:" + mediaFile.getAbsolutePath();
        return mediaFile;

    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraView.stop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}


