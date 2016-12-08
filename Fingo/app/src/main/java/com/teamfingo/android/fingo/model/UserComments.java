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

    public class User {
        private String id;

        private String level;

        private String nickname;

        private String user_img;

        private String cover_img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getCover_img() {
            return cover_img;
        }

        public void setCover_img(String cover_img) {
            this.cover_img = cover_img;
        }

        @Override
        public String toString() {
            return "ClassPojo [id = " + id + ", level = " + level + ", nickname = " + nickname + ", user_img = " + user_img + ", cover_img = " + cover_img + "]";
        }
    }

}