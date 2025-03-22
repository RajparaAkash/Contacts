package com.contacts.phonecontact.phonebook.dialer.DialerCode;

import android.view.MotionEvent;
import android.view.View;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.service.SwipeEvents;

public class CallerIdPopupInitView2 implements View.OnTouchListener {
    final CallerIdPopup this$0;
    private float initialTouchX;
    private float initialTouchY;
    private int initialX;
    private int initialY;
    private float lastX;
    private float lastY;

    CallerIdPopupInitView2(CallerIdPopup callerIdPopup) {
        this.this$0 = callerIdPopup;
    }


    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    return false;
                }
                float translationX = view.getTranslationX() + (motionEvent.getRawX() - this.lastX);
                view.setAlpha(((float) 1) - (Math.abs(translationX) / ((float) (view.getWidth() / 2))));
                view.setTranslationX(translationX);
                this.lastX = motionEvent.getRawX();
                this.lastY = motionEvent.getRawY();
                this$0.getParams().x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                this$0.getParams().y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));
                int width = view.getWidth() / 2;
                if (view.getTranslationX() > ((float) width) || view.getTranslationX() < ((float) (-width))) {
                    SwipeEvents.SwipeDirection swipeDirection = view.getTranslationX() > 0.0f ? SwipeEvents.SwipeDirection.RIGHT : SwipeEvents.SwipeDirection.LEFT;
                    this$0.dismissPopUp(swipeDirection, this$0.activityCallerIdBinding.getRoot());
                } else {
                    this$0.getWindowManager().updateViewLayout(this$0.activityCallerIdBinding.getRoot(), this$0.getParams());
                }
            }
            return true;
        }
        lastX = motionEvent.getRawX();
        lastY = motionEvent.getRawY();
        initialX = this$0.getParams().x;
        initialY = this$0.getParams().y;
        initialTouchX = motionEvent.getRawX();
        initialTouchY = motionEvent.getRawY();
        return true;
    }

}
