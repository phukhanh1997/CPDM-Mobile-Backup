package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.model.User;

import java.io.Serializable;
import java.util.List;

public class UserListAdapter extends BaseAdapter implements Serializable {
    private List<User> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public UserListAdapter(List<User> listData, Context acontext) {
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
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.user_item_layout, null);
            holder = new ViewHolder();
            holder.textId = (TextView) convertView.findViewById(R.id.textView_listUser_id);
            holder.textDisplayName = (TextView) convertView.findViewById(R.id.textView_listUser_name);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        User user = this.listData.get(position);
        holder.textId.setText(user.getId() + " ");
        holder.textDisplayName.setText(user.getDisplayName());

        return convertView;
    }

    static class ViewHolder{
        TextView textId;
        TextView textDisplayName;
    }
}
