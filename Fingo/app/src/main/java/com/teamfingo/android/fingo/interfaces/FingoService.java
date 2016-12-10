package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.model.MovieComment;
import com.teamfingo.android.fingo.model.MovieScore;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.SearchList;
import com.teamfingo.android.fingo.model.SearchMovie;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.model.UserDetail;
import com.teamfingo.android.fingo.model.UserMovies;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jaemin on 2016. 11. 29..
 *
 *
 */

public interface FingoService {

    // Box Office Movie List
    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking(@Header("Authorization") String authorization);

    // Movie Detail Information
    @GET("/api/v1.0/movie/detail/{id}/")
    Call<Movie> getMovie(@Header("Authorization") String authorization, @Path("id") String id);

    // 검색했을 때 Movie Information
    @GET("/api/v1.0/movie/search/")
    Call<ArrayList<SearchMovie>> getSearchMovie(@Header("Authorization") String authorization, @Query("q") String word);

    // Movie Score - 영화 상세 화면에서 별점 남기기와 관련된 API
    @GET("/api/v1.0/movie/score/{id}/")
    Call<MovieScore> getMovieScore(@Header("Authorization") String authorization, @Path("id") String id);
    @FormUrlEncoded
    @POST("/api/v1.0/movie/score/{id}/")
    Call<Void> postMovieScore(@Header("Authorization") String authorization, @Path("id") String id, @Field("score") String score);

    // Movie Comment - 영화 상세 화면에서 코멘트 남기기와 관련된 API
    @GET("/api/v1.0/movie/{id}/comment/")
    Call<MovieComment> getMovieComment(@Header("Authorization") String authorization, @Path("id") String id);
    @FormUrlEncoded
    @POST("/api/v1.0/movie/{id}/comment/")
    Call<Void> postMovieComment(@Header("Authorization") String authorization, @Path("id") String id, @Field("comment") String comment);

    // SignUp function
    @FormUrlEncoded
    @POST("/api/v1.0/user/signup/")
    Call<FingoAccessToken> createEmailUser(@Field("email") String email, @Field("password") String password, @Field("nickname") String nickname);

    // email login function
    @FormUrlEncoded
    @POST("/api/v1.0/user/login/")
    Call<FingoAccessToken> userEmailLogin(@Field("email") String email, @Field("password") String password);

    // email logout function
    @POST("/api/v1.0/user/logout/")
    Call<Void> userEmailLogout(@Header("Authorization") String authorization);

    // Search function
    @GET("/api/v1.0/movie/search/")
    Call<SearchList> getSearchList(@Header("Authorization") String authorization, @Query("q")String q);

    // getPersonalComments
    @GET("/api/v1.0/activity/user/comments/")
    Call<UserComments> getUserComments(@Header("Authorization") String authorization);

    // get user detail
    @GET("/api/v1.0/activity/user/detail/")
    Call<UserDetail> getUserDetail(@Header("Authorization") String authorization);

    // get wish movie
    @GET("/api/v1.0/activity/user/wish/movies/")
    Call<UserMovies> getWishMovie(@Header("Authorization") String authorization);

    // get watched movie
    @GET("/api/v1.0/activity/user/watched/movies/")
    Call<UserMovies> getWatchedMovie(@Header("Authorization") String authorization);

}
