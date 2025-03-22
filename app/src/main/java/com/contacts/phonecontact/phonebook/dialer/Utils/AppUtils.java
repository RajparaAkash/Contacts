package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;

import java.util.Arrays;
import java.util.Locale;

public class AppUtils {

    public static boolean isNetworkAvailable(Context context2) {
        if (context2 != null) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context2.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void shareApp(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public static void ratingApp(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void feedBackApp(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{"videomakerapps304@gmail.com"});
            intent.putExtra("android.intent.extra.SUBJECT", "Feedback");
            intent.putExtra("android.intent.extra.CC", "videomakerapps304@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            context.startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (Exception exception) {
            Toast.makeText(context, context.getString(R.string.gmail_not_install), Toast.LENGTH_SHORT).show();
        }
    }


    public static String getFormattedDuration$default(int i, boolean z, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = false;
        }
        return getFormattedDuration(i, z);
    }

    public static String getFormattedDuration(int i, boolean z) {
        StringBuilder sb = new StringBuilder(8);
        int i2 = i / ConstantsKt.HOUR_SECONDS;
        int i3 = (i % ConstantsKt.HOUR_SECONDS) / 60;
        int i4 = i % 60;
        if (i >= 3600) {
            String format = String.format(Locale.getDefault(), "%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i2)}, 1));
            sb.append(format);
            sb.append(":");
        } else if (z) {
            sb.append("0:");
        }
        String format2 = String.format(Locale.getDefault(), "%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i3)}, 1));
        sb.append(format2);
        sb.append(":");
        String format3 = String.format(Locale.getDefault(), "%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i4)}, 1));
        sb.append(format3);
        String sb2 = sb.toString();
        return sb2;
    }


}
