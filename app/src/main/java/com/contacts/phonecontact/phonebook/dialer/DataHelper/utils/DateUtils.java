package com.contacts.phonecontact.phonebook.dialer.DataHelper.utils;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.ConstantsKt;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static String formatDate(long j) {
        String format = new SimpleDateFormat(ConstantsKt.DATE_FORMAT_FOUR).format(new Date(j));
        return format;
    }

    public static String convertHistoryTime(long j) {
        return new SimpleDateFormat("dd MMM, yyyy  hh:mm a").format((Object) new Date(j));
    }

    public static String formatToDigitalClock(long j) {
        long j2 = j * ((long) 1000);
        int hours = ((int) TimeUnit.MILLISECONDS.toHours(j2)) % 24;
        int minutes = ((int) TimeUnit.MILLISECONDS.toMinutes(j2)) % 60;
        int seconds = ((int) TimeUnit.MILLISECONDS.toSeconds(j2)) % 60;
        if (hours > 0) {
            String format = String.format("%dh %2dm %2ds", Arrays.copyOf(new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}, 3));
            return format;
        } else if (minutes > 0) {
            String format2 = String.format("%2dm %2ds", Arrays.copyOf(new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}, 2));
            return format2;
        } else if (seconds <= 0) {
            return "00:00";
        } else {
            String format3 = String.format("%2ds", Arrays.copyOf(new Object[]{Integer.valueOf(seconds)}, 1));
            return format3;
        }
    }

    public static String convertTimeInAmPm(long j) {
        return new SimpleDateFormat("KK:mm a").format((Object) new Date(j));
    }

    public static String convertTimeInDayAndAmPm(long j) {
        return new SimpleDateFormat("dd MMM hh:mm a").format((Object) new Date(j));
    }

    public static String convertTimeInDayAndAmPmCallLog(long j) {
        return new SimpleDateFormat("hh:mm a").format((Object) new Date(j));
    }

}
