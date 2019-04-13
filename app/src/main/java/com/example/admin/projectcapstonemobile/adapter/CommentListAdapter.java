package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Comment;

import java.io.Serializable;
import java.util.List;

public class CommentListAdapter extends BaseAdapter implements Serializable {
    private List<Comment> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentListAdapter(List<Comment> listData, Context acontext) {
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
            convertView = layoutInflater.inflate(R.layout.comment_item_layout, null);
            holder = new ViewHolder();
            holder.commentUser = (TextView) convertView.findViewById(R.id.textView_comment_item_name);
            holder.commentContent = (TextView) convertView.findViewById(R.id.textView_comment_item_content);
            holder.lastModifiedDate = (TextView) convertView.findViewById(R.id.textView_comment_item_lastModifiedDate);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Comment comment = this.listData.get(position);
        holder.commentUser.setText(comment.getUser().getDisplayName());
        holder.commentContent.setText(comment.getContent());
        if(comment.getStatus()==1){
            holder.lastModifiedDate.setText("Đã chỉnh sửa");
        } else holder.lastModifiedDate.setText("");

        return convertView;
    }

    static class ViewHolder{
        TextView commentUser;
        TextView commentContent;
        TextView lastModifiedDate;
    }
}
