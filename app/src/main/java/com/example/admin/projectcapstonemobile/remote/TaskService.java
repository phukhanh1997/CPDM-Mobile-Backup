package com.example.admin.projectcapstonemobile.remote;

import com.example.admin.projectcapstonemobile.model.Task;
import com.example.admin.projectcapstonemobile.model.TaskIssue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface TaskService {
    @GET("tasks/search/all/executes")
    Call<List<Task>> getAllTasks(@Header("Authorization") String userToken);

    @GET("tasks/{taskId}")
    Call<Task> getTaskDetail(@Header("Authorization") String userToken, @Path("taskId") Integer taskId);

    @PATCH("tasks/{taskId}/complete")
    Call<Task> changeStatusTask(@Header("Authorization") String userToken, @Path("taskId") Integer taskId);

    @GET("tasks/search/all/creates")
    Call<List<Task>> getAllTasksCreated(@Header("Authorization") String userToken);

    @GET("tasks/search/all/relatives")
    Call<List<Task>> getAllTasksRelative(@Header("Authorization") String userToken);

    @GET("tasks/{taskId}/issues")
    Call<List<TaskIssue>> getAllTaskIssue(@Header("Authorization") String userToken, @Path("taskId") Integer taskId);

    @PATCH("task-issues/{id}/complete")
    Call<TaskIssue> completeTaskIssue(@Header("Authorization") String userToken, @Path("id") Integer id);
}
