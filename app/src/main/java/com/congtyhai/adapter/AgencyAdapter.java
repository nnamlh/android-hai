package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.receive.AgencyInfo;

import java.util.List;

/**
 * Created by NAM on 11/11/2016.
 */

public class AgencyAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<AgencyInfo> agencyInfos;

    public AgencyAdapter(Activity activity, List<AgencyInfo> agencyInfos) {
        inflater = activity.getLayoutInflater();
        this.agencyInfos = agencyInfos;
    }


    @Override
    public int getCount() {
        return agencyInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return agencyInfos.get(i);
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

        AgencyInfo info = agencyInfos.get(i);

        code.setText(info.getCode());
        name.setText(info.getName());

        return view;
    }
}
