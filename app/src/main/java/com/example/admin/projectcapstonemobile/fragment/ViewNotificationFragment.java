package com.example.admin.projectcapstonemobile.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.activity.TaskDetailActivity;
import com.example.admin.projectcapstonemobile.adapter.NotificationAdapter;
import com.example.admin.projectcapstonemobile.model.Notification;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.NotificationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNotificationFragment extends Fragment {
    private final String userInformationSharedPreferences = "informationSharedPreferences";
    private View rootView;
    private String userToken;
    private String userRole;
    private List<Notification> listNotification;
    private ListView listView;
    private NotificationService notificationService;
    public ViewNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_view_notification, container, false);
        notificationService = ApiUtils.getNotificationService();
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(userInformationSharedPreferences, Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("userToken", "");
        userRole = sharedPreferences.getString("userRole", "");

        listView = (ListView) rootView.findViewById(R.id.list_notification);

        listNotification = getListNotification(userToken);
        if(listNotification!=null){
            listView.setAdapter(new NotificationAdapter(listNotification, getActivity()));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = listView.getItemAtPosition(position);
                Notification notification = (Notification) object;
                String url = notification.getUrl();
                String type = url.split("/")[1];
                if(userRole.equals("ROLE_STAFF")){
                    if(type.equals("tasks")){
                        Integer taskId = Integer.parseInt(notification.getUrl().split("/")[2]);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("taskId", taskId);
                        startActivity(intent);
                    }
                    if(notification.getUrl().equals("/userLeaveRequests")){
                        ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment, "abc")
                                .addToBackStack(null)
                                .commit();
                    }
                }
                //if manager
                if(userRole.equals("ROLE_MANAGER")){
                    if(type.equals("tasks")){
                        Integer taskId = Integer.parseInt(notification.getUrl().split("/")[2]);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("taskId", taskId);
                        startActivity(intent);
                    }
                    if(notification.getUrl().equals("/approverLeaveRequests")){
                        ViewLeaveRequestForManagerFragment fragment = new ViewLeaveRequestForManagerFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment, "abc")
                                .addToBackStack(null)
                                .commit();
                    }

                    if(notification.getUrl().equals("/userLeaveRequests")){
                        ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment, "abc")
                                .addToBackStack(null)
                                .commit();
                    }
                }//end if
                if(userRole.equals("ROLE_ADMIN")){
                    if(type.equals("tasks")){
                        Integer taskId = Integer.parseInt(notification.getUrl().split("/")[2]);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("taskId", taskId);
                        startActivity(intent);
                    }
                    if(notification.getUrl().equals("/approverLeaveRequests")){
                        ViewLeaveRequestFragment fragment = new ViewLeaveRequestFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment, "abc")
                                .addToBackStack(null)
                                .commit();
                    }
                }


            }
        });


        return rootView;
    }
    private List<Notification> getListNotification(String userToken){
        List<Notification> listNoti = new ArrayList<>();
        Call<List<Notification>> call = notificationService.getAllNotification("Bearer " + userToken);
        try {
            listNoti = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listNoti;
    }
}
