package com.example.admin.projectcapstonemobile.remote;

import com.example.admin.projectcapstonemobile.model.Credential;
import com.example.admin.projectcapstonemobile.model.ResObject;
import com.example.admin.projectcapstonemobile.model.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @POST("login")
    Call<ResObject> getUser(@Body Credential body);

    @GET("self")
    Call<User> getUserInformation(@Header("Authorization") String userToken);

    @GET("users/findAllDisplayNameByDepartmentAndRoleNameOfCurrentLoggedManager")
    Call<List<User>> getApprover(@Header("Authorization") String userToken, @Query("roleName") String roleName);

    @GET("users/search/all/list")
    Call<List<User>> getAllStaff(@Header("Authorization") String userToken);
}
