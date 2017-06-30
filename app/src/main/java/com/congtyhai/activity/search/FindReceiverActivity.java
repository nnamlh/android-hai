package com.congtyhai.activity.search;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.model.receive.AgencyInfo;
import com.congtyhai.model.send.ReceiverRequest;
import com.congtyhai.model.receive.ReceiverResult;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class FindReceiverActivity extends BaseActivity {

    String TAG = FindReceiverActivity.class.getName();

    @BindView(R.id.list_view)
    ListView listView;

    AgencyAdapter adapter;
    List<AgencyInfo> agencyInfos;
    List<AgencyInfo> agencyInfosSearch;

    @BindView(R.id.inputSearch)
    EditText editText;

    @BindView(R.id.btnsearch)
    ImageView btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_receiver);
        createToolbar();

        ButterKnife.bind(this);

        initList();

        adapter = new AgencyAdapter(this, agencyInfos);
        listView.setAdapter(adapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_NULL) {
                    makeJsonRequest();

                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                agencyInfosSearch.clear();
                for (AgencyInfo info : agencyInfos) {
                    if (info.getCode().contains(cs))
                        agencyInfosSearch.add(info);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AgencyInfo info = agencyInfos.get(i);
                showAlert("CHỌN", "Chọn đại lý : " + info.getCode(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HaiSetting.getInstance().setAGENCY(info.getCode());
                        HaiSetting.getInstance().setAGENCYNAME(info.getName());
                        onBackPressed();
                    }
                });
            }
        });

        btnSearch.setVisibility(View.GONE);

        /*
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString()))
                    Toast.makeText(FindReceiverActivity.this, "Gỏ tên cần tìm.", Toast.LENGTH_SHORT).show();
              //  else
                  //  makeJsonRequest();
            }
        });*/

    }

    private void initList() {
        agencyInfos = new ArrayList<>();
        agencyInfosSearch = new ArrayList<>();
        new ReadDataTask().execute();
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyInfo>> {
        protected List<AgencyInfo> doInBackground(String... urls) {

            List<AgencyInfo> data = new ArrayList<>();
            try {

                data = getListReceive();

            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            showpDialog();
        }

        protected void onPostExecute(List<AgencyInfo> result) {
            agencyInfos = result;
            hidepDialog();
        }
    }

    private void makeJsonRequest() {
        showpDialog();
        agencyInfos.clear();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        ReceiverRequest info = new ReceiverRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, editText.getText().toString());

        Call<ReceiverResult> call = apiService.findReceiver(info);

        call.enqueue(new Callback<ReceiverResult>() {
            @Override
            public void onResponse(Call<ReceiverResult> call,
                                   retrofit2.Response<ReceiverResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        for (AgencyInfo item : response.body().getReceivers()) {
                            agencyInfos.add(item);
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

                hidepDialog();
            }

            @Override
            public void onFailure(Call<ReceiverResult> call, Throwable t) {
                hidepDialog();
                showAlert("Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }

}
