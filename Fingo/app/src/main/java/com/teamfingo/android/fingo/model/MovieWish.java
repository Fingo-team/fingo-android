package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 11..
 */

public class MovieWish {
    private String wish_movie;

    public String getWish_movie() {
        return wish_movie;
    }

    public void setWish_movie(String wish_movie) {
        this.wish_movie = wish_movie;
    }

    @Override
    public String toString() {
        return "[ wish_movie = " + wish_movie + " ]";
    }
}