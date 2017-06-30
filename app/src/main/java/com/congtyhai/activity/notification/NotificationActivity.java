package com.congtyhai.activity.notification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.NotificationAdapter;
import com.congtyhai.model.data.NotificationInfo;
import com.congtyhai.model.send.NotificationRequet;
import com.congtyhai.model.receive.NotificationResult;
import com.congtyhai.model.receive.NotificationResultInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;
import com.congtyhai.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {

    private LoadMoreListView listView;

    private NotificationAdapter adapter;

    private List<NotificationResultInfo> notificationResultInfos;

    private int pageNo = 0;
    private int pageMax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createToolbar();

        listView = (LoadMoreListView) findViewById(R.id.list);

        notificationResultInfos = new ArrayList<>();
        adapter = new NotificationAdapter(this, notificationResultInfos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NotificationResultInfo info = notificationResultInfos.get(i);
                info.setIsRead(1);

                Realm realm = RealmController.getInstance().getRealm();
                final NotificationInfo item = RealmController.getInstance().getNotification(info.getId());
                if (item != null) {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.setRead(1);
                        }
                    });
                }

                Intent intent = new Intent(NotificationActivity.this, NotificationDetailActivity.class);
                intent.putExtra("TITLE", info.getTitle());
                intent.putExtra("CONTENT", info.getContent());
                intent.putExtra("TIME", info.getTime());
                intent.putExtra("IMAGE", info.getImage());
                startActivity(intent);
            }
        });
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                reprare(true);
            }
        });

        reprare(false);

    }

    @Override
    protected  void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void reprare(final boolean fLoadMore) {

        if (fLoadMore){
            if (pageNo >= pageMax){
                ((LoadMoreListView) listView).onLoadMoreComplete();
                return;
            }

        }


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        NotificationRequet info = new NotificationRequet(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, pageNo);

        Call<NotificationResult> call = apiService.notification(info);

        call.enqueue(new Callback<NotificationResult>() {
            @Override
            public void onResponse(Call<NotificationResult> call,
                                   Response<NotificationResult> response) {

              if (response.body() != null) {
                  if ("1".equals(response.body().getId())) {

                      Realm realm = RealmController.getInstance().getRealm();

                      if(response.body().getNotifications().size() == 0) {
                          RealmController.getInstance().clearNotificationAll();
                      }

                      pageMax = response.body().getPagemax();

                      for (final NotificationResultInfo item : response.body().getNotifications()) {
                          if (!fLoadMore) {
                              NotificationInfo dCheck = RealmController.getInstance().getNotification(item.getId());
                              if (dCheck == null) {
                                  realm.executeTransaction(new Realm.Transaction() {
                                      @Override
                                      public void execute(Realm realm) {
                                          NotificationInfo info = realm.createObject(NotificationInfo.class, item.getId());
                                          info.setRead(0);
                                      }
                                  });

                              } else {
                                  if (dCheck.getRead() == 1)
                                      item.setIsRead(1);
                              }
                          } else
                              item.setIsRead(1);

                          notificationResultInfos.add(item);
                      }

                      adapter.notifyDataSetChanged();

                      pageNo++;
                  } else {
                      showAlert("Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      });
                  }
              }

                if (fLoadMore) {
                    adapter.notifyDataSetChanged();
                    ((LoadMoreListView) listView).onLoadMoreComplete();
                }
            }

            @Override
            public void onFailure(Call<NotificationResult> call, Throwable t) {
                showAlert("Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });

    }

}
