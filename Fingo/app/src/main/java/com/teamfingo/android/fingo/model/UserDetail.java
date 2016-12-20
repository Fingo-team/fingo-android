package com.teamfingo.android.fingo.model;

/**
 * Created by taewon on 2016-12-08.
 */

public class UserDetail
{
    private String watched_movie_cnt;

    private User_profile user_profile;

    private String comment_cnt;

    private String wish_movie_cnt;

    public String getWatched_movie_cnt ()
    {
        return watched_movie_cnt;
    }

    public void setWatched_movie_cnt (String watched_movie_cnt)
    {
        this.watched_movie_cnt = watched_movie_cnt;
    }

    public User_profile getUser_profile ()
    {
        return user_profile;
    }

    public void setUser_profile (User_profile user_profile)
    {
        this.user_profile = user_profile;
    }

    public String getComment_cnt ()
    {
        return comment_cnt;
    }

    public void setComment_cnt (String comment_cnt)
    {
        this.comment_cnt = comment_cnt;
    }

    public String getWish_movie_cnt ()
    {
        return wish_movie_cnt;
    }

    public void setWish_movie_cnt (String wish_movie_cnt)
    {
        this.wish_movie_cnt = wish_movie_cnt;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [watched_movie_cnt = "+watched_movie_cnt+", user_profile = "+user_profile+", comment_cnt = "+comment_cnt+", wish_movie_cnt = "+wish_movie_cnt+"]";
    }

    public class User_profile
    {
        private String cover_img_url;

        private String id;

        private String user_img_url;

        private String level;

        private String nickname;

        public String getCover_img_url ()
        {
            return cover_img_url;
        }

        public void setCover_img_url (String cover_img_url)
        {
            this.cover_img_url = cover_img_url;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getUser_img_url ()
        {
            return user_img_url;
        }

        public void setUser_img_url (String user_img_url)
        {
            this.user_img_url = user_img_url;
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

        @Override
        public String toString()
        {
            return "ClassPojo [cover_img_url = "+cover_img_url+", id = "+id+", user_img_url = "+user_img_url+", level = "+level+", nickname = "+nickname+"]";
        }
    }
}