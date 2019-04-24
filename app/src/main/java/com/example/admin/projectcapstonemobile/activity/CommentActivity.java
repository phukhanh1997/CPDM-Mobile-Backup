package com.example.admin.projectcapstonemobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.admin.projectcapstonemobile.R;


public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLnlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initialView();
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
}
