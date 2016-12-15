package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 15..
 */

public class Statistics
{
    private Scores scores;

    private Genres[] genres;

    private Actors[] actors;

    private Directors[] directors;

    private Nations[] nations;

    public Scores getScores ()
    {
        return scores;
    }

    public void setScores (Scores scores)
    {
        this.scores = scores;
    }

    public Genres[] getGenres ()
    {
        return genres;
    }

    public void setGenres (Genres[] genres)
    {
        this.genres = genres;
    }

    public Actors[] getActors ()
    {
        return actors;
    }

    public void setActors (Actors[] actors)
    {
        this.actors = actors;
    }

    public Directors[] getDirectors ()
    {
        return directors;
    }

    public void setDirectors (Directors[] directors)
    {
        this.directors = directors;
    }

    public Nations[] getNations ()
    {
        return nations;
    }

    public void setNations (Nations[] nations)
    {
        this.nations = nations;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [scores = "+scores+", genres = "+genres+", actors = "+actors+", directors = "+directors+", nations = "+nations+"]";
    }

    public class Genres
    {
        private Genre genre;

        private String count;

        public Genre getGenre ()
        {
            return genre;
        }

        public void setGenre (Genre genre)
        {
            this.genre = genre;
        }

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [genre = "+genre+", count = "+count+"]";
        }
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
            return "ClassPojo [name = "+name+"]";
        }
    }

    public class Actors
    {
        private String count;

        private Actor actor;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public Actor getActor ()
        {
            return actor;
        }

        public void setActor (Actor actor)
        {
            this.actor = actor;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [count = "+count+", actor = "+actor+"]";
        }
    }

    public class Actor
    {
        private String name;

        private String img;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getImg ()
        {
            return img;
        }

        public void setImg (String img)
        {
            this.img = img;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [name = "+name+", img = "+img+"]";
        }
    }

    public class Nations
    {
        private String count;

        private Nation nation;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public Nation getNation ()
        {
            return nation;
        }

        public void setNation (Nation nation)
        {
            this.nation = nation;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [count = "+count+", nation = "+nation+"]";
        }
    }

    public class Nation
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
            return "ClassPojo [name = "+name+"]";
        }
    }

    public class User_statistics
    {
        private String user;

        private String movie_count;

        public String getUser ()
        {
            return user;
        }

        public void setUser (String user)
        {
            this.user = user;
        }

        public String getMovie_count ()
        {
            return movie_count;
        }

        public void setMovie_count (String movie_count)
        {
            this.movie_count = movie_count;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [user = "+user+", movie_count = "+movie_count+"]";
        }
    }


    public class Scores
    {
        private String two;

        private String five;

        private String two_point_five;

        private String point_five;

        private String four_point_five;

        private String one;

        private String three;

        private String four;

        private String one_point_five;

        private String three_point_five;

        private User_statistics user_statistics;

        public String getTwo ()
        {
            return two;
        }

        public void setTwo (String two)
        {
            this.two = two;
        }

        public String getFive ()
        {
            return five;
        }

        public void setFive (String five)
        {
            this.five = five;
        }

        public String getTwo_point_five ()
        {
            return two_point_five;
        }

        public void setTwo_point_five (String two_point_five)
        {
            this.two_point_five = two_point_five;
        }

        public String getPoint_five ()
        {
            return point_five;
        }

        public void setPoint_five (String point_five)
        {
            this.point_five = point_five;
        }

        public String getFour_point_five ()
        {
            return four_point_five;
        }

        public void setFour_point_five (String four_point_five)
        {
            this.four_point_five = four_point_five;
        }

        public String getOne ()
        {
            return one;
        }

        public void setOne (String one)
        {
            this.one = one;
        }

        public String getThree ()
        {
            return three;
        }

        public void setThree (String three)
        {
            this.three = three;
        }

        public String getFour ()
        {
            return four;
        }

        public void setFour (String four)
        {
            this.four = four;
        }

        public String getOne_point_five ()
        {
            return one_point_five;
        }

        public void setOne_point_five (String one_point_five)
        {
            this.one_point_five = one_point_five;
        }

        public String getThree_point_five ()
        {
            return three_point_five;
        }

        public void setThree_point_five (String three_point_five)
        {
            this.three_point_five = three_point_five;
        }

        public User_statistics getUser_statistics ()
        {
            return user_statistics;
        }

        public void setUser_statistics (User_statistics user_statistics)
        {
            this.user_statistics = user_statistics;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [two = "+two+", five = "+five+", two_point_five = "+two_point_five+", point_five = "+point_five+", four_point_five = "+four_point_five+", one = "+one+", three = "+three+", four = "+four+", one_point_five = "+one_point_five+", three_point_five = "+three_point_five+", user_statistics = "+user_statistics+"]";
        }
    }

    public class Directors
    {
        private String count;

        private Director director;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public Director getDirector ()
        {
            return director;
        }

        public void setDirector (Director director)
        {
            this.director = director;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [count = "+count+", director = "+director+"]";
        }
    }

    public class Director
    {
        private String name;

        private String img;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getImg ()
        {
            return img;
        }

        public void setImg (String img)
        {
            this.img = img;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [name = "+name+", img = "+img+"]";
        }
    }
}
