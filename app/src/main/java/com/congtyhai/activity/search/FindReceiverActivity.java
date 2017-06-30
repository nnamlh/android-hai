package com.congtyhai.activity.search;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.ReceiveAdapter;
import com.congtyhai.model.receive.ReceiveInfo;
import com.congtyhai.util.HaiSetting;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FindReceiverActivity extends BaseActivity {

    String TAG = FindReceiverActivity.class.getName();

    @BindView(R.id.list_view)
    ListView listView;

    ReceiveAdapter adapter;
    List<ReceiveInfo> receiveInfos;
    List<ReceiveInfo> receiveInfosSearch;

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

        adapter = new ReceiveAdapter(this, receiveInfosSearch);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                receiveInfosSearch.clear();
                for (ReceiveInfo info : receiveInfos) {
                    if (info.getCode().contains(cs))
                        receiveInfosSearch.add(info);
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
                final ReceiveInfo info = receiveInfos.get(i);
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
        receiveInfos = new ArrayList<>();
        receiveInfosSearch = new ArrayList<>();
        new ReadDataTask().execute();
    }

    private class ReadDataTask extends AsyncTask<String, Integer, List<ReceiveInfo>> {
        protected List<ReceiveInfo> doInBackground(String... urls) {

            List<ReceiveInfo> data = new ArrayList<>();
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

        protected void onPostExecute(List<ReceiveInfo> result) {
            receiveInfos  = result;
            hidepDialog();
        }
    }

}
