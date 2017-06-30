package com.congtyhai.activity.support;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.MsgToHaiAdapter;
import com.congtyhai.model.data.DMsgToHai;
import com.congtyhai.model.send.MsgToHai;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgToHaiActivity extends BaseActivity {

    private RadioGroup radioGroup;
    private Button btnSend;
    private EditText eContent;
    private ListView listView;
    private MsgToHaiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_to_hai);
        createToolbar();


        listView = (ListView) findViewById(R.id.listmsg);

        eContent = (EditText) findViewById(R.id.content);
        btnSend = (Button) findViewById(R.id.send);
        radioGroup = (RadioGroup) findViewById(R.id.toggle);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Gửi yêu cầu", "Nội dung yêu cầu sẽ được gửi tới HAI.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(eContent.getText().toString())) {
                            showAlert("Cảnh báo", "Nhập nội dung gửi đi.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                        } else
                            sendInfo();
                    }
                });
            }
        });
        refeshList();
    }

    private String chooseStt() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.btnspport:
                return "sp";
            case R.id.btntrouble:
                return "sc";
            default:
                return "";
        }

    }


    private void refeshList() {
        realm.beginTransaction();
        List<DMsgToHai> msgToHais = RealmController.getInstance().getAllMsg();
        adapter = new MsgToHaiAdapter(this, msgToHais);
        listView.setAdapter(adapter);
        realm.cancelTransaction();
    }

    private void sendInfo() {

        showpDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        final MsgToHai info = new MsgToHai();
        info.setToken(HaiSetting.getInstance().TOKEN);
        info.setUser(HaiSetting.getInstance().USER);
        info.setContent(eContent.getText().toString());
        info.setType(chooseStt());

        Call<ResultInfo> call = apiService.msgToHai(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   Response<ResultInfo> response) {
                hidepDialog();
              if (response.body() != null) {
                  if ("1".equals(response.body().getId())) {

                      Calendar cal = Calendar.getInstance();
                      final Date d = cal.getTime();
                     final SimpleDateFormat ft =
                              new SimpleDateFormat("MM/dd/yyyy HH:mm");

                      realm.executeTransaction(new Realm.Transaction() {
                          @Override
                          public void execute(Realm realm) {
                              DMsgToHai saveData = realm.createObject(DMsgToHai.class, RealmController.getInstance().getNextKey(DMsgToHai.class));
                              saveData.setType(chooseStt());
                              saveData.setContent(info.getContent());
                              saveData.setUser(info.getUser());
                              saveData.setDate(ft.format(d));
                          }
                      });

                      showAlert("Gửi thành công", "Cảm ơn yêu cầu từ quý khách, HAI sẽ phản hồi lại sớm nhất.", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              eContent.setText("");
                              refeshList();
                          }
                      });

                  } else {
                      showAlert("Cảnh báo", response.body().getMsg(), new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      });
                  }
              }


            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

                hidepDialog();
                showAlert("Cảnh báo", "Đường truyền của bạn bị lỗi, kiểm tra lại wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

}
