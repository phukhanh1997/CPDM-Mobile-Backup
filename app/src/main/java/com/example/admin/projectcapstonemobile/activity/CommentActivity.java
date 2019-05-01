package com.example.admin.projectcapstonemobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.adapter.CommentListAdapter;
import com.example.admin.projectcapstonemobile.adapter.StoredCommentListAdapter;
import com.example.admin.projectcapstonemobile.model.Comment;
import com.example.admin.projectcapstonemobile.model.StoredComment;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.CommentService;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.google.android.gms.common.api.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private ListView listView_comment;
    private List<Comment> listComment;
    private EditText edtComment;
    private Button btnComment;
    private CommentService commentService;
    private UserService userService;
    private LinearLayout mLnlBack;
    private String userToken;
    private Integer taskId;
    private List<StoredComment> listStoredComment;
    private ListView listView_storedComment;
    private Dialog commentDialog;
    private TextView textView_dialog_comment_user;
    private EditText editText_dialog_comment_content;
    private Button btn_dialog_comment_confirm;
    private Button btn_dialog_comment_cancel;
    private Button btn_confirm_change_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initialView();
        commentService = ApiUtils.getCommentService();
        userService = ApiUtils.getUserService();
        taskId = (Integer) getIntent().getSerializableExtra("taskId");
        listView_comment = (ListView) findViewById(R.id.listCommentDetail);
        edtComment = (EditText) findViewById(R.id.edtText_task_detail_comment);
        btnComment = (Button) findViewById(R.id.btn_task_detail_send_comment);


        commentDialog = new Dialog(CommentActivity.this);
        commentDialog.setTitle("Chỉnh sửa bình luận");
        commentDialog.setContentView(R.layout.dialog_comment);
        textView_dialog_comment_user = (TextView) commentDialog.findViewById(R.id.dialog_comment_username);
        editText_dialog_comment_content = (EditText) commentDialog.findViewById(R.id.dialog_comment_content);
        btn_dialog_comment_confirm = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_confirm);
        btn_dialog_comment_cancel = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_cancel);
        listView_storedComment = (ListView) commentDialog.findViewById(R.id.listView_storedComment);

        final SharedPreferences sharedPreferences = getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");


        listComment = getAllCommentByTaskId(taskId);
        if (listComment != null) {
            listView_comment.setAdapter(new CommentListAdapter(listComment, this));
        }

        if (listView_comment.getCount() == 0) {
            listView_comment.setVisibility(View.INVISIBLE);
        } else {
            listView_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView_comment.getItemAtPosition(position);
                    final Comment thisComment = (Comment) object;
                    Integer commentId = thisComment.getId();
                    listStoredComment = getAllOldVersion(commentId);
                    System.out.println("Da den on item click");
                    if (listStoredComment != null) {
                        listView_storedComment.setAdapter(new StoredCommentListAdapter(listStoredComment, commentDialog.getContext()));
                        System.out.println("Hien thi stored comment size: " + listStoredComment.size());
                    }
                    textView_dialog_comment_user.setText(thisComment.getUser().getDisplayName());
                    String username = sharedPreferences.getString("userName", "");
                    System.out.println("Username trong sharedPref la " + username);
                    System.out.println("Email cua user la " + thisComment.getUser().getId());
                    Integer userId = getUserId(userToken);
                    System.out.println("User id self la " + userId);
                    if (userId != thisComment.getUser().getId()) {
                        editText_dialog_comment_content.setEnabled(false);
                        btn_dialog_comment_confirm.setVisibility(View.INVISIBLE);
                    } else {
                        editText_dialog_comment_content.setEnabled(true);
                        btn_dialog_comment_confirm.setVisibility(View.VISIBLE);
                    }
                    editText_dialog_comment_content.setText(thisComment.getContent());
                    commentDialog.show();
                    //editText_dialog_comment_content.setEnabled(false);
                    btn_dialog_comment_confirm.setOnClickListener(new View.OnClickListener() {
                        Integer id = thisComment.getId();

                        @Override
                        public void onClick(View v) {
                            String content = editText_dialog_comment_content.getText().toString();
                            System.out.println("Day la content" + content);
                            if (validationComment(content)) {
                                Task newTask = new Task(taskId);
                                Comment editComment = new Comment(content, newTask, 1);
                                editComment(id, editComment);
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    });
                    btn_dialog_comment_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commentDialog.dismiss();
                        }
                    });
                }
            });
        }
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtComment.getText().toString();
                if (validationComment(content)) {
                    Task newTask = new Task(taskId);
                    Comment newComment = new Comment(content, newTask, 1);
                    createComment(newComment);
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(CommentActivity.this, "Bình luận không thể bỏ trống.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialView() {
        mLnlBack = findViewById(R.id.linear_layout_back);
        mLnlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_layout_back:
                CommentActivity.this.finish();
                break;
        }
    }
    private boolean validationComment(String comment) {
        if (comment == null || comment.trim().length() == 0) {
            Toast.makeText(this, "Comment can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createComment(Comment newComment) {
        Call<Comment> call = commentService.createComment("Bearer " + userToken, newComment);
        try {
            call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<Comment> getAllCommentByTaskId(Integer taskId) {
        List<Comment> content = new ArrayList<>();
        Call<List<Comment>> call = commentService.getAllCommentByTaskId("Bearer " + userToken, taskId);
        try {
            content = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null) {
            return content;
        } else return null;
    }

    private List<StoredComment> getAllOldVersion(Integer commentId) {
        List<StoredComment> content = new ArrayList<StoredComment>();
        Call<List<StoredComment>> call = commentService.loadOldVersion("Bearer " + userToken, commentId);
        try {
            content = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content != null) {
            return content;
        } else return null;
    }
    private void editComment(Integer idComment, Comment editComment) {
        Call<Comment> call = commentService.editComment("Bearer " + userToken, idComment, editComment);
        try {
            call.execute().body();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private Integer getUserId(String userToken) {
        User user = new User();
        Call<User> call = userService.getUserInformation("Bearer " + userToken);
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user.getId();
    }
}
