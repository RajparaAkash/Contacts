package com.contacts.phonecontact.phonebook.dialer.DialerCode.service;

import android.view.MotionEvent;
import android.view.View;

public class SwipeEvents implements View.OnTouchListener {
    private SwipeDirection detectSwipeDirection;
    private SwipeCallback swipeCallback;
    private SwipeSingleCallback swipeSingleCallback;
    View view;
    float x1;
    float x2;
    float y1;
    float y2;

    public interface SwipeCallback {
        void onClick(View view);

        void onSwipeBottom();

        void onSwipeLeft();

        void onSwipeRight();

        void onSwipeTop();
    }

    public enum SwipeDirection {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public interface SwipeSingleCallback {
        void onSwipe();
    }

    private static SwipeEvents newInstance() {
        return new SwipeEvents();
    }

    public static void detect(View view2, SwipeCallback swipeCallback2) {
        SwipeEvents newInstance = newInstance();
        newInstance.swipeCallback = swipeCallback2;
        newInstance.view = view2;
        newInstance.detect();
    }

    public static void detectTop(View view2, SwipeSingleCallback swipeSingleCallback2) {
        SwipeEvents newInstance = newInstance();
        newInstance.swipeSingleCallback = swipeSingleCallback2;
        newInstance.view = view2;
        newInstance.detectSingle(SwipeDirection.TOP);
    }

    public static void detectRight(View view2, SwipeSingleCallback swipeSingleCallback2) {
        SwipeEvents newInstance = newInstance();
        newInstance.swipeSingleCallback = swipeSingleCallback2;
        newInstance.view = view2;
        newInstance.detectSingle(SwipeDirection.RIGHT);
    }

    public static void detectBottom(View view2, SwipeSingleCallback swipeSingleCallback2) {
        SwipeEvents newInstance = newInstance();
        newInstance.swipeSingleCallback = swipeSingleCallback2;
        newInstance.view = view2;
        newInstance.detectSingle(SwipeDirection.BOTTOM);
    }

    public static void detectLeft(View view2, SwipeSingleCallback swipeSingleCallback2) {
        SwipeEvents newInstance = newInstance();
        newInstance.swipeSingleCallback = swipeSingleCallback2;
        newInstance.view = view2;
        newInstance.detectSingle(SwipeDirection.LEFT);
    }

    private void detect() {
        this.view.setOnTouchListener(this);
    }

    private void detectSingle(SwipeDirection swipeDirection) {
        this.detectSwipeDirection = swipeDirection;
        this.view.setOnTouchListener(this);
    }

    public boolean onTouch(View view2, MotionEvent motionEvent) {
        SwipeSingleCallback swipeSingleCallback2;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.x1 = motionEvent.getX();
            this.y1 = motionEvent.getY();
            return false;
        } else if (action != 1) {
            return false;
        } else {
            this.x2 = motionEvent.getX();
            float y = motionEvent.getY();
            this.y2 = y;
            SwipeDirection swipeDirection = null;
            if (Math.abs(this.x2 - this.x1) > Math.abs(y - this.y1)) {
                if (Math.abs(this.x2 - this.x1) < 150.0f) {
                    this.swipeCallback.onClick(view2);
                } else {
                    swipeDirection = this.x1 < this.x2 ? SwipeDirection.RIGHT : SwipeDirection.LEFT;
                }
            } else if (Math.abs(this.y2 - this.y1) < 100.0f) {
                this.swipeCallback.onClick(view2);
            } else {
                swipeDirection = this.y1 > this.y2 ? SwipeDirection.TOP : SwipeDirection.BOTTOM;
            }
            SwipeDirection swipeDirection2 = this.detectSwipeDirection;
            if (swipeDirection2 == null || (swipeSingleCallback2 = this.swipeSingleCallback) == null) {
                if (swipeDirection == SwipeDirection.RIGHT) {
                    this.swipeCallback.onSwipeRight();
                }
                if (swipeDirection == SwipeDirection.LEFT) {
                    this.swipeCallback.onSwipeLeft();
                }
                if (swipeDirection == SwipeDirection.TOP) {
                    this.swipeCallback.onSwipeTop();
                }
                if (swipeDirection != SwipeDirection.BOTTOM) {
                    return false;
                }
                this.swipeCallback.onSwipeBottom();
                return false;
            } else if (swipeDirection != swipeDirection2) {
                return false;
            } else {
                swipeSingleCallback2.onSwipe();
                return false;
            }
        }
    }
}
