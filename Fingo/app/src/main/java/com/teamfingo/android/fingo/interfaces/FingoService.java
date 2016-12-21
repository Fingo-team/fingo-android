package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.Category;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.MovieComment;
import com.teamfingo.android.fingo.model.MovieScore;
import com.teamfingo.android.fingo.model.MovieWish;
import com.teamfingo.android.fingo.model.MovieWrapper;
import com.teamfingo.android.fingo.model.SearchList;
import com.teamfingo.android.fingo.model.SearchMovie;
import com.teamfingo.android.fingo.model.Statistics;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.model.UserDetail;
import com.teamfingo.android.fingo.model.UserMovies;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jaemin on 2016. 11. 29..
 */

public interface FingoService {

    // Movie Category Main
    @GET("/api/v1.0/movie/main/")
    Call<Category> getCategoryMain(@Header("Authorization") String authorization);

    // Box Office Movie List
    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking(@Header("Authorization") String authorization);

    // Movie List
    @GET("/api/v1.0/movie/{type}/")
    Call<MovieWrapper> getMovieList(@Header("Authorization") String authorization, @Path("type") String type, @Query("genre") String genre);

    // Movie Detail Information
    @GET("/api/v1.0/movie/detail/{id}/")
    Call<Movie> getMovie(@Header("Authorization") String authorization, @Path("id") String id);

    // 검색했을 때 Movie Information
    @GET("/api/v1.0/movie/search/")
    Call<SearchMovie> getSearchMovie(@Header("Authorization") String authorization, @Query("q") String word, @Query("page") int page);

    // Movie Wish - 영화 상세 화면에서 보고싶어요와 관련된 API
    @GET("/api/v1.0/movie/wish/{id}/")
    Call<MovieWish> getMovieWish(@Header("Authorization") String authorization, @Path("id") String id);

    @FormUrlEncoded
    @POST("/api/v1.0/movie/wish/{id}/")
    Call<Void> postMovieWish(@Header("Authorization") String authorization, @Path("id") String id, @Field("wish_movie") String wishMovie);

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

    @FormUrlEncoded
    @PATCH("/api/v1.0/movie/{id}/comment/")
    Call<Void> patchMovieComment(@Header("Authorization") String authorization, @Path("id") String id, @Field("comment") String comment);

    // Statistics - User의 취향 통계를 나타내주는 API
    @GET("/api/v1.0/statistics/all/")
    Call<Statistics> getStatistics(@Header("Authorization") String authorization);

    // Recommend화면의 영화 평가 늘리기 - 영화 30개를 랜덤으로 불러오는 API
    @GET("/api/v1.0/movie/random/")
    Call<MovieWrapper> getRandomMovie(@Header("Authorization") String authorization);

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
    Call<SearchList> getSearchList(@Header("Authorization") String authorization, @Query("q") String q);

    // getPersonalComments
    @GET("/api/v1.0/activity/user/comments/")
    Call<UserComments> getUserComments(@Header("Authorization") String authorization, @Query("page")int page, @Query("order") String order);

    // get user detail
    @GET("/api/v1.0/activity/user/detail/")
    Call<UserDetail> getUserDetail(@Header("Authorization") String authorization);

    // get wish movie
    @GET("/api/v1.0/activity/user/wish/movies/")
    Call<UserMovies> getWishMovie(@Header("Authorization") String authorization, @Query("page") int page, @Query("order")String order);

    // get watched movie
    @GET("/api/v1.0/activity/user/watched/movies/")
    Call<UserMovies> getWatchedMovie(@Header("Authorization") String authorization, @Query("page") int page, @Query("order")String order);

    @FormUrlEncoded
    @POST("/api/v1.0/user/fb_login/")
    Call<FingoAccessToken> createFacebookUser(@Field("access_token") String facebook_token);

    // 코멘트 수정 및 삭제 in my page
    @FormUrlEncoded
    @PATCH("/api/v1.0/movie/{id}/comment/")
    Call<Void> correctUserComment(@Header("Authorization") String authorization, @Path("id") String id, @Field("comment") String comment);

    @DELETE("/api/v1.0/movie/{id}/comment/")
    Call<Void> deleteUserComment(@Header("Authorization") String authorization, @Path("id") String id);

    // 이미지 업로드
    @Multipart
    @PATCH("/api/v1.0/user/upload_profile/")
    Call<ResponseBody> uploadImage(@Header("Authorization") String authorization, @Part MultipartBody.Part image);

}