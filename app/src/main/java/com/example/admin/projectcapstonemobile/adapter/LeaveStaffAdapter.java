package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.LeaveRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class LeaveStaffAdapter extends BaseExpandableListAdapter implements Serializable {
    private List<String> listData;
    private HashMap<String, List<LeaveRequest>> listDataChild;
    private Context context;

    public LeaveStaffAdapter(List<String> listData, HashMap<String, List<LeaveRequest>> listDataChild, Context context) {
        this.listData = listData;
        this.listDataChild = listDataChild;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_view_group, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView_listIssue_header);
        textView.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.leave_staff_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.fromDate = (TextView) convertView.findViewById(R.id.textView_leave_staff_fromDate);
            viewHolder.toDate = (TextView) convertView.findViewById(R.id.textView_leave_staff_toDate);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LeaveRequest leaveRequest = (LeaveRequest) getChild(groupPosition, childPosition);
        viewHolder.fromDate.setText("Ngày bắt đầu: " + leaveRequest.getFromDate());
        viewHolder.toDate.setText("Số ngày: " + leaveRequest.getDayOff());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class ViewHolder{
        TextView fromDate;
        TextView toDate;
    }
}
