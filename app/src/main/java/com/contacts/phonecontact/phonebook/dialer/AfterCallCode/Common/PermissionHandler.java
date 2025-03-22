package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionHandler {
    private static PermissionHandler sInstance;


    public static PermissionHandler getInstance() {
        if (sInstance == null) {
            synchronized (PermissionHandler.class) {
                if (sInstance == null) {
                    sInstance = new PermissionHandler();
                }
            }
        }
        return sInstance;
    }


    public boolean hasPermission(Context context) {
        for (String str : getPermissionArray()) {
            if (ContextCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPermission(Context context, ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (ContextCompat.checkSelfPermission(context, it.next()) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPermission(Context context, String[] strArr) {
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPermission(Context context, String str) {
        return ContextCompat.checkSelfPermission(context, str) == 0;
    }


    public String[] getPermissionArray() {
        ArrayList arrayList = new ArrayList();
        int i = Build.VERSION.SDK_INT;
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        return strArr;
    }

    public String[] getCallPermissionArray() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("android.permission.READ_PHONE_STATE");
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        return strArr;
    }


    public boolean checkNeverAskAgain(Activity activity) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : getPermissionArray()) {
            if (activity.shouldShowRequestPermissionRationale(str)) {
                return false;
            }
        }
        return true;
    }


}
