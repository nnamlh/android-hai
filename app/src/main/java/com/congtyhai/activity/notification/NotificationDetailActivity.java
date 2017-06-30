package com.congtyhai.activity.notification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;


public class NotificationDetailActivity extends BaseActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
       createToolbar();

        TextView title = (TextView) findViewById(R.id.title);
        TextView content = (TextView) findViewById(R.id.content);
        TextView time = (TextView) findViewById(R.id.time);
        final ImageView imageView = (ImageView) findViewById(R.id.attachimage);

        Intent intent = getIntent();

        title.setText(intent.getStringExtra("TITLE").toUpperCase());
        content.setText(intent.getStringExtra("CONTENT"));
        time.setText(intent.getStringExtra("TIME"));
        String url = intent.getStringExtra("IMAGE");

        Glide.with(getApplicationContext()).load(url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

}
