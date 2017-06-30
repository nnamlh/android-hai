package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.receive.NotificationResultInfo;

import java.util.List;

/**
 * Created by NAM on 11/2/2016.
 */

public class NotificationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<NotificationResultInfo> infos;
    private Activity activity;


    public NotificationAdapter(Activity activity, List<NotificationResultInfo> infos) {
        inflater = activity.getLayoutInflater();
        this.activity = activity;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.notification_item, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView messenge = (TextView) view.findViewById(R.id.messenge);

        TextView time = (TextView) view.findViewById(R.id.time);

        NotificationResultInfo info = infos.get(i);

        title.setText(info.getTitle().toUpperCase());
        if (info.getContent().length() > 100) {
            messenge.setText(info.getContent().substring(0, 100) + "...");
        } else
            messenge.setText(info.getContent());

        time.setText(info.getTime());

        if (info.getIsRead() == 0) {
            view.setBackgroundColor(activity.getResources().getColor(R.color.hover_notification));
        } else {
            view.setBackgroundColor(activity.getResources().getColor(R.color.no_hover_notification));
        }




        return view;
    }
}
