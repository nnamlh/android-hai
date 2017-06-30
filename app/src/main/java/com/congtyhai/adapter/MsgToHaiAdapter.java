package com.congtyhai.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.data.DMsgToHai;

import java.util.List;

/**
 * Created by NAM on 11/11/2016.
 */

public class MsgToHaiAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<DMsgToHai> msgToHais;

    public MsgToHaiAdapter(Activity activity, List<DMsgToHai> meDMsgToHais) {
        inflater = activity.getLayoutInflater();
        this.msgToHais = meDMsgToHais;
    }


    @Override
    public int getCount() {
        return msgToHais.size();
    }

    @Override
    public Object getItem(int i) {
        return msgToHais.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.item_msg_to_hai, null);
        }

        TextView txtType = (TextView) view.findViewById(R.id.item_type);
        TextView txtContent = (TextView) view.findViewById(R.id.item_content);
        TextView txtDate = (TextView)view.findViewById(R.id.item_date);

        DMsgToHai info  = msgToHais.get(getCount() - 1 - i);

        txtType.setText(info.getType());
        txtContent.setText(info.getContent());
        txtDate.setText(info.getDate());


        return view;
    }
}
