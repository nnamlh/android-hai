package com.congtyhai.activity.support;

import android.os.Bundle;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;


public class ContactActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        createToolbar();

    }

}
