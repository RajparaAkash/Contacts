package com.xyz.danoz.recyclerviewfastscroller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SectionIndexer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.xyz.danoz.recyclerviewfastscroller.calculation.progress.ScrollProgressCalculator;
import com.xyz.danoz.recyclerviewfastscroller.calculation.progress.TouchableScrollProgressCalculator;
import com.xyz.danoz.recyclerviewfastscroller.sectionindicator.SectionIndicator;

public abstract class AbsRecyclerViewFastScroller extends FrameLayout implements RecyclerViewScroller {

    private static final int[] STYLEABLE = R.styleable.AbsRecyclerViewFastScroller;
    protected final View mBar;
    protected final View mHandle;

    private RecyclerView mRecyclerView;
    private SectionIndicator mSectionIndicator;

    protected RecyclerView.OnScrollListener mOnScrollListener;

    public AbsRecyclerViewFastScroller(Context context) {
        this(context, null, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, STYLEABLE, 0, 0);

        try {
            int layoutResource = attributes.getResourceId(R.styleable.AbsRecyclerViewFastScroller_rfs_fast_scroller_layout,
                    getLayoutResourceId());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layoutResource, this, true);

            mBar = findViewById(R.id.scroll_bar);
            mHandle = findViewById(R.id.scroll_handle);

            Drawable barDrawable = attributes.getDrawable(R.styleable.AbsRecyclerViewFastScroller_rfs_barBackground);
//            int barColor = attributes.getColor(R.styleable.AbsRecyclerViewFastScroller_rfs_barColor, Color.GRAY);
//            applyCustomAttributesToView(mBar, barDrawable, barColor);

            Drawable handleDrawable = attributes.getDrawable(R.styleable.AbsRecyclerViewFastScroller_rfs_handleBackground);
//            int handleColor = attributes.getColor(R.styleable.AbsRecyclerViewFastScroller_rfs_handleColor, Color.GRAY);
//            applyCustomAttributesToView(mHandle, handleDrawable, handleColor);
        } finally {
            attributes.recycle();
        }

        setOnTouchListener(new FastScrollerTouchListener(this));
    }

    private void applyCustomAttributesToView(View view, Drawable drawable, int color) {
        if (drawable != null) {
            setViewBackground(view, drawable);
        } else {
            view.setBackgroundColor(color);
        }
    }

    public void setHandleColor(int color) {
        mHandle.setBackgroundColor(color);
    }

    public void setHandleBackground(Drawable drawable) {
        setViewBackground(mHandle, drawable);
    }

    public void setBarColor(int color) {
        mBar.setBackgroundColor(color);
    }

    public void setBarBackground(Drawable drawable) {
        setViewBackground(mBar, drawable);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    private void setViewBackground(View view, Drawable background) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setSectionIndicator(SectionIndicator sectionIndicator) {
        mSectionIndicator = sectionIndicator;
    }

    @Nullable
    public SectionIndicator getSectionIndicator() {
        return mSectionIndicator;
    }

    @Override
    public void scrollTo(float scrollProgress, boolean fromTouch) {
        int position = getPositionFromScrollProgress(scrollProgress);
        mRecyclerView.scrollToPosition(position);

        updateSectionIndicator(position, scrollProgress);
    }

    private void updateSectionIndicator(int position, float scrollProgress) {
        if (mSectionIndicator != null) {
            mSectionIndicator.setProgress(scrollProgress);
            if (mRecyclerView.getAdapter() instanceof SectionIndexer) {
                SectionIndexer indexer = ((SectionIndexer) mRecyclerView.getAdapter());
                int section = indexer.getSectionForPosition(position);
                Object[] sections = indexer.getSections();
                mSectionIndicator.setSection(sections[section]);
            }
        }
    }

    private int getPositionFromScrollProgress(float scrollProgress) {
        return (int) (mRecyclerView.getAdapter().getItemCount() * scrollProgress);
    }

    @NonNull
    public RecyclerView.OnScrollListener getOnScrollListener() {
        if (mOnScrollListener == null) {
            mOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    float scrollProgress = 0;
                    ScrollProgressCalculator scrollProgressCalculator = getScrollProgressCalculator();
                    if (scrollProgressCalculator != null) {
                        scrollProgress = scrollProgressCalculator.calculateScrollProgress(recyclerView);
                    }
                    moveHandleToPosition(scrollProgress);
                }
            };
        }
        return mOnScrollListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getScrollProgressCalculator() == null) {
            onCreateScrollProgressCalculator();
        }

        // synchronize the handle position to the RecyclerView
        float scrollProgress = getScrollProgressCalculator().calculateScrollProgress(mRecyclerView);
        moveHandleToPosition(scrollProgress);
    }

    protected abstract void onCreateScrollProgressCalculator();

    public float getScrollProgress(MotionEvent event) {
        ScrollProgressCalculator scrollProgressCalculator = getScrollProgressCalculator();
        if (scrollProgressCalculator != null) {
            return getScrollProgressCalculator().calculateScrollProgress(event);
        }
        return 0;
    }

    protected abstract int getLayoutResourceId();

    @Nullable
    protected abstract TouchableScrollProgressCalculator getScrollProgressCalculator();

    public abstract void moveHandleToPosition(float scrollProgress);

}