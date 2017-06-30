package com.congtyhai.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.congtyhai.activity.R;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScanActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.simple_scan);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);


        Intent intent = getIntent();

        String keyType = intent.getStringExtra("ScreenKey");

        if (keyType == "staff") {
            setupFormatsStaff();
        } else if (keyType == "event")
            setupFormats();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();

        formats.add(new BarcodeFormat(39, "CODE39"));
        formats.add(new BarcodeFormat(128, "CODE128"));
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    public void setupFormatsStaff() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();

        formats.add(new BarcodeFormat(64, "QRCODE"));

        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
      /*  Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();*/
        Intent intent = new Intent();
        intent.putExtra("Content", rawResult.getContents());
        setResult(Activity.RESULT_OK, intent);

        finish();

    }
}