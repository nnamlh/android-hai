package com.congtyhai.activity.product;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.util.HaiSetting;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ProductActivity extends BaseActivity implements ZBarScannerView.ResultHandler {
    private static final String TAG = ProductActivity.class.getSimpleName();

    private TextView txtCount;

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
       createToolbar();

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.barcode_scanner);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);
        setupFormats();

        txtCount = (TextView) findViewById(R.id.txtcount);
        txtCount.setText("" + HaiSetting.getInstance().countListProduct());


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
        if (rawResult.getContents() == null || HaiSetting.getInstance().getLIST_PRODUCT().contains(rawResult.getContents())) {
            Toast.makeText(getApplicationContext(), "Đã quét.", Toast.LENGTH_SHORT).show();
        } else if (rawResult.getContents().length() < 17) {
            Toast.makeText(getApplicationContext(), "Mã không hợp lệ.", Toast.LENGTH_SHORT).show();
        }
        else {
            HaiSetting.getInstance().addListProduct(rawResult.getContents());
            txtCount.setText("" + HaiSetting.getInstance().countListProduct());
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ProductActivity.this);
            }
        }, 500);
    }
}
