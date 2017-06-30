package com.congtyhai.activity.event;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.GeneralAdapter;
import com.congtyhai.util.HaiSetting;

public class SendCodeResultActivity extends BaseActivity {
    private GeneralAdapter adapterResult;
    private ListView listView;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code_result);
        createToolbar();
        listView = (ListView) findViewById(R.id.list);
        adapterResult = new GeneralAdapter(SendCodeResultActivity.this, HaiSetting.getInstance().getEventCodeResult());
        listView.setAdapter(adapterResult);


        txtResult = (TextView) findViewById(R.id.txtresult);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        txtResult.setText(msg);
    }
}
