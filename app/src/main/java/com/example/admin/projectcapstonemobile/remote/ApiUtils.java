package com.example.admin.projectcapstonemobile.remote;



public class ApiUtils {
    public static final String BASE_UTL = "http://192.168.1.41:8080/";

    public static TaskService getTaskService(){
        return RetrofitClient.getClient(BASE_UTL).create(TaskService.class);
    }

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_UTL).create(UserService.class);
    }

    public static CommentService getCommentService(){
        return RetrofitClient.getClient(BASE_UTL).create(CommentService.class);
    }
    public static LeaveService getLeaveService(){
        return RetrofitClient.getClient(BASE_UTL).create(LeaveService.class);
    }
    public static NotificationService getNotificationService(){
        return RetrofitClient.getClient(BASE_UTL).create(NotificationService.class);
    }
}
