package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 6..
 */

public class SearchMovie
{
    private Genre[] genre;

    private String id;

    private Nation_code[] nation_code;

    private String title;

    private String score;

    private String img;

    private String first_run_date;

    public Genre[] getGenre ()
    {
        return genre;
    }

    public void setGenre (Genre[] genre)
    {
        this.genre = genre;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Nation_code[] getNation_code ()
    {
        return nation_code;
    }

    public void setNation_code (Nation_code[] nation_code)
    {
        this.nation_code = nation_code;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    public String getImg ()
    {
        return img;
    }

    public void setImg (String img)
    {
        this.img = img;
    }

    public String getFirst_run_date ()
    {
        return first_run_date;
    }

    public void setFirst_run_date (String first_run_date)
    {
        this.first_run_date = first_run_date;
    }

    @Override
    public String toString()
    {
        return "[genre = "+genre+", id = "+id+", nation_code = "+nation_code+", title = "+title+", score = "+score+", img = "+img+", first_run_date = "+first_run_date+"]";
    }

    public class Genre
    {
        private String name;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public class Nation_code
    {
        private String name;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}