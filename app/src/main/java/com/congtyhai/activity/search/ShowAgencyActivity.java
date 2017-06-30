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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.AgencyAdapter;
import com.congtyhai.model.receive.AgencyInfo;
import com.congtyhai.util.HaiSetting;

import java.util.ArrayList;
import java.util.List;

public class ShowAgencyActivity extends BaseActivity {

    private String TAG = ShowAgencyActivity.class.getName();

    private ListView listView;

    private AgencyAdapter adapter;

    private List<AgencyInfo> agencyInfos;

    private List<AgencyInfo> agencyInfosSearch;

    private EditText editText;

    private ImageView btnSearch;

    private RadioGroup radioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_agency);
        createToolbar();

        listView = (ListView) findViewById(R.id.list_view);

        initList();


        adapter = new AgencyAdapter(this, agencyInfosSearch);
        listView.setAdapter(adapter);


        editText = (EditText) findViewById(R.id.inputSearch);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_NULL) {
                    //makeJsonRequest();

                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                agencyInfosSearch.clear();
                String stt = chooseStt();
                for (AgencyInfo info : agencyInfos) {
                    if (stt.equals("1") && info.getType().equals("CI")) {
                        if (info.getCode().contains(cs)){
                            agencyInfosSearch.add(info);
                        }
                    } else if (stt.equals("2") && info.getType().equals("CII")){
                        if(info.getCode().contains(cs)) {
                            agencyInfosSearch.add(info);
                        }
                    }
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

        radioGroup = (RadioGroup) findViewById(R.id.toggle);


        btnSearch = (ImageView) findViewById(R.id.btnsearch);
        btnSearch.setVisibility(View.GONE);
        /*
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString()))
                    Toast.makeText(ShowAgencyActivity.this, "Gỏ tên cần tìm.", Toast.LENGTH_SHORT).show();
                else
                    makeJsonRequest();
            }
        });*/

    }

    private void initList() {
        agencyInfos = new ArrayList<>();
        agencyInfosSearch = new ArrayList<>();
        new ReadDataTask().execute();
    }



    private String chooseStt() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.btnc1:
                return "1";
            case R.id.btnc2:
                return "2";
            default:
                return "";
        }

    }

    /*
    private void makeJsonRequest() {
        showpDialog();
        agencyInfos.clear();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String type = chooseStt();

        AgencyRequest info = new AgencyRequest(HaiSetting.getInstance().USER, HaiSetting.getInstance().TOKEN, type, editText.getText().toString());

        Call<AgencyResult> call = apiService.findAgency(info);

        call.enqueue(new Callback<AgencyResult>() {
            @Override
            public void onResponse(Call<AgencyResult> call,
                                   retrofit2.Response<AgencyResult> response) {

                if (response.body() != null) {
                    if ("1".equals(response.body().getId())) {

                        for (AgencyInfo item : response.body().getAgences()) {
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
            public void onFailure(Call<AgencyResult> call, Throwable t) {
                hidepDialog();
                showAlert("Cảnh báo", "Đường truyền bị lỗi, kiểm tra lại kết nối wifi hoặc 3G.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }
    */


    private class ReadDataTask extends AsyncTask<String, Integer, List<AgencyInfo>> {
        protected List<AgencyInfo> doInBackground(String... urls) {

            List<AgencyInfo> data = new ArrayList<>();
            try{

                data = getListAgency();

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

}
