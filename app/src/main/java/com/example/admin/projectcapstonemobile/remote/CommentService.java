package com.example.admin.projectcapstonemobile.remote;

import com.example.admin.projectcapstonemobile.model.Comment;
import com.example.admin.projectcapstonemobile.model.StoredComment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {
    @GET("comments/findAllByTask")
    Call<List<Comment>> getAllCommentByTaskId(@Header ("Authorization") String token, @Query("id") Integer taskId);

    @POST("comments")
    Call<Comment> createComment(@Header ("Authorization") String token, @Body Comment comment);

    @PUT("comments/{commentId}")
    Call<Comment> editComment(@Header ("Authorization") String token,@Path("commentId") Integer commentId, @Body Comment comment);

    @GET("comments/loadOldVersion/{id}")
    Call<List<StoredComment>> loadOldVersion(@Header ("Authorization") String token, @Path("id") Integer commentId);
}
