package com.xyz.danoz.recyclerviewfastscroller.calculation.progress;

import android.view.MotionEvent;

public interface TouchableScrollProgressCalculator extends ScrollProgressCalculator {

    public float calculateScrollProgress(MotionEvent event);

}
