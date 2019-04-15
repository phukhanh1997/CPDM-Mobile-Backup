package com.example.admin.projectcapstonemobile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.adapter.TaskListAdapter;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.TaskService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedTaskFragment extends Fragment {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private TaskService taskService;
    private List<Task> tasks;
    private List<Task> listTask;
    private String userToken;
    private TextView headerTextOverDate;
    private TextView headerTextAssign;
    public AssignedTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assigned_task, null);
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setTitle("List Task Assigned");
        final ListView listView = (ListView) rootView.findViewById(R.id.listTaskAssigned);
        final ListView listViewOverDate = (ListView) rootView.findViewById(R.id.listTaskOverDate);
        //userToken = (String) getActivity().getIntent().getSerializableExtra("UserToken");
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        System.out.println("Day la userToken o assigned task " + userToken);
        taskService = ApiUtils.getTaskService();
        listTask = getAllTask();
        tasks = getAllTask();
        List<Task> toRemove = new ArrayList<Task>();
        Date currentDate = Calendar.getInstance().getTime();
        if(tasks!=null){
            System.out.println("Day la size cua list tasks" + tasks.size());
            for (Task task: tasks) {
                if(task.getStatus().equals("Outdated") || task.getStatus().equals("Complete outdated")){
                    toRemove.add(task);
                }
            }
            tasks.removeAll(toRemove);
            for (Task x: tasks
                 ) {
                System.out.println(x.getTitle());
            }
        }


        if(toRemove!=null){
            listViewOverDate.setAdapter(new TaskListAdapter(toRemove, getActivity()));
        }
        if(listViewOverDate.getCount()==0){
            listViewOverDate.setVisibility(View.INVISIBLE);
        }
        listViewOverDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = listViewOverDate.getItemAtPosition(position);
                Task task = (Task) object;
                Integer taskId = task.getId();
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });

        //set header for list view
//        LayoutInflater inflater2 = getLayoutInflater();
//        ViewGroup header = (ViewGroup) inflater2.inflate(R.layout.listview_header, listView, false);
//        listView.addHeaderView(header);
//        headerTextAssign = (TextView) header.findViewById(R.id.textView_header);
//        headerTextAssign.setText("List task assigned");
        //
        if(tasks!=null){
            listView.setAdapter(new TaskListAdapter(tasks, getActivity()));
        }

        if(listView.getCount()==0){
            listView.setVisibility(View.INVISIBLE);
        }
        else{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView.getItemAtPosition(position);
                    Task task = (Task) object;
                    Integer taskId = task.getId();
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }
    private List<Task> getAllTask(){
        List<Task> content = new ArrayList<>();
        Call<List<Task>> call = taskService.getAllTasks("Bearer " + userToken);
        String sum = null;
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
}
