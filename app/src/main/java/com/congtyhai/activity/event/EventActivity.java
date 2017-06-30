package com.congtyhai.activity.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.EventAdapter;
import com.congtyhai.model.send.AuthInfo;
import com.congtyhai.model.receive.ResultEvent;
import com.congtyhai.model.receive.ResultEventInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RecyclerTouchListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        createToolbar();
        // dialog
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        HaiSetting.getInstance().clearListEvent();
        adapter = new EventAdapter(this, HaiSetting.getInstance().getResultEventInfos());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ResultEventInfo info =  HaiSetting.getInstance().getResultEventInfos().get(position);

                Intent i = new Intent(EventActivity.this, EventDetailActivity.class);

                i.putExtra("EventID", info.getEid());

                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                            getInfo();
                                    }
                                }
        );

    }
    @Override
    public void onRefresh() {
        HaiSetting.getInstance().clearListEvent();
        adapter = new EventAdapter(this, HaiSetting.getInstance().getResultEventInfos());
        recyclerView.setAdapter(adapter);
        getInfo();
    }

    private void getInfo() {

       // showDialog();
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        AuthInfo info = new AuthInfo(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN);

        Call<ResultEvent> call = apiService.loyaltyEvent(info);

        call.enqueue(new Callback<ResultEvent>() {
            @Override
            public void onResponse(Call<ResultEvent> call,
                                   Response<ResultEvent> response) {

               if(response.body() != null) {
                   if ("1".equals(response.body().getId())) {

                       for (ResultEventInfo info: response.body().getEvents()) {
                           HaiSetting.getInstance().addListEvent(info);
                       }

                       adapter.notifyDataSetChanged();

                   } else {
                       showAlert("Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                           }
                       });
                   }
               }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResultEvent> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showAlert("Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }
}
