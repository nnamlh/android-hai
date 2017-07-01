package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.HaiLocation;
import com.congtyhai.model.receive.ProductCodeInfo;
import com.congtyhai.util.HaiSetting;

import java.util.List;

/**
 * Created by HAI on 7/1/2017.
 */

public class ProductManageAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> product;

    public ProductManageAdapter(Activity activity, List<String> product) {
        this.inflater = activity.getLayoutInflater();
        this.product = product;
    }


    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int position) {
        return product.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_manage_list_item, null);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.txtname);
        TextView txtCode = (TextView) convertView.findViewById(R.id.txtcode);

        String barcode = product.get(position);

        String code = barcode.substring(8, 10);

        txtCode.setText(barcode);
        txtName.setText(HaiSetting.getInstance().findProductNameByCode(code));

        return convertView;
    }
}
