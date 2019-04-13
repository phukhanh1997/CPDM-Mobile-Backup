package com.example.admin.projectcapstonemobile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.adapter.CommentListAdapter;
import com.example.admin.projectcapstonemobile.adapter.StoredCommentListAdapter;
import com.example.admin.projectcapstonemobile.adapter.TaskIssueAdapter;
import com.example.admin.projectcapstonemobile.model.Comment;
import com.example.admin.projectcapstonemobile.model.StoredComment;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.model.TaskIssue;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.CommentService;
import com.example.admin.projectcapstonemobile.remote.TaskService;
import com.example.admin.projectcapstonemobile.remote.UserService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;

public class TaskDetailActivity extends AppCompatActivity {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private TextView txt_createdBy;
    private TextView txt_from;
    private TextView txt_to;
    private TextView txt_taskTitle;
    private TextView txt_taskSummary;
    private ListView listView_comment;
    private ListView listView_storedComment;
    private ExpandableListView listView_taskIssue;
    private CommentService commentService;
    private TaskService taskService;
    private UserService userService;
    private List<Comment> listComment;
    private List<TaskIssue> listIssue;
    private List<StoredComment> listStoredComment;
    private String userToken;
    private EditText edtComment;
    private Button btnComment;
    private Integer taskId;
    private Dialog commentDialog;
    private TextView textView_dialog_comment_user;
    private EditText editText_dialog_comment_content;
    private Button btn_dialog_comment_confirm;
    private Button btn_dialog_comment_cancel;
    private Button btnChangeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setTitle("Mô tả công việc");
        txt_createdBy = (TextView) findViewById(R.id.textView_taskDetail_createdBy);
        txt_from = (TextView) findViewById(R.id.textView_taskDetail_timeStart);
        txt_to = (TextView) findViewById(R.id.textView_taskDetail_timeEnd);
        txt_taskTitle = (TextView) findViewById(R.id.textView_taskDetail_taskTitle);
        txt_taskSummary = (TextView) findViewById(R.id.textView_taskDetail_taskSummary);
        edtComment = (EditText) findViewById(R.id.edtText_task_detail_comment);
        btnComment = (Button) findViewById(R.id.btn_task_detail_send_comment);
        btnChangeStatus = (Button) findViewById(R.id.btn_task_detail_change_status);
        listView_taskIssue = (ExpandableListView) findViewById(R.id.listView_taskIssue);
                //dialog
        commentDialog = new Dialog(TaskDetailActivity.this);
        commentDialog.setTitle("Chỉnh sửa bình luận");
        commentDialog.setContentView(R.layout.dialog_comment);
        textView_dialog_comment_user = (TextView) commentDialog.findViewById(R.id.dialog_comment_username);
        editText_dialog_comment_content = (EditText) commentDialog.findViewById(R.id.dialog_comment_content);
        btn_dialog_comment_confirm = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_confirm);
        btn_dialog_comment_cancel = (Button) commentDialog.findViewById(R.id.btn_dialog_comment_cancel);
        listView_storedComment = (ListView) commentDialog.findViewById(R.id.listView_storedComment);


        commentService = ApiUtils.getCommentService();
        taskService = ApiUtils.getTaskService();
        userService = ApiUtils.getUserService();
        listView_comment = (ListView) findViewById(R.id.listComment);
        //userToken = (String) getIntent().getSerializableExtra("UserToken");
        final SharedPreferences sharedPreferences = getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");

        //load task information
        taskId = (Integer) getIntent().getSerializableExtra("taskId");
        Task task = getTaskDetail(userToken, taskId);
        txt_createdBy.setText("Người tạo: " + task.getCreator().getDisplayName());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dateStart = new Date();
        dateStart = task.getStartTime();

        txt_from.setText("Ngày bắt đầu: " + dateFormat.format(dateStart));
        Date dateEnd = new Date();
        dateEnd = task.getEndTime();
        //test
        Date currentDate = new Date(System.currentTimeMillis());
        //
        txt_to.setText("Ngày kết thúc: " + dateFormat.format(dateEnd));
        txt_taskTitle.setText("Tiêu đề: " + task.getTitle());
        txt_taskSummary.setText("Nội dung:" + task.getDescription());
        btnChangeStatus.setText("Đang thực hiện");
        System.out.println("Luc load len la`" + btnChangeStatus.getText());
        //load issue
        listIssue = getAllTaskIssue(userToken, taskId);
        List<String> listHeader = new ArrayList<String>();
        listHeader.add("Danh sách vấn đề:");
        HashMap<String, List<TaskIssue>> listDataChild = new HashMap<String, List<TaskIssue>>();
        listDataChild.put(listHeader.get(0), listIssue);

        if(listIssue!=null){
            listView_taskIssue.setAdapter(new TaskIssueAdapter(listHeader, listDataChild, this));
            System.out.println("Day la taskIssue " + listIssue.size());
        }

        //load comment
        listComment = getAllCommentByTaskId(task.getId());
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
                    if(listStoredComment!=null){
                        listView_storedComment.setAdapter(new StoredCommentListAdapter(listStoredComment, commentDialog.getContext()));
                        System.out.println("Hien thi stored comment size: " + listStoredComment.size());
                    }
                    textView_dialog_comment_user.setText(thisComment.getUser().getDisplayName());
                    String username = sharedPreferences.getString("userName", "");
                    System.out.println("Username trong sharedPref la " + username);
                    System.out.println("Email cua user la " + thisComment.getUser().getId());
                    Integer userId = getUserId(userToken);
                    System.out.println("User id self la " + userId);
                    if(userId != thisComment.getUser().getId()){
                        editText_dialog_comment_content.setEnabled(false);
                        btn_dialog_comment_confirm.setVisibility(View.INVISIBLE);
                    }
                    else{
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
                                Task newTask = new Task(task.getId());
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

        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer taskId = task.getId();
                changeStatus(taskId);
                task.setStatus("Done");
                btnChangeStatus.setText(task.getStatus());
                finish();
                startActivity(getIntent());
                System.out.println("Status bay gio la: " + task.getStatus());
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtComment.getText().toString();
                if (validationComment(content)) {
                    Task newTask = new Task(task.getId());
                    Comment newComment = new Comment(content, newTask, 1);
                    createComment(newComment);
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(TaskDetailActivity.this, "Comment can't be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<Comment> getAllCommentByTaskId(Integer taskId) {
        List<Comment> content = new ArrayList<>();
        Call<List<Comment>> call = commentService.getAllCommentByTaskId("Bearer " + userToken, taskId);
        try {
            content = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(content!=null){
            return content;
        }
        else return null;
    }

    private List<StoredComment> getAllOldVersion(Integer commentId){
        List<StoredComment> content = new ArrayList<StoredComment>();
        Call<List<StoredComment>> call = commentService.loadOldVersion("Bearer " + userToken, commentId);
        try{
            content = call.execute().body();
        } catch (IOException e){
            e.printStackTrace();
        }
        if(content!=null){
            return content;
        }
        else return null;
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

    private void editComment(Integer idComment, Comment editComment) {
        Call<Comment> call = commentService.editComment("Bearer " + userToken, idComment, editComment);
        try {
            call.execute().body();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void changeStatus(Integer taskId) {
        Call<Task> call = taskService.changeStatusTask("Bearer " + userToken, taskId);
        try {
            call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Integer getUserId(String userToken){
        User user = new User();
        Call<User> call = userService.getUserInformation("Bearer " + userToken);
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user.getId();
    }

    private Task getTaskDetail(String userToken, Integer taskId){
        Task taskDetail = new Task();
        Call<Task> call = taskService.getTaskDetail("Bearer " + userToken, taskId);
        try {
            taskDetail = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskDetail;
    }

    private List<TaskIssue> getAllTaskIssue(String userToken, Integer taskId){
        List<TaskIssue> listIssue = new ArrayList<>();
        Call<List<TaskIssue>> call = taskService.getAllTaskIssue("Bearer " + userToken, taskId);
        try {
            listIssue = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(listIssue!=null){
            return listIssue;
        }
        else return null;
    }
}
