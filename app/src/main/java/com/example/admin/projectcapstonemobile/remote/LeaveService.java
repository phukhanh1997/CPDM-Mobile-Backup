package com.example.admin.projectcapstonemobile.remote;

import com.example.admin.projectcapstonemobile.model.LeaveRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LeaveService {
    @POST("leaveRequests")
    Call<LeaveRequest> createLeaveRequest(@Header("Authorization") String userToken,
                                          @Body LeaveRequest leaveRequest);


    @GET("leaveRequests/search/notAllowDateFromToday")
    Call<List<String>> getNotAllowedDate(@Header("Authorization") String userToken);

    @GET("leaveRequests/search/workingTaskDateFromToday")
    Call<List<String>> getWorkingDate(@Header("Authorization") String userToken);

    @GET("leaveRequests/search/findAllByUser")
    Call<List<LeaveRequest>> findLeaveRequestByUser(@Header("Authorization") String userToken,
                                                  @Query("sort") String createdDate,
                                                  @Query("status") Integer status);

    @GET("leaveRequests/search/findAllByApprover")
    Call<List<LeaveRequest>> findLeaveRequestByApprover(@Header("Authorization") String userToken,
                                                        @Query("sort") String createdDate,
                                                        @Query("status") Integer status);

    @PUT("leaveRequests/{id}")
    Call<LeaveRequest> updateStatusRequest(@Header("Authorization") String userToken,
                                           @Path("id") Integer requestId,
                                           @Body LeaveRequest leaveRequest);

    @DELETE("leaveRequests/{id}")
    Call<LeaveRequest> deleteLeaveRequest(@Header("Authorization") String userToken,
                                          @Path("id") Integer requestId);
}
