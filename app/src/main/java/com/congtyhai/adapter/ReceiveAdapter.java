package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.receive.ReceiveInfo;

import java.util.List;

/**
 * Created by HAI on 6/30/2017.
 */

public class ReceiveAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ReceiveInfo> receiveInfos;

    public ReceiveAdapter(Activity activity, List<ReceiveInfo> receiveInfos) {
        inflater = activity.getLayoutInflater();
        this.receiveInfos = receiveInfos;
    }


    @Override
    public int getCount() {
        return receiveInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return receiveInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.item_agency, null);
        }

        TextView code = (TextView) view.findViewById(R.id.code);
        TextView name = (TextView) view.findViewById(R.id.name);

        ReceiveInfo info = receiveInfos.get(i);

        code.setText(info.getCode());
        name.setText(info.getName());

        return view;
    }
}

