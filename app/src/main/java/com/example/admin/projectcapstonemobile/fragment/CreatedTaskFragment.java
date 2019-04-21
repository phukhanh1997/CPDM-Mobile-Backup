package com.example.admin.projectcapstonemobile.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.activity.TaskDetailActivity;
import com.example.admin.projectcapstonemobile.adapter.TaskListAdapter;
import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.TaskService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedTaskFragment extends Fragment {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private TaskService taskService;
    private List<Task> tasks;
    private String userToken;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE = 10;
    private int noOfBtns;
    private Button[] btns;
    private TextView title;
    private ListView listView;
    private View rootView;
    public CreatedTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_created_task, null);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setTitle("List task created");
        listView = (ListView) rootView.findViewById(R.id.listTaskCreated);
        title = (TextView) rootView.findViewById(R.id.titlePaginationCreated);
        //userToken = (String) getActivity().getIntent().getSerializableExtra("UserToken");
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        taskService = ApiUtils.getTaskService();
        tasks = getAllTask();
        TOTAL_LIST_ITEMS = tasks.size();
        Btnfooter(inflater);

        loadList(0);
        CheckBtnBackGroud(0);

        if (listView.getCount() == 0) {
            listView.setVisibility(View.INVISIBLE);
        } else {
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
    private void Btnfooter(LayoutInflater inflater)
    {
        int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        LinearLayout ll = (LinearLayout)rootView.findViewById(R.id.btnLayCreated);

        btns = new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this.getActivity());
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }
    private void CheckBtnBackGroud(int index)
    {
        title.setText("Danh sách công việc");
        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                btns[index].setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }
    private void loadList(int number)
    {
        List<Task> sort = new ArrayList<Task>();

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<tasks.size())
            {
                sort.add(tasks.get(i));
            }
            else
            {
                break;
            }
        }
        listView.setAdapter(new TaskListAdapter(sort, getActivity()));
    }
    private List<Task> getAllTask() {
        List<Task> content = new ArrayList<>();
        Call<List<Task>> call = taskService.getAllTasksCreated("Bearer " + userToken);
        String sum = null;
        try {
            content = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            return content;
        } else
            return null;
    }
}
