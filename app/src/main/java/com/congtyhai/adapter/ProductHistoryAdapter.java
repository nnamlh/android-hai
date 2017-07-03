package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.data.DHistoryProductScan;
import com.congtyhai.util.HaiSetting;

import java.util.List;

/**
 * Created by HAI on 7/3/2017.
 */

public class ProductHistoryAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<DHistoryProductScan> dHistoryProductScen;

    public ProductHistoryAdapter(Activity activity, List<DHistoryProductScan> productHistory) {
        this.inflater = activity.getLayoutInflater();
        this.dHistoryProductScen = productHistory;
    }

    @Override
    public int getCount() {
        return dHistoryProductScen.size();
    }

    @Override
    public Object getItem(int position) {
        return dHistoryProductScen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.product_history_item, null);
        }

        TextView txtAction  = (TextView) convertView.findViewById(R.id.txtaction);

        TextView txtReceive = (TextView) convertView.findViewById(R.id.txtreceive);

        TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtquantity);

        TextView txtSuccess = (TextView) convertView.findViewById(R.id.txtsuccess);

        TextView txtFail = (TextView) convertView.findViewById(R.id.txtfail);

        DHistoryProductScan history = dHistoryProductScen.get(position);

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

        return convertView;
    }
}
