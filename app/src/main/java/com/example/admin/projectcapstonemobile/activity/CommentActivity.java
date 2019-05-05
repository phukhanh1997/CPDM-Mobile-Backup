package com.example.admin.projectcapstonemobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.example.admin.projectcapstonemobile.model.Notification;
import com.example.admin.projectcapstonemobile.model.StoredComment;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.CommentService;
import com.example.admin.projectcapstonemobile.remote.NotificationService;
import com.example.admin.projectcapstonemobile.remote.TaskService;
import com.example.admin.projectcapstonemobile.remote.UserService;
import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private NotificationService notificationService;
    private TaskService taskService;
    private Task task;
    private List<User> receivers = new ArrayList<>();
    private DatabaseReference databaseReference;
    private List<User> relatives;
    private User selfUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initialView();
        final SharedPreferences sharedPreferences = getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        commentService = ApiUtils.getCommentService();
        userService = ApiUtils.getUserService();
        taskService = ApiUtils.getTaskService();
        notificationService = ApiUtils.getNotificationService();
        taskId = (Integer) getIntent().getSerializableExtra("taskId");
        task = getTaskDetail(userToken, taskId);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        listView_comment = (ListView) findViewById(R.id.listCommentDetail);
        edtComment = (EditText) findViewById(R.id.edtText_task_detail_comment);
        btnComment = (Button) findViewById(R.id.btn_task_detail_send_comment);

        relatives = getAllRelatives(userToken, taskId);
        Integer idCreator = task.getCreator().getId();
        Integer idExecutor = task.getExecutor().getId();
        User creator = new User(idCreator);
        User executor = new User(idExecutor);
        if(relatives!=null){
            for (User x: relatives) {
                receivers.add(x);
            }
        }
        receivers.add(creator);
        receivers.add(executor);
        Integer selfId = getUserId(userToken);
        User self = new User(selfId);
        receivers.remove(self);
        selfUser = getUserInformation(userToken);
        commentDialog = new Dialog(CommentActivity.this);
        commentDialog.setTitle("Chỉnh sửa bình luận");
        commentDialog.setContentView(R.layout.dialog_comment);
        textView_dialog_comment_user = (TextView) commentDialog.findViewById(R.id.dialog_comment_username);
        editText_dialog_comment_content = (EditText) commentDialog.findViewById(R.id.dialog_comment_content);
        btn_dialog_comment_confirm = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_confirm);
        btn_dialog_comment_cancel = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_cancel);
        listView_storedComment = (ListView) commentDialog.findViewById(R.id.listView_storedComment);



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
                    for (User x:receivers) {
                        Notification newNoti = new Notification("Một bình luận của " + selfUser.getDisplayName(), "Từ " + task.getTitle(), "/tasks/" + taskId, x);
                        Call<Notification> callNoti = notificationService.sendNotification("Bearer " + userToken, newNoti);
                        callNoti.enqueue(new Callback<Notification>() {
                            @Override
                            public void onResponse(Call<Notification> call, Response<Notification> response) {
                                if(response.isSuccessful()){
                                    Integer idRes = response.body().getId();
                                    for (User x: receivers) {
                                        databaseReference.child("users/").child(x.getId().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<String> abc = (List<String>) dataSnapshot.getValue();
                                                String title = newNoti.getTitle();
                                                String detail = newNoti.getDetail();
                                                System.out.println("Day la title " + title);
                                                System.out.println("Day la detail " + detail);
                                                Notification noti = new Notification(title, detail, abc);
                                                noti.setId(idRes);
                                                noti.setUrl("/tasks/" + taskId);

                                                Call<Void> callPush = notificationService.pushNotification("Bearer " + userToken, noti);
                                                callPush.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if (response.isSuccessful()) {
                                                            //Toast.makeText(CommentActivity.this, "Call success", Toast.LENGTH_SHORT).show();
                                                        }
                                                        if (!response.isSuccessful()) {
                                                            //Toast.makeText(CommentActivity.this, "Call fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        //Toast.makeText(CommentActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Notification> call, Throwable t) {

                            }
                        });
                    }

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

    private User getUserInformation(String userToken) {
        User newUser = new User();
        Call<User> call = userService.getUserInformation(userToken);
        try {
            newUser = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newUser;
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
    private List<User> getAllRelatives(String userToken, Integer taskId){
        List<User> listRelative = new ArrayList<>();
        Call<List<User>> call = taskService.getAllRelatives("Bearer " + userToken, taskId);

        try {
            listRelative = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listRelative;
    }
    private Task getTaskDetail(String userToken, Integer taskId) {
        Task taskDetail = new Task();
        Call<Task> call = taskService.getTaskDetail("Bearer " + userToken, taskId);
        try {
            taskDetail = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskDetail;
    }
}
