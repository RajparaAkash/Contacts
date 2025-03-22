package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import kotlin.Unit;

public class StickHeaderItemDecoration extends RecyclerView.ItemDecoration {
    private final StickyHeaderInterface mListener;
    private int mStickyHeaderHeight;

    public StickHeaderItemDecoration(StickyHeaderInterface stickyHeaderInterface) {
        this.mListener = stickyHeaderInterface;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        int childAdapterPosition;
        super.onDrawOver(canvas, recyclerView, state);
        View childAt = recyclerView.getChildAt(0);
        if (childAt != null && (childAdapterPosition = recyclerView.getChildAdapterPosition(childAt)) != -1) {
            int headerPositionForItem = this.mListener.getHeaderPositionForItem(childAdapterPosition);
            View headerViewForItem = getHeaderViewForItem(headerPositionForItem, recyclerView);
            fixLayoutSize(recyclerView, headerViewForItem);
            View childInContact = getChildInContact(recyclerView, headerViewForItem.getBottom(), headerPositionForItem);
            if (childInContact == null || !this.mListener.isHeader(recyclerView.getChildAdapterPosition(childInContact))) {
                drawHeader(canvas, headerViewForItem);
            } else {
                moveHeader(canvas, headerViewForItem, childInContact);
            }
        }
    }

    private View getHeaderViewForItem(int i, RecyclerView recyclerView) {
        View inflate = LayoutInflater.from(recyclerView.getContext()).inflate(this.mListener.getHeaderLayout(i), (ViewGroup) recyclerView, false);
        this.mListener.bindHeaderData(inflate, i);
        return inflate;
    }

    private void drawHeader(Canvas canvas, View view) {
        canvas.save();
        canvas.translate(0.0f, 0.0f);
        view.draw(canvas);
        canvas.restore();
    }

    private void moveHeader(Canvas canvas, View view, View view2) {
        canvas.save();
        canvas.translate(0.0f, (float) (view2.getTop() - view.getHeight()));
        view.draw(canvas);
        canvas.restore();
    }

    private View getChildInContact(RecyclerView recyclerView, int i, int i2) {
        int i3;
        int childCount = recyclerView.getChildCount();
        int i4 = 0;
        while (i4 < childCount) {
            View childAt = recyclerView.getChildAt(i4);
            int height = (i2 == i4 || !this.mListener.isHeader(recyclerView.getChildAdapterPosition(childAt))) ? 0 : this.mStickyHeaderHeight - childAt.getHeight();
            if (childAt.getTop() > 0) {
                i3 = childAt.getBottom() + height;
            } else {
                i3 = childAt.getBottom();
            }
            if (i3 > i && childAt.getTop() <= i) {
                return childAt;
            }
            i4++;
        }
        return null;
    }

    private void fixLayoutSize(ViewGroup viewGroup, View view) {
        view.measure(ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(viewGroup.getWidth(), 1073741824), viewGroup.getPaddingLeft() + viewGroup.getPaddingRight(), view.getLayoutParams().width), ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(viewGroup.getHeight(), 0), viewGroup.getPaddingTop() + viewGroup.getPaddingBottom(), view.getLayoutParams().height));
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        this.mStickyHeaderHeight = measuredHeight;
        Unit unit = Unit.INSTANCE;
        view.layout(0, 0, measuredWidth, measuredHeight);
    }

    public interface StickyHeaderInterface {
        void bindHeaderData(View view, int i);

        int getHeaderLayout(int i);

        int getHeaderPositionForItem(int i);

        boolean isHeader(int i);
    }

}
