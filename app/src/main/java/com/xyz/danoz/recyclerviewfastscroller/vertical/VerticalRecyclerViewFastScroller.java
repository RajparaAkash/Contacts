package com.xyz.danoz.recyclerviewfastscroller.vertical;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.xyz.danoz.recyclerviewfastscroller.AbsRecyclerViewFastScroller;
import com.xyz.danoz.recyclerviewfastscroller.RecyclerViewScroller;
import com.xyz.danoz.recyclerviewfastscroller.calculation.VerticalScrollBoundsProvider;
import com.xyz.danoz.recyclerviewfastscroller.calculation.position.VerticalScreenPositionCalculator;
import com.xyz.danoz.recyclerviewfastscroller.calculation.progress.TouchableScrollProgressCalculator;
import com.xyz.danoz.recyclerviewfastscroller.calculation.progress.VerticalLinearLayoutManagerScrollProgressCalculator;
import com.xyz.danoz.recyclerviewfastscroller.calculation.progress.VerticalScrollProgressCalculator;

public class VerticalRecyclerViewFastScroller extends AbsRecyclerViewFastScroller implements RecyclerViewScroller {

    @Nullable private VerticalScrollProgressCalculator mScrollProgressCalculator;
    @Nullable
    private VerticalScreenPositionCalculator mScreenPositionCalculator;

    public VerticalRecyclerViewFastScroller(Context context) {
        this(context, null);
    }

    public VerticalRecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalRecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.vertical_recycler_fast_scroller_layout;
    }

    @Override
    @Nullable
    protected TouchableScrollProgressCalculator getScrollProgressCalculator() {
        return mScrollProgressCalculator;
    }

    @Override
    public void moveHandleToPosition(float scrollProgress) {
        if (mScreenPositionCalculator == null) {
            return;
        }
        mHandle.setY(mScreenPositionCalculator.getYPositionFromScrollProgress(scrollProgress));
    }

    protected void onCreateScrollProgressCalculator() {
        VerticalScrollBoundsProvider boundsProvider =
                new VerticalScrollBoundsProvider(mBar.getY(), mBar.getY() + mBar.getHeight() - mHandle.getHeight());
        mScrollProgressCalculator = new VerticalLinearLayoutManagerScrollProgressCalculator(boundsProvider);
        mScreenPositionCalculator = new VerticalScreenPositionCalculator(boundsProvider);
    }
}
