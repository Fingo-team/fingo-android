package com.teamfingo.android.fingo.model;

/**
 * Created by taewon on 2016-12-08.
 */

public class UserComments {
    private Results[] results;

    private String previous;

    private String next;

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "ClassPojo [results = " + results + ", previous = " + previous + ", next = " + next + "]";
    }

    public class Results {
        private String activity_time;

        private float score;

        private Movie movie;

        private String comment;

        private User user;

        public String getActivity_time() {
            return activity_time;
        }

        public void setActivity_time(String activity_time) {
            this.activity_time = activity_time;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public Movie getMovie() {
            return movie;
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "ClassPojo [activity_time = " + activity_time + ", score = " + score + ", movie = " + movie + ", comment = " + comment + ", user = " + user + "]";
        }
    }

    public class Movie {
        private String id;

        private String title;

        private Stillcut[] stillcut;

        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Stillcut[] getStillcut() {
            return stillcut;
        }

        public void setStillcut(Stillcut[] stillcut) {
            this.stillcut = stillcut;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public String toString() {
            return "ClassPojo [id = " + id + ", title = " + title + ", stillcut = " + stillcut + ", img = " + img + "]";
        }
    }

    public class Stillcut {

        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public String toString() {
            return "ClassPojo [img = " + img + "]";
        }
    }

    public class User
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