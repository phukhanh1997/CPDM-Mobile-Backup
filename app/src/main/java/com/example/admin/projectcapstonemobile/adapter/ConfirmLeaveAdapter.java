package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;

import java.io.Serializable;
import java.util.List;

public class ConfirmLeaveAdapter extends BaseAdapter implements Serializable {
    private List<LeaveRequest> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ConfirmLeaveAdapter(List<LeaveRequest> listData, Context acontext) {
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
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.confirm_leave_item_layout, null);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.textView_confirm_leave_item_userName);
            holder.fromDate = (TextView) convertView.findViewById(R.id.textView_confirm_leave_item_fromDate);
            holder.toDate = (TextView) convertView.findViewById(R.id.textView_confirm_leave_item_toDate);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        LeaveRequest request = this.listData.get(position);

        String fromDate = request.getFromDate();
        String[] fromDateArray = fromDate.split("-");
        String toDate = request.getToDate();
        String[] toDateArray = toDate.split("-");
        holder.userName.setText(request.getUser().getDisplayName());
        holder.fromDate.setText("Nghỉ từ ngày " + fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0]);
        holder.toDate.setText("Nghỉ đến ngày " + toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0]);

        return convertView;
    }
    static class ViewHolder{
        TextView userName;
        TextView fromDate;
        TextView toDate;
    }
}
