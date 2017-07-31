package com.congtyhai.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.congtyhai.activity.checkin.CheckInActivity;
import com.congtyhai.activity.event.EventActivity;
import com.congtyhai.activity.notification.NotificationActivity;
import com.congtyhai.activity.product.ChooseProductFuncActivity;
import com.congtyhai.activity.support.SettingActivity;
import com.congtyhai.adapter.ActionAdapter;
import com.congtyhai.model.ActionInfo;
import com.congtyhai.model.send.FirebaseReg;
import com.congtyhai.model.data.NotificationInfo;
import com.congtyhai.model.receive.ResultTopic;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private ListView listView;
    private ActionAdapter adapter;

    private int countEvent = 0;

    private List<ActionInfo> actionInfos;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView imgBanner;

    private int imagesToShow[] = {R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.banner_3, R.mipmap.banner_4, R.mipmap.banner_5};

    public static final String ACTION_CHECK_IN = "CHECKIN";
    public static final String ACTION_CHECK_STAFF = "CHECKSTAFF";
    public static final String ACTION_PRODUCT = "PRODUCT";
    public static final String ACTION_NOTIFICATION = "NOTIFICATION";
    public static final String ACTION_SETTING = "SUPPORT";
    public static final String ACTION_EVENT = "EVENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        imgBanner = (ImageView) findViewById(R.id.main_banner);

        Random radom = new Random();
        imgBanner.setImageResource(imagesToShow[radom.nextInt(5)]);

        listView = (ListView) findViewById(R.id.list);

        getTopic();

    }


    private void getTopic() {
        showpDialog();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(HaiSetting.getInstance().SHARED_PREF, 0);
        String token = pref.getString("regId",  "");

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        final int needUpdate = needUpdateDaily();

        FirebaseReg info = new FirebaseReg(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, token, needUpdate);

        Call<ResultTopic> call = apiService.updateReg(info);

        call.enqueue(new Callback<ResultTopic>() {
            @Override
            public void onResponse(Call<ResultTopic> call,
                                   Response<ResultTopic> response) {
                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {
                        for (String topic : response.body().getTopics()) {
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
                        }
                        storeTopicInPref(response.body().getTopics());

                        setListMainFunction(response.body().getFunction());

                        if (needUpdate == 1) {
                            HaiSetting.getInstance().clearProductCodeMap();
                            saveListAgency(response.body().getAgencies());
                            saveListReceive(response.body().getRecivers());
                            saveListProduct(response.body().getProducts());

                            updateDaily();
                        }
                    }
                }
                initList(true);
                hidepDialog();
            }

            @Override
            public void onFailure(Call<ResultTopic> call, Throwable t) {
                Log.e("error:", t.getMessage());
                initList(true);
                hidepDialog();
            }
        });
    }

    private void storeTopicInPref(String[] topics) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(HaiSetting.getInstance().SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();

        StringBuilder builder = new StringBuilder();

        for (String topic : topics) {
            builder.append("-" + topic);
        }

        builder.replace(0, 1, "");

        editor.putString("topics", builder.toString());

        editor.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // lam moi lai
        initList(false);
    }


    private void initList(boolean fist) {

        List<NotificationInfo> notificationInfos = RealmController.getInstance().queryedNotification();

        actionInfos = new ArrayList<>();
        try {
            String function = getListMainFunction();

            JSONArray jsonArray = new JSONArray(function);

            for (int i = 0; i < jsonArray.length(); i++) {
                String item = jsonArray.getString(i);
                switch (item) {
                    case "checkin":
                        actionInfos.add(new ActionInfo("Lịch công tác", "Ghé thăm khách hàng", R.mipmap.checkin, ACTION_CHECK_IN, 0));
                        break;
                    case "checkstaff":
                        actionInfos.add(new ActionInfo("Kiểm tra nhân viên", "Kiểm tra thông tin nhân viên HAI", R.mipmap.checkstaff, ACTION_CHECK_STAFF, 0));
                        break;
                    case "product":
                        actionInfos.add(new ActionInfo("Quản lý sản phẩm", "Quản lý sản phẩm của HAI", R.mipmap.packing, ACTION_PRODUCT, 0));
                        break;
                    case "event":
                        actionInfos.add(new ActionInfo("Chương trình khuyến mãi", "Dành cho khách hàng của HAI", R.mipmap.event, ACTION_EVENT, countEvent));
                        break;
                    case "newfeed":
                        ActionInfo actionInfo = new ActionInfo("Thông báo", "Thông báo từ HAI", R.mipmap.newfeed, ACTION_NOTIFICATION, notificationInfos.size());
                        actionInfo.setFirst(fist);
                        actionInfos.add(actionInfo);
                        break;
                }
            }

            actionInfos.add(new ActionInfo("Hổ trợ", "Quản lý ứng dụng", R.mipmap.setting, ACTION_SETTING, 0));

        } catch (Exception e) {

        }

        adapter = new ActionAdapter(this, actionInfos);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String action = actionInfos.get(i).getActiion();
                Intent intent = null;
                switch (action) {
                    case ACTION_CHECK_IN:
                        if (checkLocation()) {
                            intent = new Intent(MainActivity.this, CheckInActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case ACTION_CHECK_STAFF:
                        intent = new Intent(MainActivity.this, CheckStaffActivity.class);
                        startActivity(intent);
                        break;
                    case ACTION_PRODUCT:
                        if (checkLocation()) {
                            intent = new Intent(MainActivity.this, ChooseProductFuncActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case ACTION_EVENT:
                        countEvent = 0;
                        intent = new Intent(MainActivity.this, EventActivity.class);
                        startActivity(intent);
                        break;
                    case ACTION_NOTIFICATION:
                        intent = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case ACTION_SETTING:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Chức năng đang tạm khóa!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
