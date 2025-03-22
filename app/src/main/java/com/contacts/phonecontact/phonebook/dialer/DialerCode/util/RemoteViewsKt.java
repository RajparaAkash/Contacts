package com.contacts.phonecontact.phonebook.dialer.DialerCode.util;

import android.view.View;
import android.widget.RemoteViews;

public class RemoteViewsKt {

    public static void setText(RemoteViews remoteViews, int i, String str) {
        remoteViews.setTextViewText(i, str);
    }

    public static void setVisibleIf(RemoteViews remoteViews, int i, boolean z) {
        remoteViews.setViewVisibility(i, z ? View.VISIBLE : View.GONE);
    }

}
