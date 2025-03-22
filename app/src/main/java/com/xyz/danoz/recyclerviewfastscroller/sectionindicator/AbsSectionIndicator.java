package com.xyz.danoz.recyclerviewfastscroller.sectionindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.xyz.danoz.recyclerviewfastscroller.calculation.VerticalScrollBoundsProvider;
import com.xyz.danoz.recyclerviewfastscroller.calculation.position.VerticalScreenPositionCalculator;
import com.xyz.danoz.recyclerviewfastscroller.sectionindicator.animation.DefaultSectionIndicatorAlphaAnimator;

public abstract class AbsSectionIndicator<T> extends FrameLayout implements SectionIndicator<T> {

    private static final int[] STYLEABLE = R.styleable.AbsSectionIndicator;

    private VerticalScreenPositionCalculator mScreenPositionCalculator;
    private DefaultSectionIndicatorAlphaAnimator mDefaultSectionIndicatorAlphaAnimator;

    public AbsSectionIndicator(Context context) {
        this(context, null);
    }

    public AbsSectionIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsSectionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, STYLEABLE, 0, 0);
        try {
            int layoutId = attributes.getResourceId(R.styleable.AbsSectionIndicator_rfs_section_indicator_layout, getDefaultLayoutId());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layoutId, this, true);
        } finally {
            attributes.recycle();
        }

        mDefaultSectionIndicatorAlphaAnimator = new DefaultSectionIndicatorAlphaAnimator(this);
    }

    protected abstract int getDefaultLayoutId();

    protected abstract int getDefaultBackgroundColor();

    protected abstract void applyCustomBackgroundColorAttribute(int color);

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mScreenPositionCalculator == null) {
            VerticalScrollBoundsProvider boundsProvider =
                    new VerticalScrollBoundsProvider(0, ((ViewGroup) getParent()).getHeight() - getHeight());
            mScreenPositionCalculator = new VerticalScreenPositionCalculator(boundsProvider);
        }
    }

    @Override
    public void setProgress(float progress) {
        setY(mScreenPositionCalculator.getYPositionFromScrollProgress(progress));
    }

    @Override
    public void animateAlpha(float targetAlpha) {
        mDefaultSectionIndicatorAlphaAnimator.animateTo(targetAlpha);
    }

    @Override
    public abstract void setSection(T object);
}
