package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
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
            holder.textSummary = (TextView) convertView.findViewById(R.id.textView_taskSummary);
            holder.textProject = (TextView) convertView.findViewById(R.id.textView_taskProject);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Task task = this.listData.get(position);
        holder.textId.setText(task.getId() + "");
        holder.textTitle.setText(task.getTitle());
        holder.textSummary.setText(task.getDescription());
        holder.textProject.setText(task.getProject().getName());

        return convertView;
    }

    static class ViewHolder{
        TextView textId;
        TextView textTitle;
        TextView textSummary;
        TextView textProject;
    }
}
