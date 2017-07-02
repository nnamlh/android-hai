package com.congtyhai.activity.product;

import android.os.Bundle;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;

public class ProductHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        createToolbar();
    }
}
