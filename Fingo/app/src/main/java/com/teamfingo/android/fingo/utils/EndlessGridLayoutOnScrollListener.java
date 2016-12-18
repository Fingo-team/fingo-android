package com.teamfingo.android.fingo.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by PC on 2016-12-18.
 */

public abstract class EndlessGridLayoutOnScrollListener extends RecyclerView.OnScrollListener{

    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private int scrolledDistance = 0;
    private boolean controlsVisible = false;

    public int previousTotal = 0; // The total number of items in the dataset after the last load

    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.

    private int pastVisibleItems, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager mGridLayoutManager;

    public EndlessGridLayoutOnScrollListener(GridLayoutManager GridLayoutManager) {
        this.mGridLayoutManager = GridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        //firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPosition();
        int firstVisibleItems = 0;
        firstVisibleItems = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisibleItems != 0 && firstVisibleItems > 0) {
            pastVisibleItems = firstVisibleItems;
        }

        if (loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (pastVisibleItems + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }

        if (scrolledDistance > 1 && controlsVisible) {
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -1 && !controlsVisible) {
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onLoadMore(int current_page);

}
