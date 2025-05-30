package com.aigestudio.wheelpicker.widgets;

public interface IWheelDayPicker {
    int getCurrentDay();

    int getMonth();

    int getSelectedDay();

    int getYear();

    void setMonth(int i);

    void setSelectedDay(int i);

    void setYear(int i);

    void setYearAndMonth(int i, int i2);
}
