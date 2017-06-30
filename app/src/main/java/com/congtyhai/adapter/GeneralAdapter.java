package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.receive.GeneralInfo;

import java.util.List;

/**
 * Created by NAM on 12/10/2016.
 */

public class GeneralAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<GeneralInfo> generalInfos;

    private Activity activity;

    public GeneralAdapter(Activity activity, List<GeneralInfo> generalInfos) {

        inflater = activity.getLayoutInflater();

        this.activity = activity;

        this.generalInfos = generalInfos;
    }



    @Override
    public int getCount() {
        return generalInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return generalInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null)
            view = inflater.inflate(R.layout.general_item, null);


        GeneralInfo generalInfo = generalInfos.get(i);

        TextView txtName = (TextView) view.findViewById(R.id.name);
        TextView txtCode = (TextView) view.findViewById(R.id.code);
        TextView txtStatus = (TextView) view.findViewById(R.id.status);


        txtName.setText(generalInfo.getName());
        txtCode.setText(generalInfo.getCode());
        txtStatus.setText(generalInfo.getStatus());


        View hoverView = view.findViewById(R.id.hover_view);

        if (generalInfo.getSuccess() == 0) {
            hoverView.setBackgroundColor(activity.getResources().getColor(R.color.hover_error));
        } else {
            hoverView.setBackgroundColor(activity.getResources().getColor(R.color.bggreen));
        }


        return view;
    }
}
