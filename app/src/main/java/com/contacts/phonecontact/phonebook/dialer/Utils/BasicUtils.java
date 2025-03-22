package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.text.TextUtils;

import java.text.Normalizer;
import java.util.ArrayList;

import kotlin.text.Regex;
import kotlin.text.StringsKt;

public class BasicUtils {
    private static final Regex normalizeRegex = new Regex("\\p{InCombiningDiacriticalMarks}+");

    public static String getSourcesSelection$default(boolean z, boolean z2, boolean z3, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = false;
        }
        if ((i2 & 2) != 0) {
            z2 = false;
        }
        if ((i2 & 4) != 0) {
            z3 = true;
        }
        if ((i2 & 8) != 0) {
            i = 0;
        }
        return getSourcesSelection(z, z2, z3, i);
    }

    public static String getSourcesSelection(boolean z, boolean z2, boolean z3, int i) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.add("mimetype = ?");
        }
        if (z2) {
            StringBuilder sb = new StringBuilder();
            sb.append(z3 ? "raw_contact_id" : "contact_id");
            sb.append(" = ?");
            arrayList.add(sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("account_name IN (" + getQuestionMarks(i) + ')');
            arrayList.add(sb2.toString());
        }
        String join = TextUtils.join(" AND ", arrayList);
        return join;
    }

    public static String[] getSourcesSelectionArgs1$default(String str, Integer num, ArrayList arrayList, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            num = null;
        }
        return getSourcesSelectionArgs1(str, num, arrayList);
    }

    public static String[] getSourcesSelectionArgs1(String str, Integer num, ArrayList<String> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        if (str != null) {
            arrayList2.add(str);
        }
        if (num != null) {
            arrayList2.add(num.toString());
        } else {
            arrayList2.addAll(arrayList);
        }
        return (String[]) arrayList2.toArray(new String[0]);
    }

    public static String times(String str, int i) {
        StringBuilder sb = new StringBuilder();
        int i2 = 1;
        if (1 <= i) {
            while (true) {
                sb.append(str);
                if (i2 == i) {
                    break;
                }
                i2++;
            }
        }
        String sb2 = sb.toString();
        return sb2;
    }

    private static String getQuestionMarks(int i) {
        return StringsKt.trimEnd(times("?,", i), ',');
    }

    public static String normalizeString(String str) {
        String normalize = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalizeRegex.replace(normalize, "");
    }

}
