package com.congtyhai.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.congtyhai.activity.R;
import com.congtyhai.model.data.DCheckIn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by NAM on 11/10/2016.
 */

public class CheckinAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DCheckIn> checkIns;

    public CheckinAdapter(Activity activity, List<DCheckIn> checkIns) {
        inflater = activity.getLayoutInflater();
        this.checkIns = checkIns;
    }


    @Override
    public int getCount() {
        return checkIns.size();
    }

    @Override
    public Object getItem(int i) {
        return checkIns.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.item_checkin, null);
        }

        TextView content = (TextView) view.findViewById(R.id.checkcontent);
        TextView time = (TextView) view.findViewById(R.id.checktime);
        TextView stt = (TextView) view.findViewById(R.id.checkstt);

        ImageView image = (ImageView) view.findViewById(R.id.checkimage);


        DCheckIn checkIn = checkIns.get(getCount() - 1 - i);

        Uri imageUri = Uri.parse(checkIn.getFilePath());
        File file = new File(imageUri.getPath());
        try {
            InputStream ims = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeStream(ims, null, options);

            image.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {

        }

        content.setText(checkIn.getComment());
        time.setText(checkIn.getDate());
        if (checkIn.getStt() == 2) {
            stt.setText("Đã cập nhật thành công!");
        } else {
            stt.setText("Chưa cập nhật hoàn thành!");
        }

        TextView agency = (TextView) view.findViewById(R.id.checkagency);
        agency.setText(checkIn.getAgency());

        return view;
    }
}
