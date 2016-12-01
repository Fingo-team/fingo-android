package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.model.SearchList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jaemin on 2016. 11. 29..
 *
 * BaseURL : http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/
 *
 */

public interface FingoService {


    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking(@Header("Authorization") String authorization);

    // SignUp function
    @FormUrlEncoded
    @POST("/api/v1.0/user/signup/")
    Call<FingoAccessToken> createEmailUser(@Field("email") String email, @Field("password") String password, @Field("nickname") String nickname);

    // SignUp function
    @FormUrlEncoded
    @POST("/api/v1.0/user/login/")
    Call<FingoAccessToken> userEmailLogin(@Field("email") String email, @Field("password") String password);

    // Search function
    @GET("/api/v1.0/movie/search/")
    Call<SearchList> getSearchList(@Header("Authorization") String authorization, @Query("q")String q);

}
