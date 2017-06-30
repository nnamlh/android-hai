package com.congtyhai.activity.event;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.view.BadgeView;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanEventCodeActivity extends BaseActivity implements ZBarScannerView.ResultHandler {

    private static final String TAG = ScanEventCodeActivity.class.getSimpleName();


    private BadgeView txtCount;

    private int count = 0;
    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_event_code);

        createToolbar();

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.barcode_scanner);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);
        setupFormats();
        txtCount = (BadgeView) findViewById(R.id.txtcount);
        txtCount.setText(count + "");

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();

        formats.add(new BarcodeFormat(39, "CODE39"));
        formats.add(new BarcodeFormat(128, "CODE128"));
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        if (rawResult.getContents() != null && rawResult.getContents().length() < 17) {
            Toast.makeText(getApplicationContext(), "Không hợp lệ.", Toast.LENGTH_SHORT).show();
        } else if (!HaiSetting.getInstance().addToEventCode(rawResult.getContents())) {
            Toast.makeText(getApplicationContext(), "Đã quét.", Toast.LENGTH_SHORT).show();
        } else {
            count++;
            txtCount.setText(count + "");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanEventCodeActivity.this);
            }
        }, 2000);
    }
}
