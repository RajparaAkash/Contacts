package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 5000.0f;

    public LinearLayoutManagerWithSmoothScroller(Context context) {
        super(context);
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: mahi.phone.call.contactbook.ContactUtills.LinearLayoutManagerWithSmoothScroller.1

            @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
            public PointF computeScrollVectorForPosition(int i2) {
                return super.computeScrollVectorForPosition(i2);
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return LinearLayoutManagerWithSmoothScroller.MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        linearSmoothScroller.setTargetPosition(i);
        startSmoothScroll(linearSmoothScroller);
    }
}
