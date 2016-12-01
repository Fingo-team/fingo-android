package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Jaemin on 2016. 11. 29..
 *
 * BaseURL : http://fingo-test.ijvmwrmuhi.ap-northeast-2.elasticbeanstalk.com
 *
 */

public interface FingoService {

    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking(@Header("Authorization") String authorization);

    // SignUp function

    // Search function

}
