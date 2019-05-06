package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Notification;

import java.io.Serializable;
import java.util.List;

public class NotificationAdapter extends BaseAdapter implements Serializable {

    private List<Notification> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public NotificationAdapter(List<Notification> listData, Context acontext) {
        this.listData = listData;
        this.context = context;
        layoutInflater = LayoutInflater.from(acontext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.notification_item, null);
            holder = new ViewHolder();
            holder.notification_content = (TextView) convertView.findViewById(R.id.textView_notification_item_content);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view_red_dot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Notification notification = this.listData.get(position);
        holder.notification_content.setText(notification.getTitle());
        if(!notification.isRead()){
            holder.imageView.setVisibility(View.VISIBLE);
        }
        if(notification.isRead()){
            holder.imageView.setVisibility(View.GONE);
        }

        return convertView;
    }
    static class ViewHolder {
        TextView notification_content;
        ImageView imageView;
    }
}
