package com.congtyhai.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.congtyhai.activity.BaseActivity;
import com.congtyhai.activity.R;
import com.congtyhai.adapter.GeneralAdapter;
import com.congtyhai.model.data.DHistoryProductScan;
import com.congtyhai.model.receive.AgencyInfo;
import com.congtyhai.model.receive.GeneralInfo;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductHistoryDetailActivity extends BaseActivity {

    @BindView(R.id.txtaction)
    TextView txtAction;

    @BindView(R.id.txtreceive)
    TextView txtReceive;

    @BindView(R.id.txtquantity)
    TextView txtQuantity;

    @BindView(R.id.txtsuccess)
    TextView txtSuccess;

    @BindView(R.id.txtfail)
    TextView txtFail;

    @BindView(R.id.list)
    ListView listView;

    GeneralAdapter adapter;

    List<GeneralInfo> generalInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history_detail);
        createToolbar();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        long id = intent.getLongExtra("HistoryId", 0);

        DHistoryProductScan history = RealmController.getInstance().queryHistoryProductScan(id);

        txtAction.setText(getTitleScreen(history.getScreen()) + " (" + history.getTime() + ")" );

        if(history.getScreen().equals(HaiSetting.getInstance().PRODUCT_TRANSPORT) || history.getScreen().equals(HaiSetting.getInstance().PRODUCT_EXPORT)) {
            txtReceive.setText("Kho nhận: " + history.getReceive());
        } else if (history.getScreen().equals(HaiSetting.getInstance().PRODUCT_HELP_SCAN)) {
            txtReceive.setText("Nhập cho đại lý: " + history.getAgency());
        } else {
            txtReceive.setVisibility(View.GONE);
        }

        txtQuantity.setText("TL : " + history.getQuantity());

        txtSuccess.setText(history.getCountSuccess());

        txtFail.setText(history.getCountFail());


        //
       try{
           Gson gson = new Gson();
           Type listType = new TypeToken<List<GeneralInfo>>() {
           }.getType();
           generalInfos = gson.fromJson(history.getProductResult(), listType);

           adapter = new GeneralAdapter(this, generalInfos);
           listView.setAdapter(adapter);

       }catch (Exception e) {

       }


    }

    private String getTitleScreen(String screen) {
        if (screen.equals(HaiSetting.getInstance().PRODUCT_IMPORT)) {
            return  "Nhập kho";
        } else if (screen.equals(HaiSetting.getInstance().PRODUCT_EXPORT)) {
            return "Xuất kho";

        } else if (screen.equals(HaiSetting.getInstance().PRODUCT_HELP_SCAN)) {
            return "Nhập giúp";
        } else if (screen.equals(HaiSetting.getInstance().PRODUCT_TRANSPORT)){
            return "Điều kho";
        }
        return "";
    }
}
