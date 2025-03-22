package com.xyz.danoz.recyclerviewfastscroller;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerViewScroller {

    public void setRecyclerView(RecyclerView recyclerView);

    public RecyclerView.OnScrollListener getOnScrollListener();

    public void scrollTo(float scrollProgress, boolean fromTouch);

}
