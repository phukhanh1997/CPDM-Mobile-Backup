package com.example.admin.projectcapstonemobile.activity;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.adapter.TaskListAdapter;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.TaskService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;

public class ListTaskActivity extends AppCompatActivity {
    private TaskService taskService;
    private List<Task> tasks;
    private TextView textView;
    private Button btnLogin;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setTitle("List Task");
        userToken = (String) getIntent().getSerializableExtra("UserToken");
        taskService = ApiUtils.getTaskService();
        tasks = getAllTask();
        final ListView listView = (ListView) findViewById(R.id.listTask);
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        try {
            Date date = dateFormat.parse(formattedDate);
            Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
            System.out.println("Day la format" + formattedDate);
            System.out.println("Day la clg do" + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tasks != null) {
            listView.setAdapter(new TaskListAdapter(tasks, this));
        }
        if (listView.getCount() == 0) {
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView.getItemAtPosition(position);
                    Task task = (Task) object;
                    Toast.makeText(ListTaskActivity.this, "You clicked in " + task.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListTaskActivity.this, TaskDetailActivity.class);
                    intent.putExtra("TaskDetail", task);
                    intent.putExtra("UserToken", userToken);
                    startActivity(intent);
                }
            });
        }
    }

    private List<Task> getAllTask() {
        List<Task> content = new ArrayList<>();
        Call<List<Task>> call = taskService.getAllTasks("Bearer " + userToken);
        String sum = null;
        try {
            content = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
