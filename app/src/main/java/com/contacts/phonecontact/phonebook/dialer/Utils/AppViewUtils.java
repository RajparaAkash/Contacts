package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.view.View;

public class AppViewUtils {

    public static void beVisibleIf(View view, boolean z) {
        if (z) {
            beVisible(view);
        } else {
            beGone(view);
        }
    }

    public static void beVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void beGone(View view) {
        view.setVisibility(View.GONE);
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == 0;
    }


}
