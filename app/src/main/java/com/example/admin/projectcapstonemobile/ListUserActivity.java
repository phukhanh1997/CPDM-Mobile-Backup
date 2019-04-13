package com.example.admin.projectcapstonemobile;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.adapter.UserListAdapter;
import com.example.admin.projectcapstonemobile.model.User;
import com.example.admin.projectcapstonemobile.remote.ApiUtils;
import com.example.admin.projectcapstonemobile.remote.UserService;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;

public class ListUserActivity extends AppCompatActivity {
    private UserService userService;
    private List<User> users;
    private String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userToken = (String) getIntent().getSerializableExtra("UserToken");
        userService = ApiUtils.getUserService();
        users = getAllUser();
        final ListView listView = (ListView) findViewById(R.id.listUser);


        listView.setAdapter(new UserListAdapter(users, this));
        if(listView.getCount()==0){
            listView.setVisibility(View.INVISIBLE);
        }
        else{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = listView.getItemAtPosition(position);
                    User user = (User) object;
                    Intent intent = new Intent(ListUserActivity.this, UserDetailActivity.class);
                    intent.putExtra("UserDetail", user);
                    intent.putExtra("UserToken", userToken);
                    startActivity(intent);
                }
            });
        }

    }

    private List<User> getAllUser(){
        List<User> userList = null;
        Call<List<User>> call = userService.getAllUsers("Bearer " + userToken);
        String sum = null;
        try{
            userList = call.execute().body();
        } catch (IOException e){
            e.printStackTrace();
        }

        return userList;
    }
}
