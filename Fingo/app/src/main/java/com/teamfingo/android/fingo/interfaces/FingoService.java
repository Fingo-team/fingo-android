package com.teamfingo.android.fingo.interfaces;

import com.teamfingo.android.fingo.model.BoxOfficeRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Jaemin on 2016. 11. 29..
 *
 * BaseURL : http://fingo-test.ijvmwrmuhi.ap-northeast-2.elasticbeanstalk.com
 *
 */

public interface FingoService {

    @Headers("Authorization: token 0fe12e037aa03fa6d4914c01adbf6ae914c6043e")
    @GET("/api/v1.0/movie/boxoffice/")
    Call<BoxOfficeRanking> getBoxOfficeRanking();
}
