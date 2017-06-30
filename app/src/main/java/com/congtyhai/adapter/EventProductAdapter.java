package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.receive.EventProduct;

import java.util.List;

/**
 * Created by NAM on 11/16/2016.
 */

public class EventProductAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<EventProduct> eventProducts;

    public EventProductAdapter(Activity activity, List<EventProduct> eventProducts) {
        this.eventProducts = eventProducts;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return eventProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return eventProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.product_event_item, null);
        }

        TextView txtName = (TextView) view.findViewById(R.id.name);
        TextView txtPoint = (TextView) view.findViewById(R.id.point);

        txtName.setText(eventProducts.get(i).getName());
        txtPoint.setText(eventProducts.get(i).getPoint() + " Điểm");

        return view;
    }
}
