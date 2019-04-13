package com.example.admin.projectcapstonemobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.model.Comment;
import com.example.admin.projectcapstonemobile.model.StoredComment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

public class StoredCommentListAdapter extends BaseAdapter implements Serializable {
    private List<StoredComment> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    public StoredCommentListAdapter(List<StoredComment> listData, Context acontext) {
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
        StoredCommentListAdapter.ViewHolder holder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.storedcomment_item_layout, null);
            holder = new StoredCommentListAdapter.ViewHolder();
            holder.commentContent = (TextView) convertView.findViewById(R.id.textView_storedcomment_item_content);
            holder.lastModifiedDate = (TextView) convertView.findViewById(R.id.textView_storedcomment_item_lastModifiedDate);
            convertView.setTag(holder);
        }
        else{
            holder = (StoredCommentListAdapter.ViewHolder) convertView.getTag();
        }
        StoredComment comment = this.listData.get(position);
        holder.commentContent.setText(comment.getContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(comment.getCreatedDate());
        holder.lastModifiedDate.setText("Thời gian chỉnh sửa: " + formattedDate);

        return convertView;
    }
    static class ViewHolder{
        TextView commentContent;
        TextView lastModifiedDate;
    }
}
