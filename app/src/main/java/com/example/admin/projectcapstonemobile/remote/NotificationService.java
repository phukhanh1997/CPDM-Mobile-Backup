package com.example.admin.projectcapstonemobile.remote;

import com.example.admin.projectcapstonemobile.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NotificationService {
    @GET("notifications")
    Call<List<Notification>> getAllNotification(@Header("Authorization") String userToken);

    @POST("notifications/push")
    Call<Notification> pushNotification(@Header("Authorization") String userToken,
                                        @Body Notification notification);
}
