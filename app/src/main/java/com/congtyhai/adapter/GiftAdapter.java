package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.congtyhai.activity.R;
import com.congtyhai.model.receive.AwardInfo;

import java.util.List;

/**
 * Created by NAM on 11/13/2016.
 */

public class GiftAdapter extends BaseAdapter {

    private List<AwardInfo> giftInfos;
    private LayoutInflater inflater;

    private Activity activity;

    public GiftAdapter(Activity activity, List<AwardInfo> giftInfos) {
        inflater = activity.getLayoutInflater();
        this.giftInfos = giftInfos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return giftInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return giftInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.gift_item, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView point = (TextView) view.findViewById(R.id.point);

        AwardInfo info = giftInfos.get(i);
        Glide.with(activity).load(info.getImage()).into(image);

        name.setText(info.getName());
        point.setText(info.getPoint() + " ĐIỂM");

        return view;
    }
}
