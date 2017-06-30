package com.congtyhai.activity.checkin;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.CheckinAdapter;
import com.congtyhai.model.send.CheckIn;
import com.congtyhai.model.data.DCheckIn;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiClientUpload;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.RealmController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInHistoryActivity extends BaseActivity {

    private ListView listView;
    private CheckinAdapter adapter;
    private List<DCheckIn> checkIns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_history);
        createToolbar();


        listView = (ListView) findViewById(R.id.list);

        checkIns = RealmController.getInstance().getCheckIns();

        if (checkIns.size() > 15) {
            List<DCheckIn> checkInsLimit = new ArrayList<>();
            for(int i = 0; i < 15; i++) {
                checkInsLimit.add(checkIns.get(i));

            }
            adapter = new CheckinAdapter(this, checkInsLimit);
        }else{
            adapter = new CheckinAdapter(this, checkIns);
        }


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }

    private void uploadImage(final long id, final String tokenString ) {

        File sourceFile = null;
        RequestBody requestFile = null;
        MultipartBody.Part body = null;

        DCheckIn item = RealmController.getInstance().getCheckIn(id);

        if (item.getFilePath() != null) {

            Uri imageUri = Uri.parse(item.getFilePath());

            sourceFile = new File(imageUri.getPath());

            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
            body =
                    MultipartBody.Part.createFormData("image", sourceFile.getName(), requestFile);
        }


        RequestBody user =
                RequestBody.create(
                        MediaType.parse("text/plain"), item.getUser());


        RequestBody token =
                RequestBody.create(
                        MediaType.parse("text/plain"), tokenString);

        RequestBody extension =
                RequestBody.create(
                        MediaType.parse("text/plain"), ".jpg");

        ApiInterface apiService =
                ApiClientUpload.getClient().create(ApiInterface.class);

        Call<ResultInfo> call = apiService.uploadImage(body, user, token, extension);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,final Response<ResultInfo> response) {
                if (response.body().getId().equals("1")) {
                   final DCheckIn item = RealmController.getInstance().getCheckIn(id);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.setStt(1);
                            item.setImage(response.body().getMsg());
                        }
                    });
                    sendInfo(item.getId(), tokenString);
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

            }
        });

    }


    private void sendInfo(final long id, String tokenString) {


        DCheckIn item = RealmController.getInstance().getCheckIn(id);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        CheckIn info = new CheckIn(item.getUser(), tokenString, item.getComment(), item.getImage(), item.getLatitude(), item.getLongitude(),item.getDate(), item.getAgency());

        Call<ResultInfo> call = apiService.checkIn(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   Response<ResultInfo> response) {
                if ("1".equals(response.body().getId())) {

                    final DCheckIn item = RealmController.getInstance().getCheckIn(id);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.setStt(2);
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

            }
        });
    }

}
