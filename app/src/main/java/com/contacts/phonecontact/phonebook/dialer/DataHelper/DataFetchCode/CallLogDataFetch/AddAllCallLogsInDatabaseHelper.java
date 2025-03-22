package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.loader.content.CursorLoader;

import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogDataModels.SIMAccount;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetAllContactNumberHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class AddAllCallLogsInDatabaseHelper {
    private final int COMPARABLE_PHONE_NUMBER_LENGTH = 9;
    private final ContactDatabase mContactDAO;
    private final Context mContext;
    private final String[] projection;
    private final Uri uri = CallLog.Calls.CONTENT_URI;
    private int colorPosition;

    public AddAllCallLogsInDatabaseHelper(Context context, ContactDatabase contactDatabase) {
        this.mContext = context;
        this.mContactDAO = contactDatabase;
        projection = new String[]{"_id", "number", "name", "date", "duration", "type", "subscription_id", "photo_uri"};
    }

    public Uri getUri() {
        return this.uri;
    }


    public void invoke1() {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList();
        final HashMap hashMap = new HashMap();
        for (final SIMAccount simAccount : AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext)) {
            if (Build.VERSION.SDK_INT >= 23) {
                final Map map = hashMap;
                final String id = simAccount.getHandle().getId();
                map.put(id, simAccount.getId());
            }
        }
        final HashMap hashMap2 = new HashMap();
        final HashMap<String, String> hashMap3 = new HashMap<String, String>();
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                if (Build.VERSION.SDK_INT >= 24) {
                    cursor = mContext.getContentResolver().query(this.uri.buildUpon().build(), projection, (String) null, (String[]) null, "_id DESC");
                } else {
                    cursor = mContext.getContentResolver().query(this.uri, projection, (String) null, (String[]) null, "_id DESC");
                }
                if (cursor != null && cursor.moveToFirst()) {
                    while (true) {

                        final int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                        final String stringValue = CursorExtenUtils.getStringValue(cursor, "number");
                        List list3;
                        if (stringValue == null) {
                            list3 = list2;
                        } else {
                            String stringValue2;
                            if ((stringValue2 = CursorExtenUtils.getStringValue(cursor, "name")) == null) {
                                stringValue2 = "";
                            }
                            final long longValue = CursorExtenUtils.getLongValue(cursor, "date");
                            final String s = null;
                            final String s2 = null;
                            String stringValue3;
                            if (Build.VERSION.SDK_INT < 23 || (stringValue3 = CursorExtenUtils.getStringValue(cursor, "photo_uri")) == null) {
                                stringValue3 = "";
                            }
                            if (++this.colorPosition >= intArray.length) {
                                this.colorPosition = 0;
                            }
                            String s4 = null;
                            Object o2 = null;
                            String s6 = null;
                            Label_1065:
                            {
                                Object o;
                                String s5;
                                String obj;
                                List list5;
                                if (hashMap2.containsKey(stringValue)) {
                                    final Object value = hashMap2.get(stringValue);
                                    final String s3 = hashMap3.get(stringValue);
                                    final List list4 = list2;
                                    s4 = stringValue3;
                                    o = value;
                                    s5 = s4;
                                    obj = s2;
                                    list5 = list4;
                                    if (s3 != null) {
                                        o2 = value;
                                        s6 = s3;
                                        list3 = list4;
                                        break Label_1065;
                                    }
                                } else {
                                    final Iterable<Contact> iterable = list2;
                                    final Collection<Contact> collection = new ArrayList();
                                    for (Contact next : iterable) {
                                        if (((Contact) next).getContactNumber().isEmpty() ^ true) {
                                            collection.add(next);
                                        }
                                    }
                                    list5 = list2;
                                    final List<Contact> list6 = (List) collection;
                                    final String string = StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
                                    final Iterator<Contact> iterator3 = list6.iterator();
                                    obj = s;
                                    String s7 = stringValue3;
                                    while (iterator3.hasNext()) {
                                        final Contact contact = (Contact) iterator3.next();
                                        final Iterator<PhoneNumber> iterator5 = contact.getContactNumber().iterator();
                                        String firstNameOriginal = stringValue2;
                                        String s8 = s7;
                                        while (iterator5.hasNext()) {
                                            final PhoneNumber phoneNumber = (PhoneNumber) iterator5.next();
                                            String s9 = null;
                                            String value2 = null;
                                            Label_0999:
                                            {
                                                if (!Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString(), (Object) string)) {
                                                    s9 = s8;
                                                    value2 = obj;
                                                    if (!Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString(), (Object) string)) {
                                                        break Label_0999;
                                                    }
                                                }
                                                firstNameOriginal = contact.getFirstNameOriginal();
                                                final int contactIdSimple = contact.getContactIdSimple();
                                                String s10;
                                                if (contact.getContactPhotoUri() != null) {
                                                    final String contactPhotoUri = contact.getContactPhotoUri();
                                                    final boolean b = contactPhotoUri.length() > 0;
                                                    s10 = s8;
                                                    if (b) {
                                                        s10 = contact.getContactPhotoUri();
                                                    }
                                                } else {
                                                    s10 = s8;
                                                    if (contact.getContactPhotoThumbUri() != null) {
                                                        final String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
                                                        final boolean b2 = contactPhotoThumbUri.length() > 0;
                                                        s10 = s8;
                                                        if (b2) {
                                                            s10 = contact.getContactPhotoThumbUri();
                                                        }
                                                    }
                                                }
                                                value2 = String.valueOf(contactIdSimple);
                                                s9 = s10;
                                            }
                                            s8 = s9;
                                            obj = value2;
                                        }
                                        hashMap3.put(stringValue, String.valueOf(obj));
                                        hashMap2.put(stringValue, firstNameOriginal);
                                        s7 = s8;
                                        stringValue2 = firstNameOriginal;
                                    }
                                    s5 = s7;
                                    o = stringValue2;
                                }
                                final String s11 = obj;
                                o2 = o;
                                s6 = s11;
                                s4 = s5;
                                list3 = list5;
                            }
                            final CharSequence charSequence = (CharSequence) o2;
                            String contactRowIDLookupList;
                            if (charSequence != null && charSequence.length() != 0 && (s6 == null || Intrinsics.areEqual((Object) s6, (Object) "0"))) {
                                contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext);
                            } else {
                                contactRowIDLookupList = s6;
                            }
                            if (o2 == null || ((CharSequence) o2).length() == 0 || contactRowIDLookupList == null || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "0") || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "") || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "null")) {
                                o2 = stringValue;
                            }
                            final boolean b3 = ((CharSequence) o2).length() == 0;
                            CharSequence string2 = (CharSequence) o2;
                            if (b3) {
                                string2 = mContext.getString(R.string.unknown);
                            }
                            final int n = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                            final int intValue2 = CursorExtenUtils.getIntValue(cursor, "duration");
                            final int intValue3 = CursorExtenUtils.getIntValue(cursor, "type");
                            Integer value3;


                            int sim = 0;
                            value3 = Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("subscription_id")));
                            if (value3 == null) {
                                value3 = -1;
                                sim = -1;
                            } else {
                                int i2 = -1;

                                String simId = String.valueOf(value3);
                                if (arrSim.size() > 1 && simId != null && !simId.isEmpty()) {
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= arrSim.size()) {
                                            break;
                                        } else if (simId.equals(arrSim.get(i3).getHandle().getId())) {
                                            i2 = i3;
                                            break;
                                        } else {
                                            i3++;
                                        }
                                    }
                                }

                                sim = i2 + 1;

                            }

//                            if ((value3 = (Integer) hashMap.get(CusrorExtenKt.getStringValue(cursor, "subscription_id"))) == null) {
//                                value3 = -1;
//                            }
                            final int intValue4 = sim;
                            String s12 = null;
                            if (intValue3 == 0) {
                                s12 = mContext.getString(R.string.block_call);
                            } else if (intValue3 == CallLog.Calls.INCOMING_TYPE) {
                                s12 = mContext.getString(R.string.incoming);
                            } else if (intValue3 == CallLog.Calls.OUTGOING_TYPE) {
                                s12 = mContext.getString(R.string.outgoing);
                            } else if (intValue3 == CallLog.Calls.MISSED_TYPE) {
                                s12 = mContext.getString(R.string.missed_Call);
                            } else if (intValue3 == CallLog.Calls.VOICEMAIL_TYPE) {
                                s12 = mContext.getString(R.string.block_call);
                            } else if (intValue3 == CallLog.Calls.REJECTED_TYPE) {
                                s12 = mContext.getString(R.string.outgoing);
                            } else if (intValue3 == CallLog.Calls.BLOCKED_TYPE) {
                                s12 = mContext.getString(R.string.block_call);
                            }
                            list.add(new CallLogModel((Integer) null, intValue, contactRowIDLookupList, stringValue, (String) string2, s4, n, intValue2, intValue3, intValue4, stringValue, "", s12, longValue, Integer.valueOf(intArray[this.colorPosition]), 1, (DefaultConstructorMarker) null));
                        }
                        if (!cursor.moveToNext()) {
                            break;
                        }
                        list2 = list3;
                    }
                }
                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);

            }
        } catch (final Exception ex) {
            ex.printStackTrace();
//            Log.e("fatal4", "invoke: " + ex.getMessage() );
        }
    }



    public void invoke2() {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList();
        final HashMap hashMap2 = new HashMap();
        final HashMap<String, String> hashMap3 = new HashMap<String, String>();
//        Log.e("fatal4", "start: " );
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
//        Log.e("fatal4", "list2 size: " + list2.size() );
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
//        Log.e("fatal4", "getSimIndex: " + arrSim.size() );
//        Log.e("fatal4", "list size: " + list.size() );
        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                cursor = mContext.getContentResolver().query(this.uri.buildUpon().build(), projection, (String) null, (String[]) null, "_id DESC");
                if (cursor != null && cursor.moveToFirst()) {
                    do {
//                        Log.e("fatal4", "invoke calllog: " + "start1" );
                        final int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                        final String stringValue = CursorExtenUtils.getStringValue(cursor, "number");
                        if (stringValue == null) {
                        } else {
                            String stringValue2;
                            if ((stringValue2 = CursorExtenUtils.getStringValue(cursor, "name")) == null) {
                                stringValue2 = "";
                            }
                            final long longValue = CursorExtenUtils.getLongValue(cursor, "date");
                            final String s = null;
                            final String s2 = null;
                            String photoUri;
                            if ((photoUri = CursorExtenUtils.getStringValue(cursor, "photo_uri")) == null) {
                                photoUri = "";
                            }
                            if (++colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }
                            String s4 = null;
                            Object o2 = null;
                            String s6 = null;
//                            Log.e("fatal4", "invoke calllog: " + "start2" );
                            Label_1065:
                            {
                                Object o;
                                String s5;
                                String obj;
                                if (hashMap2.containsKey(stringValue)) {
                                    final Object value = hashMap2.get(stringValue);
                                    final String s3 = hashMap3.get(stringValue);
                                    s4 = photoUri;
                                    o = value;
                                    s5 = s4;
                                    obj = s2;
                                    if (s3 != null) {
                                        o2 = value;
                                        s6 = s3;
                                        break Label_1065;
                                    }
                                } else {
                                    final Iterable<Contact> iterable = list2;
                                    final Collection<Contact> collection = new ArrayList();
                                    for (Contact next : iterable) {
                                        if (((Contact) next).getContactNumber().isEmpty() ^ true) {
                                            collection.add(next);
                                        }
                                    }
                                    final List<Contact> list6 = (List) collection;
                                    final String string = StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
                                    final Iterator<Contact> iterator3 = list6.iterator();
                                    obj = s;
                                    String s7 = photoUri;
                                    while (iterator3.hasNext()) {
                                        final Contact contact = (Contact) iterator3.next();
                                        final Iterator<PhoneNumber> iterator5 = contact.getContactNumber().iterator();
                                        String firstNameOriginal = stringValue2;
                                        String s8 = s7;
                                        while (iterator5.hasNext()) {
                                            final PhoneNumber phoneNumber = (PhoneNumber) iterator5.next();
                                            String s9 = null;
                                            String value2 = null;
                                            Label_0999:
                                            {
                                                if (!Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString(), (Object) string)) {
                                                    s9 = s8;
                                                    value2 = obj;
                                                    if (!Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString(), (Object) string)) {
                                                        break Label_0999;
                                                    }
                                                }
                                                firstNameOriginal = contact.getFirstNameOriginal();
                                                final int contactIdSimple = contact.getContactIdSimple();
                                                String s10;
                                                if (contact.getContactPhotoUri() != null) {
                                                    final String contactPhotoUri = contact.getContactPhotoUri();
                                                    final boolean b = contactPhotoUri.length() > 0;
                                                    s10 = s8;
                                                    if (b) {
                                                        s10 = contact.getContactPhotoUri();
                                                    }
                                                } else {
                                                    s10 = s8;
                                                    if (contact.getContactPhotoThumbUri() != null) {
                                                        final String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
                                                        final boolean b2 = contactPhotoThumbUri.length() > 0;
                                                        s10 = s8;
                                                        if (b2) {
                                                            s10 = contact.getContactPhotoThumbUri();
                                                        }
                                                    }
                                                }
                                                value2 = String.valueOf(contactIdSimple);
                                                s9 = s10;
                                            }
                                            s8 = s9;
                                            obj = value2;
                                        }
                                        hashMap3.put(stringValue, String.valueOf(obj));
                                        hashMap2.put(stringValue, firstNameOriginal);
                                        s7 = s8;
                                        stringValue2 = firstNameOriginal;
                                    }
                                    s5 = s7;
                                    o = stringValue2;
                                }
                                final String s11 = obj;
                                o2 = o;
                                s6 = s11;
                                s4 = s5;
                            }
                            final CharSequence charSequence = (CharSequence) o2;
                            String contactRowIDLookupList;
                            if (charSequence != null && charSequence.length() != 0 && (s6 == null || Intrinsics.areEqual((Object) s6, (Object) "0"))) {
                                contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext);
                            } else {
                                contactRowIDLookupList = s6;
                            }
                            if (o2 == null || ((CharSequence) o2).length() == 0 || contactRowIDLookupList == null || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "0") || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "") || Intrinsics.areEqual((Object) contactRowIDLookupList, (Object) "null")) {
                                o2 = stringValue;
                            }
                            final boolean b3 = ((CharSequence) o2).length() == 0;
                            CharSequence string2 = (CharSequence) o2;
                            if (b3) {
                                string2 = mContext.getString(R.string.unknown);
                            }
                            final int date = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                            final int duration = CursorExtenUtils.getIntValue(cursor, "duration");
                            final int type = CursorExtenUtils.getIntValue(cursor, "type");

                            String value3 = null;
                            int simIndex = -1;
                            try {
                                value3 = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("subscription_id")));
//                                Log.e("fatal4", "invoke: " + value3 );
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (value3 != null) {
                                int i2 = 0;

                                String simId = String.valueOf(value3);
                                if (arrSim.size() > 1 && simId != null && !simId.isEmpty()) {
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= arrSim.size()) {
                                            break;
                                        } else if (simId.equals(arrSim.get(i3).getHandle().getId())) {
                                            i2 = i3;
                                            break;
                                        } else {
                                            i3++;
                                        }
                                    }
                                }

                                simIndex = i2 + 1;

                            }

//                            if ((value3 = (Integer) hashMap.get(CusrorExtenKt.getStringValue(cursor, "subscription_id"))) == null) {
//                                value3 = -1;
//                            }
                            final int intValue4 = simIndex;

                            String s12 = "";
                            if (type == 0) {
                                s12 = mContext.getString(R.string.block_call);
                            } else if (type == CallLog.Calls.INCOMING_TYPE) {
                                s12 = mContext.getString(R.string.incoming);
                            } else if (type == CallLog.Calls.OUTGOING_TYPE) {
                                s12 = mContext.getString(R.string.outgoing);
                            } else if (type == CallLog.Calls.MISSED_TYPE) {
                                s12 = mContext.getString(R.string.missed_Call);
                            } else if (type == CallLog.Calls.VOICEMAIL_TYPE) {
                                s12 = mContext.getString(R.string.block_call);
                            } else if (type == CallLog.Calls.REJECTED_TYPE) {
                                s12 = mContext.getString(R.string.declined_call);
                            } else if (type == CallLog.Calls.BLOCKED_TYPE) {
                                s12 = mContext.getString(R.string.block_call);
                            } else {
                                s12 = mContext.getString(R.string.outgoing);
                            }

                            list.add(new CallLogModel((Integer) null, intValue, contactRowIDLookupList, stringValue, (String) string2, s4, date, duration, type, intValue4, stringValue, "", s12, longValue, intArray[colorPosition], 1, (DefaultConstructorMarker) null));
//                            Log.e("fatal4", "invoke calllog: " + "start3" );
                        }
//                        if (!cursor.moveToNext()) {
//                            break;
//                        }

                    } while(cursor.moveToNext());
                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
//                Log.e("fatal4", "list size: " + list.size() );
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
//            Log.e("fatal4", "invoke: " + ex.getMessage() );
        }
    }




    public void invoke() {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList();
        final HashMap hashMap2 = new HashMap();
        final HashMap<String, String> hashMap3 = new HashMap<String, String>();
//        Log.e("fatal4", "start: " );
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
//        Log.e("fatal4", "list2 size: " + list2.size() );
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
//        Log.e("fatal4", "getSimIndex: " + arrSim.size() );
//        Log.e("fatal4", "list size: " + list.size() );
        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                cursor = mContext.getContentResolver().query(this.uri.buildUpon().build(), projection, (String) null, (String[]) null, "_id DESC");
                if (cursor != null && cursor.moveToFirst()) {


                    do {
                        // Extract values from the cursor
                        final int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                        final String stringValue = CursorExtenUtils.getStringValue(cursor, "number");

                        if (stringValue != null) {
                            // Handle the "name" field
                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, "name");
                            if (stringValue2 == null) {
                                stringValue2 = "";
                            }

                            final long longValue = CursorExtenUtils.getLongValue(cursor, "date");

                            // Handle the "photo_uri" field
                            String photoUri = CursorExtenUtils.getStringValue(cursor, "photo_uri");
                            if (photoUri == null) {
                                photoUri = "";
                            }

                            // Reset color position if it exceeds the array length
                            if (++colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }

                            String s4 = null;
                            Object o2 = null;
                            String s6 = null;

                            if (hashMap2.containsKey(stringValue)) {
                                // Retrieve from cache
                                final Object value = hashMap2.get(stringValue);
                                final String s3 = hashMap3.get(stringValue);
                                s4 = photoUri;
                                o2 = value;
                                s6 = s3;
                            } else {
                                // Process contacts list
                                final Collection<Contact> collection = new ArrayList<>();
                                for (Contact contact : list2) {
                                    if (!contact.getContactNumber().isEmpty()) {
                                        collection.add(contact);
                                    }
                                }
                                final List<Contact> list6 = new ArrayList<>(collection);
                                final String string = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
                                String s7 = photoUri;

                                for (Contact contact : list6) {
                                    for (PhoneNumber phoneNumber : contact.getContactNumber()) {
                                        String trimmedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString();
                                        String trimmedNormalizedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString();

                                        if (Intrinsics.areEqual(trimmedPhone, string) || Intrinsics.areEqual(trimmedNormalizedPhone, string)) {
                                            stringValue2 = contact.getFirstNameOriginal();
                                            String contactPhotoUri = contact.getContactPhotoUri();
                                            String contactPhotoThumbUri = contact.getContactPhotoThumbUri();

                                            if (contactPhotoUri != null && !contactPhotoUri.isEmpty()) {
                                                s7 = contactPhotoUri;
                                            } else if (contactPhotoThumbUri != null && !contactPhotoThumbUri.isEmpty()) {
                                                s7 = contactPhotoThumbUri;
                                            }
                                            hashMap3.put(stringValue, String.valueOf(contact.getContactIdSimple()));
                                            hashMap2.put(stringValue, stringValue2);
                                            break;
                                        }
                                    }
                                }
                                s4 = s7;
                                o2 = stringValue2;
                            }

                            // Get contactRowIDLookupList
                            final String contactRowIDLookupList = (o2 != null && !o2.toString().isEmpty() && (s6 == null || Intrinsics.areEqual(s6, "0")))
                                    ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
                                    : s6;

                            if (o2 == null || o2.toString().isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
                                o2 = stringValue;
                            }

                            CharSequence displayName = (o2.toString().isEmpty()) ? mContext.getString(R.string.unknown) : (CharSequence) o2;

                            // Extract date, duration, type and simIndex
                            final int date = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                            final int duration = CursorExtenUtils.getIntValue(cursor, "duration");
                            final int type = CursorExtenUtils.getIntValue(cursor, "type");

                            int simIndex = -1;
                            try {
                                String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow("subscription_id"));
                                if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
                                    for (int i = 0; i < arrSim.size(); i++) {
                                        if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
                                            simIndex = i + 1;
                                            break;
                                        }
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            // Determine call type string
                            String callType;
                            switch (type) {
                                case CallLog.Calls.INCOMING_TYPE:
                                    callType = mContext.getString(R.string.incoming);
                                    break;
                                case CallLog.Calls.OUTGOING_TYPE:
                                    callType = mContext.getString(R.string.outgoing);
                                    break;
                                case CallLog.Calls.MISSED_TYPE:
                                    callType = mContext.getString(R.string.missed_Call);
                                    break;
                                case CallLog.Calls.VOICEMAIL_TYPE:
                                case CallLog.Calls.BLOCKED_TYPE:
                                    callType = mContext.getString(R.string.block_call);
                                    break;
                                case CallLog.Calls.REJECTED_TYPE:
                                    callType = mContext.getString(R.string.declined_call);
                                    break;
                                default:
                                    callType = mContext.getString(R.string.outgoing);
                            }

                            // Add the new call log entry
                            list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, displayName.toString(), s4, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition]));
                        }
                    } while (cursor.moveToNext());


                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
                Log.e("fatal4", "list size: " + list.size() );
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            Log.e("fatal4", "invoke: " + ex.getMessage() );
        }
    }




    public void invoke6() {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList();
        final HashMap hashMap2 = new HashMap();
        final HashMap<String, String> hashMap3 = new HashMap<String, String>();
        Log.e("fatal4", "start: " );
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        Log.e("fatal4", "list2 size: " + list2.size() );
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
//        Log.e("fatal4", "getSimIndex: " + arrSim.size() );
        Log.e("fatal4", "list size: " + list.size() );
        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                cursor = mContext.getContentResolver().query(this.uri.buildUpon().build(), projection, (String) null, (String[]) null, "_id DESC");
                if (cursor != null && cursor.moveToFirst()) {



                    do {
                        // Extract values from the cursor
                        final int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                        final String stringValue = CursorExtenUtils.getStringValue(cursor, "number");

                        if (stringValue != null) {
                            // Handle the "name" field
                            String contactName = CursorExtenUtils.getStringValue(cursor, "name");
                            if (contactName == null) {
                                contactName = "";
                            }

                            final long longValue = CursorExtenUtils.getLongValue(cursor, "date");

                            // Handle the "photo_uri" field
                            String photoUri = CursorExtenUtils.getStringValue(cursor, "photo_uri");
                            if (photoUri == null) {
                                photoUri = "";
                            }

                            // Reset color position if it exceeds the array length
                            if (++colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }

//                            String contactName = null;
                            String contactId = null;

                            // Check cache first
                            if (hashMap2.containsKey(stringValue)) {
                                contactName = (String) hashMap2.get(stringValue);
                                contactId = hashMap3.get(stringValue);
                            } else {
                                // Normalize number once for comparison
                                final String normalizedNumber = normalizePhoneNumber(stringValue);

                                // Preprocess the list of contacts once
                                for (Contact contact : list2) {
                                    boolean found = false;
                                    for (PhoneNumber phoneNumber : contact.getContactNumber()) {
                                        if (isNumberMatching(phoneNumber, normalizedNumber)) {
                                            contactName = contact.getFirstNameOriginal();
                                            contactId = String.valueOf(contact.getContactIdSimple());
                                            photoUri = getPhotoUri(contact, photoUri);
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        break;
                                    }
                                }

                                // Cache the results
                                hashMap2.put(stringValue, contactName);
                                hashMap3.put(stringValue, contactId);
                            }

                            // Get contactRowIDLookupList
                            final String contactRowIDLookupList = (contactName != null && !contactName.isEmpty() && (contactId == null || Intrinsics.areEqual(contactId, "0")))
                                    ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
                                    : contactId;

                            if (contactName == null || contactName.isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
                                contactName = stringValue;
                            }

                            CharSequence displayName = (contactName.isEmpty()) ? mContext.getString(R.string.unknown) : contactName;

                            // Extract date, duration, type and simIndex
                            final int date = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                            final int duration = CursorExtenUtils.getIntValue(cursor, "duration");
                            final int type = CursorExtenUtils.getIntValue(cursor, "type");

                            int simIndex = -1;
                            try {
                                String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow("subscription_id"));
                                simIndex = getSimIndex1(subscriptionId, arrSim);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            // Determine call type string
                            String callType = getCallTypeString(type);

                            // Add the new call log entry
                            list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, displayName.toString(), photoUri, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition], 1, null));
                        }
                    } while (cursor.moveToNext());


                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
                Log.e("fatal4", "list size: " + list.size() );
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            Log.e("fatal4", "invoke: " + ex.getMessage() );
        }
    }
    private String normalizePhoneNumber(String number) {
        return StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(number, "-", "", false), " ", "", false), "+91", "", false)).toString();
    }
    private boolean isNumberMatching(PhoneNumber phoneNumber, String normalizedNumber) {
        String contactNumber = normalizePhoneNumber(phoneNumber.getValue());
        String normalizedPhone = normalizePhoneNumber(phoneNumber.getNormalizedNumber());
        return Intrinsics.areEqual(contactNumber, normalizedNumber) || Intrinsics.areEqual(normalizedPhone, normalizedNumber);
    }
    private String getPhotoUri(Contact contact, String defaultUri) {
        String contactPhotoUri = contact.getContactPhotoUri();
        if (contactPhotoUri != null && !contactPhotoUri.isEmpty()) {
            return contactPhotoUri;
        }
        String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
        if (contactPhotoThumbUri != null && !contactPhotoThumbUri.isEmpty()) {
            return contactPhotoThumbUri;
        }
        return defaultUri;
    }
    private int getSimIndex1(String subscriptionId, ArrayList<SIMAccount> arrSim) {
        if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
            for (int i = 0; i < arrSim.size(); i++) {
                if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
                    return i + 1;
                }
            }
        }
        return -1;
    }
    private String getCallTypeString(int type) {
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                return mContext.getString(R.string.incoming);
            case CallLog.Calls.OUTGOING_TYPE:
                return mContext.getString(R.string.outgoing);
            case CallLog.Calls.MISSED_TYPE:
                return mContext.getString(R.string.missed_Call);
            case CallLog.Calls.VOICEMAIL_TYPE:
            case CallLog.Calls.BLOCKED_TYPE:
                return mContext.getString(R.string.block_call);
            case CallLog.Calls.REJECTED_TYPE:
                return mContext.getString(R.string.declined_call);
            default:
                return mContext.getString(R.string.outgoing);
        }
    }




    public void invoke7() {
        final int BATCH_SIZE = 25;
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList<>();
        final HashMap<String, String> hashMap2 = new HashMap<>();
        final HashMap<String, String> hashMap3 = new HashMap<>();
        final List<Contact> contacts = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        final ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
//        mContactDAO.callLogDAO().deleteAllCallLog();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                    Cursor cursor = mContext.getContentResolver().query(this.uri.buildUpon().build(), projection, null, null, "_id DESC");
                    Log.e("fatal4", "invoke: " + cursor.getCount() );
                    if (cursor != null) {
                        try {
                            while (cursor.moveToNext()) {
                                // Extract values from the cursor
                                int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                                String stringValue = CursorExtenUtils.getStringValue(cursor, "number");
                                if (stringValue != null) {
                                    String stringValue2 = CursorExtenUtils.getStringValue(cursor, "name");
                                    if (stringValue2 == null) {
                                        stringValue2 = "";
                                    }
                                    long longValue = CursorExtenUtils.getLongValue(cursor, "date");
                                    String photoUri = CursorExtenUtils.getStringValue(cursor, "photo_uri");
                                    if (photoUri == null) {
                                        photoUri = "";
                                    }
                                    if (++colorPosition >= intArray.length) {
                                        colorPosition = 0;
                                    }
                                    String s4 = null;
                                    Object o2 = null;
                                    String s6 = null;
                                    if (hashMap2.containsKey(stringValue)) {
                                        s4 = photoUri;
                                        o2 = hashMap2.get(stringValue);
                                        s6 = hashMap3.get(stringValue);
                                    } else {
                                        String cleanedNumber = StringsKt.trim(
                                                StringsKt.replace(
                                                        StringsKt.replace(
                                                                StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false
                                                )
                                        ).toString();
                                        for (Contact contact : contacts) {
                                            for (PhoneNumber phoneNumber : contact.getContactNumber()) {
                                                String trimmedPhone = StringsKt.trim(
                                                        StringsKt.replace(
                                                                StringsKt.replace(
                                                                        StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false
                                                        )
                                                ).toString();
                                                String trimmedNormalizedPhone = StringsKt.trim(
                                                        StringsKt.replace(
                                                                StringsKt.replace(
                                                                        StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false
                                                        )
                                                ).toString();
                                                if (Intrinsics.areEqual(trimmedPhone, cleanedNumber) || Intrinsics.areEqual(trimmedNormalizedPhone, cleanedNumber)) {
                                                    stringValue2 = contact.getFirstNameOriginal();
                                                    s4 = contact.getContactPhotoUri();
                                                    if (s4 == null || s4.isEmpty()) {
                                                        s4 = contact.getContactPhotoThumbUri();
                                                    }
                                                    hashMap3.put(stringValue, String.valueOf(contact.getContactIdSimple()));
                                                    hashMap2.put(stringValue, stringValue2);
                                                    break;
                                                }
                                            }
                                        }
                                        s4 = (s4 == null) ? photoUri : s4;
                                        o2 = stringValue2;
                                    }
                                    String contactRowIDLookupList = (o2 != null && !o2.toString().isEmpty() && (s6 == null || Intrinsics.areEqual(s6, "0")))
                                            ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
                                            : s6;
                                    if (o2 == null || o2.toString().isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
                                        o2 = stringValue;
                                    }
                                    CharSequence displayName = (o2.toString().isEmpty()) ? mContext.getString(R.string.unknown) : (CharSequence) o2;
                                    // Extract date, duration, type and simIndex
                                    int date = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                                    int duration = CursorExtenUtils.getIntValue(cursor, "duration");
                                    int type = CursorExtenUtils.getIntValue(cursor, "type");
                                    int simIndex = -1;
                                    try {
                                        String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow("subscription_id"));
                                        if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
                                            for (int i = 0; i < arrSim.size(); i++) {
                                                if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
                                                    simIndex = i + 1;
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    // Determine call type string
                                    String callType;
                                    switch (type) {
                                        case CallLog.Calls.INCOMING_TYPE:
                                            callType = mContext.getString(R.string.incoming);
                                            break;
                                        case CallLog.Calls.OUTGOING_TYPE:
                                            callType = mContext.getString(R.string.outgoing);
                                            break;
                                        case CallLog.Calls.MISSED_TYPE:
                                            callType = mContext.getString(R.string.missed_Call);
                                            break;
                                        case CallLog.Calls.VOICEMAIL_TYPE:
                                        case CallLog.Calls.BLOCKED_TYPE:
                                            callType = mContext.getString(R.string.block_call);
                                            break;
                                        case CallLog.Calls.REJECTED_TYPE:
                                            callType = mContext.getString(R.string.declined_call);
                                            break;
                                        default:
                                            callType = mContext.getString(R.string.outgoing);
                                    }
                                    list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, displayName.toString(), s4, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition], 1, null));
                                    if (list.size() >= BATCH_SIZE) {
                                        Log.e("fatal4", "list.size 111: " + list.size() );
                                        mContactDAO.callLogDAO().addAllHistory(list);
                                        list.clear();
                                    }
                                }
                            }
                            if (!list.isEmpty()) {
                                Log.e("fatal4", "list.size 222: " + list.size() );
                                mContactDAO.callLogDAO().addAllHistory(list);
                            }
                        } finally {
                            cursor.close();
                        }
                    }
//                    mContactDAO.callLogDAO().deleteAllCallLog();
                    Log.e("fatal4", "Processing complete");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("fatal4", "invoke: " + ex.getMessage());
            }
        });
        executor.shutdown();
    }
    public interface OnBatchCompleteListener {
        void onBatchComplete(Context context, ContactDatabase contactDatabase, long[] lastProcessedTimestamp);
    }




    public void invoke8() {
        List<CallLogModel> callLogEntries = new ArrayList<>();
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        String[] projection = new String[]{"_id", "number", "name", "date", "duration", "type", "subscription_id", "photo_uri"};
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
        Cursor callLogCursor = mContext.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

//        CursorLoader loader1 = new CursorLoader(mContext, CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC");
//        Cursor callLogCursor = loader1.loadInBackground();

        if (callLogCursor != null) {
            while (callLogCursor.moveToNext()) {
                String phoneNumber = callLogCursor.getString(callLogCursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String contactName = getContactName(phoneNumber);

                if (++colorPosition >= intArray.length) {
                    colorPosition = 0;
                }
                final int intValue = CursorExtenUtils.getIntValue(callLogCursor, "_id");
//                final String phoneNumber = CursorExtenUtils.getStringValue(callLogCursor, "number");
//                String contactName = CursorExtenUtils.getStringValue(callLogCursor, "name");
//                if (contactName == null) {
//                    contactName = "";
//                }

                final long longValue = CursorExtenUtils.getLongValue(callLogCursor, "date");

                // Handle the "photo_uri" field
                String photoUri = CursorExtenUtils.getStringValue(callLogCursor, "photo_uri");
                if (photoUri == null) {
                    photoUri = "";
                }

                final int date = (int) (CursorExtenUtils.getLongValue(callLogCursor, "date") / 1000L);
                final int duration = CursorExtenUtils.getIntValue(callLogCursor, "duration");
                final int type = CursorExtenUtils.getIntValue(callLogCursor, "type");

                int simIndex = -1;
                try {
                    String subscriptionId = callLogCursor.getString(callLogCursor.getColumnIndexOrThrow("subscription_id"));
                    if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
                        for (int i = 0; i < arrSim.size(); i++) {
                            if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
                                simIndex = i + 1;
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                // Determine call type string
                String callType;
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = mContext.getString(R.string.incoming);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = mContext.getString(R.string.outgoing);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = mContext.getString(R.string.missed_Call);
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = mContext.getString(R.string.block_call);
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        callType = mContext.getString(R.string.declined_call);
                        break;
                    default:
                        callType = mContext.getString(R.string.outgoing);
                }

                String contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(phoneNumber, mContext);
//                String contactRowIDLookupList = null;
                if (contactRowIDLookupList == null) {
                    contactRowIDLookupList = "";
                }

                callLogEntries.add(new CallLogModel(null, intValue, contactRowIDLookupList, phoneNumber, contactName, photoUri, date, duration, type, simIndex, phoneNumber, "", callType, longValue, intArray[colorPosition], 1, null));



            }
            callLogCursor.close();
        }

        mContactDAO.callLogDAO().deleteAllCallLog();
        mContactDAO.callLogDAO().addAllHistory(callLogEntries);
        Log.e("fatal4", "callLogEntries.size: " + callLogEntries.size() );

    }
    public String getContactName(String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = mContext.getContentResolver().query(
                uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                cursor.close();
                return contactName;
            }
            cursor.close();
        }
        return phoneNumber;
    }




    public void invoke9() {
        List<CallLogModel> callLogEntries = new ArrayList<>();
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        String[] projection = new String[]{"_id", "number", "name", "date", "duration", "type", "subscription_id", "photo_uri"};
        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
        Cursor callLogCursor = mContext.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

        if (callLogCursor != null) {
            while (callLogCursor.moveToNext()) {
                String phoneNumber = callLogCursor.getString(callLogCursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String contactName = getContactName(phoneNumber);

//                long date = callLogCursor.getLong(callLogCursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
//                int duration = callLogCursor.getInt(callLogCursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
//                int type = callLogCursor.getInt(callLogCursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));

//                Log.e("fatal4", "getCallLogsWithContactNames: " + "phoneNumber: " + phoneNumber + " contactName: " + contactName + " date: " + date + " duration: " + duration + " type: " + type);
//                Log.e("fatal4", "getCallLogsWithContactNames: " + "==================================================================================");

//                callLogEntries.add(new CallLogEntry(phoneNumber, contactName, date, duration, type));



                if (++colorPosition >= intArray.length) {
                    colorPosition = 0;
                }



                final int intValue = CursorExtenUtils.getIntValue(callLogCursor, "_id");
//                final String phoneNumber = CursorExtenUtils.getStringValue(callLogCursor, "number");
//                String contactName = CursorExtenUtils.getStringValue(callLogCursor, "name");
//                if (contactName == null) {
//                    contactName = "";
//                }

                final long longValue = CursorExtenUtils.getLongValue(callLogCursor, "date");

                // Handle the "photo_uri" field
                String photoUri = CursorExtenUtils.getStringValue(callLogCursor, "photo_uri");
                if (photoUri == null) {
                    photoUri = "";
                }

                final int date = (int) (CursorExtenUtils.getLongValue(callLogCursor, "date") / 1000L);
                final int duration = CursorExtenUtils.getIntValue(callLogCursor, "duration");
                final int type = CursorExtenUtils.getIntValue(callLogCursor, "type");

                int simIndex = -1;
                try {
                    String subscriptionId = callLogCursor.getString(callLogCursor.getColumnIndexOrThrow("subscription_id"));
                    if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
                        for (int i = 0; i < arrSim.size(); i++) {
                            if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
                                simIndex = i + 1;
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                // Determine call type string
                String callType;
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = mContext.getString(R.string.incoming);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = mContext.getString(R.string.outgoing);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = mContext.getString(R.string.missed_Call);
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = mContext.getString(R.string.block_call);
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        callType = mContext.getString(R.string.declined_call);
                        break;
                    default:
                        callType = mContext.getString(R.string.outgoing);
                }

                String contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(phoneNumber, mContext);
                if (contactRowIDLookupList == null) {
                    contactRowIDLookupList = "";
                }

                callLogEntries.add(new CallLogModel(null, intValue, contactRowIDLookupList, phoneNumber, contactName, photoUri, date, duration, type, simIndex, phoneNumber, "", callType, longValue, intArray[colorPosition], 1, null));



            }
            callLogCursor.close();
        }

        mContactDAO.callLogDAO().deleteAllCallLog();
        mContactDAO.callLogDAO().addAllHistory(callLogEntries);
        Log.e("fatal4", "callLogEntries.size: " + callLogEntries.size() );



    }



    public void invoke10() {
        List<CallLogModel> callLogEntries = new ArrayList<>();
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        String[] projection;
        if (Build.VERSION.SDK_INT >= 23) {
            projection = new String[]{CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.PHONE_ACCOUNT_ID, CallLog.Calls.CACHED_PHOTO_URI};
        } else {
            projection = new String[]{CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.PHONE_ACCOUNT_ID};
        }

        StringBuffer sb = new StringBuffer();
        sb.append("Call Details :");

        Cursor managedCursor;
        if (Build.VERSION.SDK_INT >= 24) {
            managedCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI.buildUpon().build(), projection, null, null, "_id DESC");
        } else {
            managedCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, "_id DESC");
        }

//        Cursor managedCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        if (managedCursor.moveToFirst()) {
            while (managedCursor.moveToNext()) {
                int id = managedCursor.getInt(managedCursor.getColumnIndexOrThrow(CallLog.Calls._ID));
                String phoneNumber = managedCursor.getString(managedCursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String contactName = managedCursor.getString(managedCursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                if (contactName == null) {
                    contactName = phoneNumber;
                }
                int type = managedCursor.getInt(managedCursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                long callDate = managedCursor.getLong(managedCursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
//                Date callDayTime = new Date(Long.parseLong(callDate));
                int duration = managedCursor.getInt(managedCursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
//                String sim = managedCursor.getString(managedCursor.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID));
                String photoUri = managedCursor.getString(managedCursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_PHOTO_URI));
                if (photoUri == null) {
                    photoUri = "";
                }
                String callType = null;

                if (++colorPosition >= intArray.length) {
                    colorPosition = 0;
                }

                switch (type) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = mContext.getString(R.string.outgoing);
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        callType = mContext.getString(R.string.outgoing);
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        callType = mContext.getString(R.string.incoming);
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        callType = mContext.getString(R.string.missed_Call);
                        break;

                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = mContext.getString(R.string.block);
                        break;
                }


                String contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(phoneNumber, mContext);
                if (contactRowIDLookupList == null) {
                    contactRowIDLookupList = "";
                }

                int simIndex = -1;
                callLogEntries.add(new CallLogModel(null, id, contactRowIDLookupList, phoneNumber, contactName, photoUri, (int) (callDate / 1000L), duration, type, simIndex, phoneNumber, "", callType, callDate, intArray[colorPosition]));


            }
        }
        if (managedCursor != null) {
            managedCursor.close();
        }

        mContactDAO.callLogDAO().deleteAllCallLog();
        mContactDAO.callLogDAO().addAllHistory(callLogEntries);
        Log.e("fatal4", "callLogEntries.size: " + callLogEntries.size() );

    }


    public void invoke11() {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList();
        final HashMap hashMap2 = new HashMap();
        final HashMap<String, String> hashMap3 = new HashMap<String, String>();
        Log.e("fatal4", "start: " );
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        Log.e("fatal4", "list2 size: " + list2.size() );
//        ArrayList<SIMAccount> arrSim = AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(mContext);
//        Log.e("fatal4", "getSimIndex: " + arrSim.size() );
        Log.e("fatal4", "list size: " + list.size() );
        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, (String) null, (String[]) null, "_id DESC");
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        // Extract values from the cursor
//                        final int intValue = CursorExtenUtils.getIntValue(cursor, "_id");
                        final int intValue = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
//                        final String stringValue = CursorExtenUtils.getStringValue(cursor, "number");
                        final String stringValue = cursor.getString(cursor.getColumnIndexOrThrow("number"));

                        if (stringValue != null) {
                            // Handle the "name" field
//                            String stringValue2 = CursorExtenUtils.getStringValue(cursor, "name");
                            String stringValue2 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            if (stringValue2 == null) {
                                stringValue2 = "";
                            }
                            String contactName = getContactName(stringValue);
                            if (contactName == null) {
                                contactName = stringValue.replace(" ", "").replace("-", "").replace("+91", "");
                            }

//                            final long longValue = CursorExtenUtils.getLongValue(cursor, "date");
                            final long longValue = cursor.getLong(cursor.getColumnIndexOrThrow("date"));

                            // Handle the "photo_uri" field
//                            String photoUri = CursorExtenUtils.getStringValue(cursor, "photo_uri");
                            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow("photo_uri"));
                            if (photoUri == null) {
                                photoUri = "";
                            }

                            // Reset color position if it exceeds the array length
                            if (++colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }

//                            String s4 = null;
//                            Object o2 = null;
//                            String s6 = null;
//
//                            if (hashMap2.containsKey(stringValue)) {
//                                // Retrieve from cache
//                                final Object value = hashMap2.get(stringValue);
//                                final String s3 = hashMap3.get(stringValue);
//                                s4 = photoUri;
//                                o2 = value;
//                                s6 = s3;
//                            } else {
//                                // Process contacts list
//                                final Collection<Contact> collection = new ArrayList<>();
//                                for (Contact contact : list2) {
//                                    if (!contact.getContactNumber().isEmpty()) {
//                                        collection.add(contact);
//                                    }
//                                }
//                                final List<Contact> list6 = new ArrayList<>(collection);
//                                final String string = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
//                                String s7 = photoUri;
//
//                                for (Contact contact : list6) {
//                                    for (PhoneNumber phoneNumber : contact.getContactNumber()) {
//                                        String trimmedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString();
//                                        String trimmedNormalizedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString();
//
//                                        if (Intrinsics.areEqual(trimmedPhone, string) || Intrinsics.areEqual(trimmedNormalizedPhone, string)) {
//                                            stringValue2 = contact.getFirstNameOriginal();
//                                            String contactPhotoUri = contact.getContactPhotoUri();
//                                            String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
//
//                                            if (contactPhotoUri != null && !contactPhotoUri.isEmpty()) {
//                                                s7 = contactPhotoUri;
//                                            } else if (contactPhotoThumbUri != null && !contactPhotoThumbUri.isEmpty()) {
//                                                s7 = contactPhotoThumbUri;
//                                            }
//                                            hashMap3.put(stringValue, String.valueOf(contact.getContactIdSimple()));
//                                            hashMap2.put(stringValue, stringValue2);
//                                            break;
//                                        }
//                                    }
//                                }
//                                s4 = s7;
//                                o2 = stringValue2;
//                            }
//
//                            // Get contactRowIDLookupList
//                            final String contactRowIDLookupList = (o2 != null && !o2.toString().isEmpty() && (s6 == null || Intrinsics.areEqual(s6, "0")))
//                                    ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
//                                    : s6;
//
//                            if (o2 == null || o2.toString().isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
//                                o2 = stringValue;
//                            }
//
//                            CharSequence displayName = (o2.toString().isEmpty()) ? mContext.getString(R.string.unknown) : (CharSequence) o2;

                            // Extract date, duration, type and simIndex
//                            final int date = (int) (CursorExtenUtils.getLongValue(cursor, "date") / 1000L);
                            final int date = (int) (longValue / 1000L);
//                            final int duration = CursorExtenUtils.getIntValue(cursor, "duration");
                            final int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
//                            final int type = CursorExtenUtils.getIntValue(cursor, "type");
                            final int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));

                            int simIndex = -1;
//                            try {
//                                String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow("subscription_id"));
//                                if (subscriptionId != null && !subscriptionId.isEmpty() && arrSim.size() > 1) {
//                                    for (int i = 0; i < arrSim.size(); i++) {
//                                        if (subscriptionId.equals(arrSim.get(i).getHandle().getId())) {
//                                            simIndex = i + 1;
//                                            break;
//                                        }
//                                    }
//                                }
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//                            }

                            // Determine call type string
                            String callType;
                            switch (type) {
                                case CallLog.Calls.INCOMING_TYPE:
                                    callType = mContext.getString(R.string.incoming);
                                    break;
                                case CallLog.Calls.OUTGOING_TYPE:
                                    callType = mContext.getString(R.string.outgoing);
                                    break;
                                case CallLog.Calls.MISSED_TYPE:
                                    callType = mContext.getString(R.string.missed_Call);
                                    break;
                                case CallLog.Calls.VOICEMAIL_TYPE:
                                case CallLog.Calls.BLOCKED_TYPE:
                                    callType = mContext.getString(R.string.block_call);
                                    break;
                                case CallLog.Calls.REJECTED_TYPE:
                                    callType = mContext.getString(R.string.declined_call);
                                    break;
                                default:
                                    callType = mContext.getString(R.string.outgoing);
                            }

                            String contactRowIDLookupList = AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext);
                            if (contactRowIDLookupList == null) {
                                contactRowIDLookupList = "";
                            }

                            // Add the new call log entry
                            list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, contactName, photoUri, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition]));
                        }
                    } while (cursor.moveToNext());

                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
                Log.e("fatal4", "list size: " + list.size() );
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            Log.e("fatal4", "invoke: " + ex.getMessage() );
        }
    }




    public void invoke1(int batchSize, int batchNumber) {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList<>();
        final HashMap<String, Object> hashMap2 = new HashMap<>();
        final HashMap<String, String> hashMap3 = new HashMap<>();
        Log.e("fatal4", "start: ");
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        Log.e("fatal4", "list size: " + list.size());

        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                String limitOffset = " LIMIT " + batchSize + " OFFSET " + (batchNumber * batchSize);
                cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, "_id DESC" + limitOffset);

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        final int intValue = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                        final String stringValue = cursor.getString(cursor.getColumnIndexOrThrow("number"));

                        if (stringValue != null) {
                            String stringValue2 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                            if (stringValue2 == null) {
                                stringValue2 = "";
                            }

                            final long longValue = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow("photo_uri"));
                            if (photoUri == null) {
                                photoUri = "";
                            }

                            if (++colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }

                            String s4 = null;
                            Object o2 = null;
                            String s6 = null;

                            if (hashMap2.containsKey(stringValue)) {
                                // Retrieve from cache
                                final Object value = hashMap2.get(stringValue);
                                final String s3 = hashMap3.get(stringValue);
                                s4 = photoUri;
                                o2 = value;
                                s6 = s3;
                            } else {
                                // Process contacts list
                                final Collection<Contact> collection = new ArrayList<>();
                                for (Contact contact : list2) {
                                    if (!contact.getContactNumber().isEmpty()) {
                                        collection.add(contact);
                                    }
                                }
                                final List<Contact> list6 = new ArrayList<>(collection);
                                final String string = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
                                String s7 = photoUri;

                                for (Contact contact : list6) {
                                    for (PhoneNumber phoneNumber : contact.getContactNumber()) {
                                        String trimmedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString();
                                        String trimmedNormalizedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString();

                                        if (Intrinsics.areEqual(trimmedPhone, string) || Intrinsics.areEqual(trimmedNormalizedPhone, string)) {
                                            stringValue2 = contact.getFirstNameOriginal();
                                            String contactPhotoUri = contact.getContactPhotoUri();
                                            String contactPhotoThumbUri = contact.getContactPhotoThumbUri();

                                            if (contactPhotoUri != null && !contactPhotoUri.isEmpty()) {
                                                s7 = contactPhotoUri;
                                            } else if (contactPhotoThumbUri != null && !contactPhotoThumbUri.isEmpty()) {
                                                s7 = contactPhotoThumbUri;
                                            }
                                            hashMap3.put(stringValue, String.valueOf(contact.getContactIdSimple()));
                                            hashMap2.put(stringValue, stringValue2);
                                            break;
                                        }
                                    }
                                }
                                s4 = s7;
                                o2 = stringValue2;
                            }

                            // Get contactRowIDLookupList
                            final String contactRowIDLookupList = (o2 != null && !o2.toString().isEmpty() && (s6 == null || Intrinsics.areEqual(s6, "0")))
                                    ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
                                    : s6;

                            if (o2 == null || o2.toString().isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
                                o2 = stringValue;
                            }

                            CharSequence displayName = (o2.toString().isEmpty()) ? mContext.getString(R.string.unknown) : (CharSequence) o2;

                            final int date = (int) (longValue / 1000L);
                            final int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                            final int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));

                            int simIndex = -1;

                            String callType;
                            switch (type) {
                                case CallLog.Calls.INCOMING_TYPE:
                                    callType = mContext.getString(R.string.incoming);
                                    break;
                                case CallLog.Calls.OUTGOING_TYPE:
                                    callType = mContext.getString(R.string.outgoing);
                                    break;
                                case CallLog.Calls.MISSED_TYPE:
                                    callType = mContext.getString(R.string.missed_Call);
                                    break;
                                case CallLog.Calls.VOICEMAIL_TYPE:
                                case CallLog.Calls.BLOCKED_TYPE:
                                    callType = mContext.getString(R.string.block_call);
                                    break;
                                case CallLog.Calls.REJECTED_TYPE:
                                    callType = mContext.getString(R.string.declined_call);
                                    break;
                                default:
                                    callType = mContext.getString(R.string.outgoing);
                            }

                            list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, displayName.toString(), s4, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition]));
                        }
                    } while (cursor.moveToNext());

                    cursor.close();  // Close the cursor when done
                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
                Log.e("fatal4", "list size: " + list.size());
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            Log.e("fatal4", "invoke: " + ex.getMessage());
        }
    }

    public void invoke2(int batchSize, int batchNumber) {
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        final List<CallLogModel> list = new ArrayList<>();
        final HashMap<String, Object> hashMap2 = new HashMap<>();
        final HashMap<String, String> hashMap3 = new HashMap<>();
        Log.e("fatal4", "start: ");
        List<Contact> list2 = AddAllCallLogsInDatabaseUtils.asList(new GetAllContactNumberHelper(mContext, mContactDAO).invoke());
        Log.e("fatal4", "list size: " + list.size());

        try {
            if (AddAllCallLogsInDatabaseUtils.isAllPermissionGranted(mContext)) {
                Cursor cursor;
                cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, "_id DESC");

                if (cursor != null) {
                    int start = batchNumber * batchSize;
                    int end = start + batchSize;

                    if (cursor.moveToPosition(start)) {
                        int currentIndex = 0;

                        do {
                            if (currentIndex >= end - start) {
                                break;
                            }

                            final int intValue = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                            final String stringValue = cursor.getString(cursor.getColumnIndexOrThrow("number"));

                            if (stringValue != null) {
                                String stringValue2 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                                if (stringValue2 == null) {
                                    stringValue2 = "";
                                }

                                final long longValue = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                                String photoUri = cursor.getString(cursor.getColumnIndexOrThrow("photo_uri"));
                                if (photoUri == null) {
                                    photoUri = "";
                                }

                                if (++colorPosition >= intArray.length) {
                                    colorPosition = 0;
                                }

                                String s4 = null;
                                Object o2 = null;
                                String s6 = null;

                                if (hashMap2.containsKey(stringValue)) {
                                    // Retrieve from cache
                                    final Object value = hashMap2.get(stringValue);
                                    final String s3 = hashMap3.get(stringValue);
                                    s4 = photoUri;
                                    o2 = value;
                                    s6 = s3;
                                } else {
                                    // Process contacts list
                                    final Collection<Contact> collection = new ArrayList<>();
                                    for (Contact contact : list2) {
                                        if (!contact.getContactNumber().isEmpty()) {
                                            collection.add(contact);
                                        }
                                    }
                                    final List<Contact> list6 = new ArrayList<>(collection);
                                    final String string = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(stringValue, "-", "", false), " ", "", false), "+91", "", false)).toString();
                                    String s7 = photoUri;

                                    for (Contact contact : list6) {
                                        for (PhoneNumber phoneNumber : contact.getContactNumber()) {
                                            String trimmedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getValue(), "-", "", false), " ", "", false), "+91", "", false)).toString();
                                            String trimmedNormalizedPhone = StringsKt.trim(StringsKt.replace(StringsKt.replace(StringsKt.replace(phoneNumber.getNormalizedNumber(), "-", "", false), " ", "", false), "+91", "", false)).toString();

                                            if (Intrinsics.areEqual(trimmedPhone, string) || Intrinsics.areEqual(trimmedNormalizedPhone, string)) {
                                                stringValue2 = contact.getFirstNameOriginal();
                                                String contactPhotoUri = contact.getContactPhotoUri();
                                                String contactPhotoThumbUri = contact.getContactPhotoThumbUri();

                                                if (contactPhotoUri != null && !contactPhotoUri.isEmpty()) {
                                                    s7 = contactPhotoUri;
                                                } else if (contactPhotoThumbUri != null && !contactPhotoThumbUri.isEmpty()) {
                                                    s7 = contactPhotoThumbUri;
                                                }
                                                hashMap3.put(stringValue, String.valueOf(contact.getContactIdSimple()));
                                                hashMap2.put(stringValue, stringValue2);
                                                break;
                                            }
                                        }
                                    }
                                    s4 = s7;
                                    o2 = stringValue2;
                                }

                                // Get contactRowIDLookupList
                                final String contactRowIDLookupList = (o2 != null && !o2.toString().isEmpty() && (s6 == null || Intrinsics.areEqual(s6, "0")))
                                        ? AddAllCallLogsInDatabaseUtils.getContactRowIDLookupList(stringValue, mContext)
                                        : s6;

                                if (o2 == null || o2.toString().isEmpty() || contactRowIDLookupList == null || Intrinsics.areEqual(contactRowIDLookupList, "0") || Intrinsics.areEqual(contactRowIDLookupList, "") || Intrinsics.areEqual(contactRowIDLookupList, "null")) {
                                    o2 = stringValue;
                                }

                                CharSequence displayName = (o2.toString().isEmpty()) ? mContext.getString(R.string.unknown) : (CharSequence) o2;

                                final int date = (int) (longValue / 1000L);
                                final int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                                final int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));

                                int simIndex = -1;

                                String callType;
                                switch (type) {
                                    case CallLog.Calls.INCOMING_TYPE:
                                        callType = mContext.getString(R.string.incoming);
                                        break;
                                    case CallLog.Calls.OUTGOING_TYPE:
                                        callType = mContext.getString(R.string.outgoing);
                                        break;
                                    case CallLog.Calls.MISSED_TYPE:
                                        callType = mContext.getString(R.string.missed_Call);
                                        break;
                                    case CallLog.Calls.VOICEMAIL_TYPE:
                                    case CallLog.Calls.BLOCKED_TYPE:
                                        callType = mContext.getString(R.string.block_call);
                                        break;
                                    case CallLog.Calls.REJECTED_TYPE:
                                        callType = mContext.getString(R.string.declined_call);
                                        break;
                                    default:
                                        callType = mContext.getString(R.string.outgoing);
                                }

                                list.add(new CallLogModel(null, intValue, contactRowIDLookupList, stringValue, displayName.toString(), s4, date, duration, type, simIndex, stringValue, "", callType, longValue, intArray[colorPosition]));
                            }

                            currentIndex++;
                        } while (cursor.moveToNext());

                        cursor.close(); // Close the cursor when done
                    }
                }

                mContactDAO.callLogDAO().deleteAllCallLog();
                mContactDAO.callLogDAO().addAllHistory(list);
                Log.e("fatal4", "list size: " + list.size());
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            Log.e("fatal4", "invoke: " + ex.getMessage());
        }
    }




    public void invoke12() {
        Log.e("fatal4", "getCallLogs: " + "start" );
        final int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        int colorPosition = 0;
        List<CallLogModel> callLogs = new ArrayList<>();
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE,
                CallLog.Calls.CACHED_PHOTO_URI,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID  // This field is subscription_id
        };
        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls._ID + " DESC");

        if (cursor != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

            while (cursor.moveToNext()) {
                if (++colorPosition >= intArray.length) {
                    colorPosition = 0;
                }

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls._ID));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                if (name == null) {
                    name = number;
                }
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_PHOTO_URI));
                if (photoUri == null) {
                    photoUri = "";
                }
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
//                String subscriptionId = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID));
                int simID = 0;

                String dateFormat = sdf.format(new Date(date));

                String callType = "";
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = mContext.getString(R.string.incoming);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = mContext.getString(R.string.outgoing);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = mContext.getString(R.string.missed_Call);
                        break;
                    case CallLog.Calls.VOICEMAIL_TYPE:
                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = mContext.getString(R.string.block_call);
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        callType = mContext.getString(R.string.declined_call);
                        break;
                    default:
                        callType = mContext.getString(R.string.outgoing);
                }

                String contactRowIDLookupList = "";
//                String contactRowIDLookupList = ReadContact.getIdWithNumber(context, number);
//                if (contactRowIDLookupList == null) {
//                    contactRowIDLookupList = "";
//                }

                CallLogModel model = new CallLogModel((Integer) id, id, contactRowIDLookupList, number, name, photoUri, (int) (date / 1000L), duration, type, simID, number, "", callType, date, intArray[colorPosition]);
                callLogs.add(model);

            }
            cursor.close();

        }

        mContactDAO.callLogDAO().deleteAllCallLog();
        mContactDAO.callLogDAO().addAllHistory(callLogs);
        Log.e("fatal4", "list size: " + callLogs.size() );

    }



}
