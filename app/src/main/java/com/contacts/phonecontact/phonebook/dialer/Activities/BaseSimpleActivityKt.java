package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;

import kotlin.jvm.internal.Intrinsics;

public class BaseSimpleActivityKt {
    public static void hideKeyboard(Activity activity) {
        if (ConstantsKt.isOnMainThread()) {
            hideKeyboardSync(activity);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    hideKeyboardSync(activity);
                }
            });
        }
    }

    public static void hideKeyboardSync(Activity activity) {
        Object systemService = activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodManager inputMethodManager = (InputMethodManager) systemService;
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        Window window = activity.getWindow();
        Intrinsics.checkNotNull(window);
        window.setSoftInputMode(3);
        View currentFocus2 = activity.getCurrentFocus();
        if (currentFocus2 != null) {
            currentFocus2.clearFocus();
        }
    }

}
