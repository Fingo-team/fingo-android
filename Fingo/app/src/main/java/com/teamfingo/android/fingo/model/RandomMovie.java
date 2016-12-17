package com.teamfingo.android.fingo.model;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 12. 17..
 */

public class RandomMovie
{
    private ArrayList<Movie> data = new ArrayList<>();

    public ArrayList<Movie> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Movie> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+"]";
    }
}