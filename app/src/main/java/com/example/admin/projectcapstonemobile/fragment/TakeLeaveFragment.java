package com.example.admin.projectcapstonemobile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.admin.projectcapstonemobile.R;

import androidx.fragment.app.Fragment;

public class TakeLeaveFragment extends Fragment implements View.OnClickListener {


    public TakeLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_leave, container, false);
        initialView();
        initialData();
        return view;
    }

    private void initialView() {

    }


    private void initialData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
