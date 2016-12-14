package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 1..
 */

public class Movie
{
    private Genre[] genre;

    private String story;

    private Nation_code[] nation_code;

    private String title;

    private Stillcut[] stillcut;

    private String score;

    private String img;

    private String first_run_date;

    private Actor[] actor;

    private Director[] director;

    public Genre[] getGenre ()
    {
        return genre;
    }

    public void setGenre (Genre[] genre)
    {
        this.genre = genre;
    }

    public String getStory ()
    {
        return story;
    }

    public void setStory (String story)
    {
        this.story = story;
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

    public Stillcut[] getStillcut ()
    {
        return stillcut;
    }

    public void setStillcut (Stillcut[] stillcut)
    {
        this.stillcut = stillcut;
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

    public Actor[] getActor ()
    {
        return actor;
    }

    public void setActor (Actor[] actor)
    {
        this.actor = actor;
    }

    public Director[] getDirector ()
    {
        return director;
    }

    public void setDirector (Director[] director)
    {
        this.director = director;
    }

    @Override
    public String toString()
    {
        return "[genre = "+genre+", story = "+story+", nation_code = "+nation_code+", title = "+title+", stillcut = "+stillcut+", score = "+score+", img = "+img+", first_run_date = "+first_run_date+", actor = "+actor+", director = "+director+"]";
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
            return "[name = "+name+", img = "+img+"]";
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
            return name + img;
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
            return name;
        }
    }

    public class Stillcut
    {
        private String img;

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
            return img;
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