package com.aigestudio.wheelpicker.widgets;

public interface IWheelYearPicker {
    int getCurrentYear();

    int getSelectedYear();

    int getYearEnd();

    int getYearStart();

    void setSelectedYear(int i);

    void setYearEnd(int i);

    void setYearFrame(int i, int i2);

    void setYearStart(int i);
}
