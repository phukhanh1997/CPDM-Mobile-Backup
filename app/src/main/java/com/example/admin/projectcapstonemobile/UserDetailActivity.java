package com.example.admin.projectcapstonemobile;

import android.os.Bundle;
import android.widget.TextView;

import com.example.admin.projectcapstonemobile.model.User;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {
    private TextView txtDisplayName;
    private String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        User user = (User) getIntent().getSerializableExtra("UserDetail");
        txtDisplayName = (TextView) findViewById(R.id.textView_userDetail_displayName);
        txtDisplayName.setText(user.getDisplayName());
        userToken = (String) getIntent().getSerializableExtra("UserToken");
    }
}
