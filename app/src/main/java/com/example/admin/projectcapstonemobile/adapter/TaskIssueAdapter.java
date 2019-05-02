package com.example.admin.projectcapstonemobile.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.TaskIssue;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.TaskService;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class TaskIssueAdapter extends BaseExpandableListAdapter implements Serializable {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private String userToken;
    private List<String> listData;
    private HashMap<String, List<TaskIssue>> listDataChild;
    private Context context;
    private TaskService taskService = ApiUtils.getTaskService();

    public TaskIssueAdapter(List<String> listData, HashMap<String, List<TaskIssue>> listDataChild, Context context) {
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_view_group, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView_listIssue_header);
        textView.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        SharedPreferences sharedPreferences = context.getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.task_issue_item_layout, null);
            holder = new ViewHolder();
            holder.taskIssueNumber = (TextView) convertView.findViewById(R.id.textView_taskIssue_number);
            holder.taskIssueDescription = (TextView) convertView.findViewById(R.id.textView_taskIssue_description);
            holder.taskIssueStatus = (TextView) convertView.findViewById(R.id.textView_taskIssue_status);
            holder.buttonFinish = (Button) convertView.findViewById(R.id.btn_taskIssue_confirm);
            holder.imgStatus = convertView.findViewById(R.id.image_view_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskIssue issue = (TaskIssue) getChild(groupPosition, childPosition);

        holder.taskIssueNumber.setText(getChildId(groupPosition, childPosition) + 1 + ". ");
        holder.taskIssueDescription.setText(issue.getDetail());

        if (issue.getCompleted()) {
            holder.taskIssueStatus.setText("Hoàn thành");
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_checked));
            holder.buttonFinish.setVisibility(View.INVISIBLE);
        } else {
            holder.taskIssueStatus.setText("Chưa hoàn thành");
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_circle_shape));
        }
        holder.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_confirm_taskissue);
                dialog.setTitle("Xác nhận báo cáo");
                Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm_changestatusissue);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel_changestatusissue);
                dialog.show();
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Call<TaskIssue> call = taskService.completeTaskIssue("Bearer " + userToken, issue.getId());
                        try {
                            call.execute().body();
                            holder.taskIssueStatus.setText("Hoàn thành");
                            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_checked));
                            holder.buttonFinish.setVisibility(View.INVISIBLE);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView taskIssueNumber;
        TextView taskIssueDescription;
        TextView taskIssueStatus;
        Button buttonFinish;
        ImageView imgStatus;
    }
}
