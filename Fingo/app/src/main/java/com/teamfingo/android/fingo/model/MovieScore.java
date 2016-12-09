package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 9..
 */

public class MovieScore
{
    private String score;

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [score = "+score+"]";
    }
}
