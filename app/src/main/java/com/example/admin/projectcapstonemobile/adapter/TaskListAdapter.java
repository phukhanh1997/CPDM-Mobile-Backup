package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Task;


import java.io.Serializable;
import java.util.List;

public class TaskListAdapter extends BaseAdapter implements Serializable {
    private List<Task> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public TaskListAdapter(List<Task> listData, Context acontext) {
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
            convertView = layoutInflater.inflate(R.layout.task_item_layout, null);
            holder = new ViewHolder();
            holder.textId = (TextView) convertView.findViewById(R.id.textView_taskId);
            holder.textTitle = (TextView) convertView.findViewById(R.id.textView_taskTitle);
            holder.textProject = (TextView) convertView.findViewById(R.id.textView_taskProject);
            holder.textStatus= (TextView) convertView.findViewById(R.id.textView_taskStatus);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = this.listData.get(position);
        holder.textId.setText(task.getId() + "");
        holder.textTitle.setText(task.getTitle());
        holder.textProject.setText(task.getProject().getName());

        if(task.getStatus().equals("Outdated")){
            holder.textStatus.setText("Trễ hạn");
            holder.textStatus.setTextColor(Color.RED);
        }
        else if(task.getStatus().equals("Complete outdated")){
            holder.textStatus.setText("Hoàn thành");
            holder.textStatus.setTextColor(Color.BLUE);
        }
        else if(task.getStatus().equals("Completed")){
            holder.textStatus.setText("Hoàn thành");
            holder.textStatus.setTextColor(Color.BLUE);
        }
        else if(task.getStatus().equals("Waiting")){
            holder.textStatus.setText("Đang chờ");
            holder.textStatus.setTextColor(Color.GRAY);
        }
        else{
            holder.textStatus.setText("Đang thực hiện");
        }
        return convertView;
    }

    static class ViewHolder{
        TextView textId;
        TextView textTitle;
        TextView textProject;
        TextView textStatus;
    }
}
