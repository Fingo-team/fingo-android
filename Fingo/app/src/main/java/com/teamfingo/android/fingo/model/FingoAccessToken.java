package com.teamfingo.android.fingo.model;

/**
 * Created by taewon on 2016-12-01.
 */

public class FingoAccessToken
{
    private String token;

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [token = "+token+"]";
    }
}