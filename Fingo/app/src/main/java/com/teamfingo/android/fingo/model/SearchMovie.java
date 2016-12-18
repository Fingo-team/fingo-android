package com.teamfingo.android.fingo.model;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 12. 18..
 */

public class SearchMovie {
    private String next;
    private String previous;
    private ArrayList<Movie> results;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
