package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import android.view.View;

public class ViewExtensionUtils {
    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void gone(View view) {
        view.setVisibility(View.GONE);
    }

    public static void invisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }
}
