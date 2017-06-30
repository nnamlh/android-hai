package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.congtyhai.activity.R;
import com.congtyhai.model.receive.ResultEventInfo;

import java.util.List;

/**
 * Created by NAM on 11/13/2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {


    private Context mContext;
    private List<ResultEventInfo> eventInfos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            overflow.setVisibility(View.GONE);
        }
    }


    public EventAdapter(Context mContext, List<ResultEventInfo> eventInfos) {
        this.mContext = mContext;
        this.eventInfos = eventInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ResultEventInfo info = eventInfos.get(position);

        holder.title.setText(info.getEname());
        holder.time.setText(info.getEtime());

        // loading album cover using Glide library
        Glide.with(mContext).load(info.getEimage()).into(holder.thumbnail);
        // holder.thumbnail.setImageResource(info.getImage());
        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_event, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_bookmark:
                    Toast.makeText(mContext, "Thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play:
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return eventInfos.size();
    }

}
