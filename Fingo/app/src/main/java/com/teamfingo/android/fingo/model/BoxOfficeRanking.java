package com.teamfingo.android.fingo.model;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 11. 29..
 */

public class BoxOfficeRanking
{
    private ArrayList<Data> data = new ArrayList<>();

    public ArrayList<Data> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Data> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "[data = "+data+"]";
    }

    public class Data
    {
        private String rank;

        private Movie movie;

        public String getRank ()
        {
            return rank;
        }

        public void setRank (String rank)
        {
            this.rank = rank;
        }

        public Movie getMovie ()
        {
            return movie;
        }

        public void setMovie (Movie movie)
        {
            this.movie = movie;
        }

        @Override
        public String toString()
        {
            return "[rank = "+rank+", movie = "+movie+"]";
        }
    }
}
