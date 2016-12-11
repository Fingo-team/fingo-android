package com.teamfingo.android.fingo.model;

/**
 * Created by Jaemin on 2016. 12. 9..
 */

public class MovieComment
{
    private String score;

    private String comment;

    private User user;

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    public String getComment ()
    {
    return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [score = "+score+", comment = "+comment+", user = "+user+"]";
    }

    public class User
    {
        private String id;

        private String level;

        private String nickname;

        private String user_img;

        private String cover_img;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getLevel ()
        {
            return level;
        }

        public void setLevel (String level)
        {
            this.level = level;
        }

        public String getNickname ()
        {
            return nickname;
        }

        public void setNickname (String nickname)
        {
            this.nickname = nickname;
        }

        public String getUser_img ()
        {
            return user_img;
        }

        public void setUser_img (String user_img)
        {
            this.user_img = user_img;
        }

        public String getCover_img ()
        {
            return cover_img;
        }

        public void setCover_img (String cover_img)
        {
            this.cover_img = cover_img;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", level = "+level+", nickname = "+nickname+", user_img = "+user_img+", cover_img = "+cover_img+"]";
        }
    }

}
