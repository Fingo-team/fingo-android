package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Jaemin on 2016. 11. 29..
 *
 * BaseURL : http://fingo-test.ijvmwrmuhi.ap-northeast-2.elasticbeanstalk.com
 *
 */

public interface FingoService {


    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking(@Header("Authorization") String authorization);

    @GET("/api/v1.0/movie/detail/{id}/")
    Call<Movie> getMovie(@Header("Authorization") String authorization, @Path("id") String id);

    // SignUp function

    // Search function

}
