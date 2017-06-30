package com.congtyhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.ProductChooseFuncInfo;

import java.util.List;


/**
 * Created by computer on 6/21/2017.
 */

public class ProductChooseFuncAdapter extends RecyclerView.Adapter<ProductChooseFuncAdapter.MyViewHolder> {

    List<ProductChooseFuncInfo> arr;
    Context mContext;

    public ProductChooseFuncAdapter(List<ProductChooseFuncInfo> arr, Context mContext) {
        this.arr = arr;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_choose_func_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductChooseFuncInfo info = arr.get(position);

        holder.title.setText(info.getTitle());
        holder.imageView.setImageResource(info.getImage());

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.image);
        }
    }


}
