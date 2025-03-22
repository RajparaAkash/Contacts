package com.xyz.danoz.recyclerviewfastscroller.sectionindicator.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.contacts.phonecontact.phonebook.dialer.R;

import com.xyz.danoz.recyclerviewfastscroller.sectionindicator.AbsSectionIndicator;

public abstract class SectionTitleIndicator<T> extends AbsSectionIndicator<T> {

    private static final int[] STYLEABLE = R.styleable.SectionTitleIndicator;
    private static final int DEFAULT_TITLE_INDICATOR_LAYOUT = R.layout.section_indicator_with_title;
    private static final int DEFAULT_BACKGROUND_COLOR = android.R.color.darker_gray;
    private static final int DEFAULT_TEXT_COLOR = android.R.color.white;

    private final View mIndicatorBackground;
    private final TextView mTitleText;

    public SectionTitleIndicator(Context context) {
        this(context, null);
    }

    public SectionTitleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionTitleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorBackground = findViewById(R.id.section_title_popup);
        mTitleText = (TextView) findViewById(R.id.section_indicator_text);

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, STYLEABLE, 0, 0);
        try {
            int customBackgroundColor =
                    attributes.getColor(R.styleable.SectionTitleIndicator_rfs_backgroundColor, getDefaultBackgroundColor());
            applyCustomBackgroundColorAttribute(customBackgroundColor);

            int customTextColor =
                    attributes.getColor(R.styleable.SectionTitleIndicator_rfs_textColor, getDefaultBackgroundColor());
            applyCustomTextColorAttribute(customTextColor);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected int getDefaultLayoutId() {
        return DEFAULT_TITLE_INDICATOR_LAYOUT;
    }

    protected int getDefaultBackgroundColor() {
        return DEFAULT_BACKGROUND_COLOR;
    }

    protected int getDefaultTextColor() {
        return DEFAULT_TEXT_COLOR;
    }

    @Override
    protected void applyCustomBackgroundColorAttribute(int color) {
        setIndicatorBackgroundColor(color);
    }

    public void setIndicatorBackgroundColor(int color) {
        Drawable backgroundDrawable = mIndicatorBackground.getBackground();

        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable backgroundShape = (GradientDrawable) backgroundDrawable;
            backgroundShape.setColor(color);
        } else {
          mIndicatorBackground.setBackgroundColor(color);
        }
    }

    protected void applyCustomTextColorAttribute(int color) {
        setIndicatorTextColor(color);
    }

    public void setIndicatorTextColor(int color) {
        mTitleText.setTextColor(color);
    }

    public void setTitleText(String text) {
        mTitleText.setText(text);
    }

}
