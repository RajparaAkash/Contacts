package com.aigestudio.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.aigestudio.wheelpicker.WheelPicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WheelYearPicker extends WheelPicker implements IWheelYearPicker {
    private int mSelectedYear;
    private int mYearEnd;
    private int mYearStart;

    public WheelYearPicker(Context context) {
        this(context, null);
    }

    public WheelYearPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mYearStart = 1000;
        this.mYearEnd = PathInterpolatorCompat.MAX_NUM_POINTS;
        updateYears();
        this.mSelectedYear = Calendar.getInstance().get(1);
        updateSelectedYear();
    }

    private void updateYears() {
        ArrayList arrayList = new ArrayList();
        for (int i = this.mYearStart; i <= this.mYearEnd; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        super.setData(arrayList);
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(this.mSelectedYear - this.mYearStart);
    }

    @Override // com.aigestudio.wheelpicker.WheelPicker, com.aigestudio.wheelpicker.IWheelPicker
    public void setData(List list) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelYearPicker");
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public void setYearFrame(int i, int i2) {
        this.mYearStart = i;
        this.mYearEnd = i2;
        this.mSelectedYear = getCurrentYear();
        updateYears();
        updateSelectedYear();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public int getYearStart() {
        return this.mYearStart;
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public void setYearStart(int i) {
        this.mYearStart = i;
        this.mSelectedYear = getCurrentYear();
        updateYears();
        updateSelectedYear();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public int getYearEnd() {
        return this.mYearEnd;
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public void setYearEnd(int i) {
        this.mYearEnd = i;
        updateYears();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public int getSelectedYear() {
        return this.mSelectedYear;
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public void setSelectedYear(int i) {
        this.mSelectedYear = i;
        updateSelectedYear();
    }

    @Override // com.aigestudio.wheelpicker.widgets.IWheelYearPicker
    public int getCurrentYear() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition()))).intValue();
    }
}
