package com.congtyhai.adapter;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.ActionInfo;
import com.congtyhai.view.BadgeView;

import java.util.List;

/**
 * Created by NAM on 10/11/2016.
 */

public class ActionAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ActionInfo> actionInfos;
private  Activity activity;

    public ActionAdapter(Activity activity, List<ActionInfo> actionInfos) {

        inflater = activity.getLayoutInflater();
this.activity = activity;
        this.actionInfos = actionInfos;

    }

    @Override
    public int getCount() {
        return actionInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return actionInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.list_action_row, null);
        }

        ActionInfo info = actionInfos.get(i);

        TextView title = (TextView) view.findViewById(R.id.title);

        ImageView image = (ImageView) view.findViewById(R.id.image);

        title.setText(info.getTitle());

        image.setImageResource(info.getImage());

        BadgeView badgeView = (BadgeView) view.findViewById(R.id.badge);

        if (info.getCount() == 0) {

            if (info.isFirst()) {
                badgeView.setText("N");
                badgeView.setBackgroundColor(activity.getResources().getColor(R.color.bggreen));
            } else {
                badgeView.setVisibility(View.GONE);
            }

        } else {

            badgeView.setText(info.getCount() + "");
            badgeView.setVisibility(View.VISIBLE);
        }




        return view;
    }
}
