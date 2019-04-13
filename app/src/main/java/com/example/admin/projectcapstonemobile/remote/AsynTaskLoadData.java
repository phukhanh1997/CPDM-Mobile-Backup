package com.example.admin.projectcapstonemobile.remote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.model.Task;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsynTaskLoadData extends AsyncTask {
    ProgressDialog progressDialog;
    Context context;
    TaskService taskService;
    private List<Task> tasks;

    public AsynTaskLoadData(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("HowKteam");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }

    @Override
    protected Object doInBackground(Object[] objects) {// class nay dung lam gi? tai luc dau em ko co class nay voi ko co ham

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
    }
}
