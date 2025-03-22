package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common.Utils;
import com.contacts.phonecontact.phonebook.dialer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateTimePicker extends FrameLayout {
    private static final String TAG = "DateTimePicker";
    private Context context;
    private OnDateChangeListener dateChangeListener;
    private List<String> dateList;
    private WheelPicker datePicker;
    private int daysForward = 30;
    private WheelPicker hourPicker;
    private List<String> hoursList;
    private int itemsDistanceBottomDate = -86;
    private int itemsDistanceBottomHours = -86;
    private int itemsDistanceBottomMinutes = -86;
    private int itemsDistanceDate = 86;
    private int itemsDistanceHours = 86;
    private int itemsDistanceMinutes = 86;
    private int itemsDistanceTopDate = 86;
    private int itemsDistanceTopHours = 86;
    private int itemsDistanceTopMinutes = 86;
    private ConstraintLayout layout;
    private List<String> minutesList;
    private WheelPicker minutesPicker;
    private long selectedDate = 0;

    public DateTimePicker(Context context2) {
        super(context2);
        this.context = context2;
        init();
    }

    public DateTimePicker(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        init();
    }

    public DateTimePicker(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        this.context = context2;
        init();
    }

    public void vibrate() {
    }

    private void init() {
        setLayoutParams(new LayoutParams(-1, -2));
        layout = (ConstraintLayout) FrameLayout.inflate(getContext(), R.layout.layout_time_date_picker, null);
        datePicker = (WheelPicker) layout.findViewById(R.id.date_picker);
        hourPicker = (WheelPicker) layout.findViewById(R.id.hour_picker);
        minutesPicker = (WheelPicker) layout.findViewById(R.id.minutes_picker);
        datePicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {

            @Override
            public void onWheelScrollStateChanged(int i) {
            }

            @Override
            public void onWheelScrolled(int i) {
                if (i > itemsDistanceTopDate) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopDate += itemsDistanceDate;
                    DateTimePicker.this.itemsDistanceBottomDate += itemsDistanceDate;
                } else if (i < itemsDistanceBottomDate) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopDate -= itemsDistanceDate;
                    DateTimePicker.this.itemsDistanceBottomDate -= itemsDistanceDate;
                }
            }

            @Override
            public void onWheelSelected(int i) {
                if (dateChangeListener != null) {
                    dateChangeListener.vor(getDate());
                }
            }
        });
        hourPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {

            @Override
            public void onWheelScrollStateChanged(int i) {
            }

            @Override
            public void onWheelScrolled(int i) {
                if (i > itemsDistanceTopHours) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopHours += itemsDistanceHours;
                    DateTimePicker.this.itemsDistanceBottomHours += itemsDistanceHours;
                } else if (i < itemsDistanceBottomHours) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopHours -= itemsDistanceHours;
                    DateTimePicker.this.itemsDistanceBottomHours -= itemsDistanceHours;
                }
            }

            @Override
            public void onWheelSelected(int i) {
                if (dateChangeListener != null) {
                    dateChangeListener.vor(getDate());
                }
            }
        });
        minutesPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {

            @Override
            public void onWheelScrollStateChanged(int i) {
            }

            @Override
            public void onWheelScrolled(int i) {
                if (i > itemsDistanceTopMinutes) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopMinutes += itemsDistanceMinutes;
                    DateTimePicker.this.itemsDistanceBottomMinutes += itemsDistanceMinutes;
                } else if (i < itemsDistanceBottomMinutes) {
                    vibrate();
                    DateTimePicker.this.itemsDistanceTopMinutes -= itemsDistanceMinutes;
                    DateTimePicker.this.itemsDistanceBottomMinutes -= itemsDistanceMinutes;
                }
            }

            @Override
            public void onWheelSelected(int i) {
                if (dateChangeListener != null) {
                    dateChangeListener.vor(getDate());
                }
            }
        });
        dateList = addDatesToList();
        hoursList = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutesList = new ArrayList();
        for (int i = 0; i < 60; i += 5) {
            minutesList.add(String.format("%02d", Integer.valueOf(i)));
        }
        datePicker.setData(dateList);
        hourPicker.setData(hoursList);
        minutesPicker.setData(minutesList);
        addView(layout, new LayoutParams(-1, -2));
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                Log.e(DateTimePicker.TAG, "onGlobalLayout: setData " + selectedDate);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (selectedDate > 0) {
                    setDate(selectedDate);
                }
            }
        });
    }

    public void setDaysForward(int i) {
        this.daysForward = i;
        init();
    }

    public List<String> addDatesToList() {
        ArrayList arrayList = new ArrayList();
        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < daysForward - 2; i++) {
            arrayList.add(Utils.getPrettyDate(context, instance.getTimeInMillis()));
            instance.add(6, 1);
        }
        return arrayList;
    }

    public long getDate() {
        Calendar instance = Calendar.getInstance();
        instance.set(11, hourPicker.getCurrentItemPosition());
        instance.set(12, minutesPicker.getCurrentItemPosition() * 5);
        instance.roll(6, datePicker.getCurrentItemPosition());
        instance.set(13, 0);
        return instance.getTimeInMillis();
    }

    public void setDate(long j) {
        int i;
        selectedDate = j;
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        Calendar instance2 = Calendar.getInstance();
        int i2 = instance.get(11);
        int i3 = instance.get(12) / 5;
        if (instance.get(6) == instance2.get(6)) {
            i = 0;
        } else if (instance.get(6) - 1 == instance2.get(6)) {
            i = 1;
        } else {
            i = datePicker.getData().indexOf(Utils.getPrettyDate(context, instance.getTimeInMillis()));
        }
        Log.e(TAG, "setDate: " + i2 + ", " + i3 + ", " + i);
        hourPicker.setSelectedItemPosition(i2, true);
        minutesPicker.setSelectedItemPosition(i3, true);
        datePicker.setSelectedItemPosition(i, true);
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.dateChangeListener = onDateChangeListener;
    }

    public interface OnDateChangeListener {
        void vor(long j);
    }

}
