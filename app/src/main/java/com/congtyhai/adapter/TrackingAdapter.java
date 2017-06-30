package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.congtyhai.activity.R;
import com.congtyhai.model.receive.TrackingInfo;

import java.util.List;

/**
 * Created by NAM on 11/19/2016.
 */

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.MyViewHolder> {

    private Context mContext;
    private List<TrackingInfo> trackingInfos;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, importTime, exportTime, quantity;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tname);
            importTime = (TextView) view.findViewById(R.id.importTime);
            exportTime = (TextView) view.findViewById(R.id.exportTime);
            quantity = (TextView) view.findViewById(R.id.quantity);
        }
    }

    public TrackingAdapter(Context mContext, List<TrackingInfo> trackingInfos) {
        this.mContext = mContext;
        this.trackingInfos = trackingInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracking_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        TrackingInfo info = trackingInfos.get(position);

        holder.title.setText(info.getName());

        holder.importTime.setText(info.getImportTime());
        holder.exportTime.setText(info.getExportTime());

        holder.quantity.setText(info.getStatus());

    }

    @Override
    public int getItemCount() {
        return trackingInfos.size();
    }
}
