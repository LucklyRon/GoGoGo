package com.dreamingfly.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamingfly.R;
import com.dreamingfly.entry.Information;
import com.lidroid.xutils.BitmapUtils;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Information> data;
    private static LayoutInflater inflater = null;
    BitmapUtils bitmapUtils;

    public LazyAdapter(Activity a, ArrayList<Information> info) {
        activity = a;
        data = info;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bitmapUtils = new BitmapUtils(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_raw, null);

        TextView name = (TextView) vi.findViewById(R.id.name); // name
        TextView age = (TextView) vi.findViewById(R.id.age); // age
        TextView sex = (TextView) vi.findViewById(R.id.sex); // sex
        TextView day = (TextView) vi.findViewById(R.id.day); // day
        TextView description = (TextView) vi.findViewById(R.id.description); // description
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // 缩略图

        Information info = new Information();
        info = data.get(position);

        // 设置ListView的相关值
        name.setText(info.getName());
        age.setText(info.getAge());
        sex.setText(info.getSex());
        day.setText(info.getDay());
        description.setText(info.getDescription());
        thumb_image.setTag(info.getImage());
        bitmapUtils.display(thumb_image, info.getImage());
        return vi;
    }
}