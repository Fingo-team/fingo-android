package com.teamfingo.android.fingo.model;

/**
 * Created by taewon on 2016-12-09.
 */

public class UserMovies
{
    private Results[] results;

    private String previous;

    private String next;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    public String getPrevious ()
{
    return previous;
}

    public void setPrevious (String previous)
    {
        this.previous = previous;
    }

    public String getNext ()
{
    return next;
}

    public void setNext (String next)
    {
        this.next = next;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+", previous = "+previous+", next = "+next+"]";
    }

    public class Results
    {
        private String activity_time;

        private Movie movie;

        public String getActivity_time ()
        {
            return activity_time;
        }

        public void setActivity_time (String activity_time)
        {
            this.activity_time = activity_time;
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
            return "ClassPojo [activity_time = "+activity_time+", movie = "+movie+"]";
        }
    }

    public class Movie
    {
        private String id;

        private String title;

        private String score;

        private String img;

        private String first_run_date;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
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
            return "ClassPojo [id = "+id+", title = "+title+", score = "+score+", img = "+img+", first_run_date = "+first_run_date+"]";
        }
    }
}
