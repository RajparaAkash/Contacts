package com.xyz.danoz.recyclerviewfastscroller.sectionindicator;

import android.widget.SectionIndexer;

public interface SectionIndicator<T> {

    public void setProgress(float progress);

    public void setSection(T section);

    public void animateAlpha(float targetAlpha);
}
