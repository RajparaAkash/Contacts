package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.SparseArray;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Organization;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetEmailsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetEventsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetNotesHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetOrganisationsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetPhoneNumbersHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetWebsitesHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class GetAllContactNumberHelper {
    private final ContactDatabase mContactDAO;
    private final Context mContext;
    private final String sorting = "data2 ASC";
    private ArrayList<String> accountList = new ArrayList<>();
    private int colorPosition;
    private String[] projection = {"mimetype", "contact_id", "raw_contact_id", "data4", "data2", "data5", "data3", "data6", "photo_uri", "photo_thumb_uri", "starred", "custom_ringtone", "account_name", "account_type", "mimetype"};

    public GetAllContactNumberHelper(Context context, ContactDatabase contactDatabase) {
        this.mContext = context;
        this.mContactDAO = contactDatabase;
    }

    private boolean isAllPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PreferencesManager.hasPermission(context, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"});
        }
        return true;
    }

    public SparseArray<Contact> invoke1() {
        SparseArray<Contact> sparseArray;
        Exception e;
        int i;
        String str;
        String str2 = null;
        int i2 = 0;
        String[] strArr = new String[0];
        String str3 = null;
        Throwable th;
        Cursor cursor;
        Throwable th2;
        Cursor cursor2;
        Exception e2;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        Throwable th3;
        String str9 = "vnd.android.cursor.item/name";
        SparseArray<Contact> sparseArray2 = new SparseArray<>();
        try {
            ArrayList arrayList = new ArrayList();
            Iterator<ContactSource> it = this.mContactDAO.contactSourceDAO().getAllAccounts().iterator();
            while (true) {
                i = 1;
                str = "";
                if (!it.hasNext()) {
                    break;
                }
                ContactSource next = it.next();
                if (true ^ Intrinsics.areEqual(next.getName(), str)) {
                    arrayList.add(next);
                }
            }
            ArrayList<ContactSource> arrayList2 = arrayList;
            ArrayList<String> arrayList3 = new ArrayList<>(CollectionsKt.collectionSizeOrDefault(arrayList2, 10));
            for (ContactSource contactSource : arrayList2) {
                arrayList3.add(contactSource.getName());
            }
            this.accountList = arrayList3;
            int[] intArray = this.mContext.getResources().getIntArray(R.array.thumb_color);
            int i3 = 2;
            String[] strArr2 = {"vnd.android.cursor.item/organization", str9};
            int i4 = 0;
            int i5 = 0;
            while (i5 < i3) {
                String[] strArr3 = new String[i];
                strArr3[i4] = strArr2[i5];
                if (isAllPermissionGranted(this.mContext)) {
                    Uri uri = ContactsContract.Data.CONTENT_URI;
                    Cursor query = this.mContext.getContentResolver().query(uri, this.projection, "mimetype = ?", strArr3, this.sorting);
                    if (query != null) {
                        Cursor cursor3 = query;
                        try {
                            Cursor cursor4 = cursor3;
                            while (cursor4.moveToNext()) {
                                try {
                                    int i6 = this.colorPosition + i;
                                    this.colorPosition = i6;
                                    if (i6 >= intArray.length) {
                                        try {
                                            this.colorPosition = i4;
                                        } catch (Exception e3) {
                                            e2 = e3;
                                            sparseArray = sparseArray2;
                                            cursor2 = cursor3;
                                            try {
                                                System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                                Unit unit = Unit.INSTANCE;
                                                CloseableKt.closeFinally(cursor2, null);
                                                Unit unit2 = Unit.INSTANCE;
                                                i5 = i2 + 1;
                                                sparseArray2 = sparseArray;
                                                str = str3;
                                                strArr2 = strArr;
                                                str9 = str2;
                                                i3 = 2;
                                                i = 1;
                                                i4 = 0;
                                            } catch (Throwable th4) {
                                                th2 = th4;
                                                cursor = cursor2;
                                                th = th2;
                                                try {
                                                    throw th;
                                                } catch (Throwable th5) {
                                                    CloseableKt.closeFinally(cursor, th);
                                                    throw th5;
                                                }
                                            }
                                        } catch (Throwable th6) {
                                        }
                                    }
                                    cursor4.getColumnIndex("raw_contact_id");
                                    int columnIndex = cursor4.getColumnIndex("starred");
                                    int columnIndex2 = cursor4.getColumnIndex(MyContactsContentProvider.COL_PHOTO_URI);
                                    int columnIndex3 = cursor4.getColumnIndex("photo_thumb_uri");
                                    int columnIndex4 = cursor4.getColumnIndex("data4");
                                    int columnIndex5 = cursor4.getColumnIndex("data2");
                                    int columnIndex6 = cursor4.getColumnIndex("data5");
                                    str3 = str;
                                    int columnIndex7 = cursor4.getColumnIndex("data3");
                                    strArr = strArr2;
                                    int columnIndex8 = cursor4.getColumnIndex("data6");
                                    i2 = i5;
                                    String string = cursor4.getString(cursor4.getColumnIndexOrThrow("mimetype"));
                                    if (Intrinsics.areEqual(string, str9)) {
                                        str4 = cursor4.getString(columnIndex4);
                                        if (str4 == null) {
                                            str2 = str9;
                                            str4 = str3;
                                        } else {
                                            str2 = str9;
                                            Intrinsics.checkNotNullExpressionValue(str4, "it.getString(indexPrefix) ?: \"\"");
                                        }
                                        str8 = cursor4.getString(columnIndex5);
                                        if (str8 == null) {
                                            str8 = str3;
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str8, "it.getString(fNameIndex) ?: \"\"");
                                        }
                                        str7 = cursor4.getString(columnIndex6);
                                        if (str7 == null) {
                                            str7 = str3;
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str7, "it.getString(mNameIndex) ?: \"\"");
                                        }
                                        str6 = cursor4.getString(columnIndex7);
                                        if (str6 == null) {
                                            str6 = str3;
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str6, "it.getString(sNameIndex) ?: \"\"");
                                        }
                                        str5 = cursor4.getString(columnIndex8);
                                        if (str5 == null) {
                                            str5 = str3;
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str5, "it.getString(suffixIndex) ?: \"\"");
                                        }
                                    } else {
                                        str2 = str9;
                                        str8 = str3;
                                        str7 = str8;
                                        str6 = str7;
                                        str5 = str6;
                                        str4 = str5;
                                    }
                                    String string2 = cursor4.getString(cursor4.getColumnIndexOrThrow("account_name"));
                                    if (string2 == null) {
                                        cursor2 = cursor3;
                                        string2 = str3;
                                    } else {
                                        cursor2 = cursor3;
                                        try {
                                            Intrinsics.checkNotNullExpressionValue(string2, "it.getString(accountNameIndex) ?: \"\"");
                                        } catch (Exception e4) {
                                            e2 = e4;
                                            sparseArray = sparseArray2;
                                            System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                            Unit unit3 = Unit.INSTANCE;
                                            CloseableKt.closeFinally(cursor2, null);
                                            Unit unit22 = Unit.INSTANCE;
                                            i5 = i2 + 1;
                                            sparseArray2 = sparseArray;
                                            str = str3;
                                            strArr2 = strArr;
                                            str9 = str2;
                                            i3 = 2;
                                            i = 1;
                                            i4 = 0;
                                        } catch (Throwable th7) {
                                            th3 = th7;
                                            th = th3;
                                            cursor = cursor2;
                                            throw th;
                                        }
                                    }
                                    int intValue = CursorExtenUtils.getIntValue(cursor4, "contact_id");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str8);
                                    try {
                                        sb.append(' ');
                                        sb.append(str7);
                                        sb.append(' ');
                                        sb.append(str6);
                                        String sb2 = sb.toString();
                                        if (!Intrinsics.areEqual(string2, "Skype")) {
                                            int intValue2 = CursorExtenUtils.getIntValue(cursor4, "raw_contact_id");
                                            String obj = StringsKt.trim((CharSequence) str4).toString();
                                            String obj2 = StringsKt.trim((CharSequence) str8).toString();
                                            String obj3 = StringsKt.trim((CharSequence) StringsKt.trim((CharSequence) sb2).toString()).toString();
                                            String obj4 = StringsKt.trim((CharSequence) str7).toString();
                                            String obj5 = StringsKt.trim((CharSequence) str6).toString();
                                            String obj6 = StringsKt.trim((CharSequence) str5).toString();
                                            ArrayList arrayList4 = new ArrayList();
                                            ArrayList arrayList5 = new ArrayList();
                                            ArrayList arrayList6 = new ArrayList();
                                            ArrayList arrayList7 = new ArrayList();
                                            ArrayList arrayList8 = new ArrayList();
                                            ArrayList arrayList9 = new ArrayList();
                                            String string3 = cursor4.getString(columnIndex2);
                                            String str10 = string3 == null ? null : string3;
                                            String string4 = cursor4.getString(columnIndex3);
                                            sparseArray = sparseArray2;
                                            try {
                                                sparseArray.put(CursorExtenUtils.getIntValue(cursor4, "raw_contact_id"), new Contact(null, intValue2, intValue, obj, obj2, obj3, obj4, obj5, obj6, str10, string4 == null ? null : string4, cursor4.getInt(columnIndex) == 1, null, arrayList4, arrayList5, arrayList7, arrayList6, arrayList8, arrayList9, string2, new ArrayList(), "", "", "", string, Integer.valueOf(intArray[this.colorPosition]), 1, null));
                                                sparseArray2 = sparseArray;
                                                str = str3;
                                                strArr2 = strArr;
                                                i5 = i2;
                                                str9 = str2;
                                                cursor3 = cursor2;
                                            } catch (Exception e5) {
                                                e2 = e5;
                                                System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                                Unit unit32 = Unit.INSTANCE;
                                                CloseableKt.closeFinally(cursor2, null);
                                                Unit unit222 = Unit.INSTANCE;
                                                i5 = i2 + 1;
                                                sparseArray2 = sparseArray;
                                                str = str3;
                                                strArr2 = strArr;
                                                str9 = str2;
                                                i3 = 2;
                                                i = 1;
                                                i4 = 0;
                                            } catch (Throwable th8) {
                                                th3 = th8;
                                                th = th3;
                                                cursor = cursor2;
                                                throw th;
                                            }
                                        } else {
                                            str = str3;
                                            strArr2 = strArr;
                                            i5 = i2;
                                            str9 = str2;
                                            cursor3 = cursor2;
                                            sparseArray2 = sparseArray2;
                                        }
                                        i = 1;
                                        i4 = 0;
                                    } catch (Exception e6) {
                                        e2 = e6;
                                        sparseArray = sparseArray2;
                                        System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                        Unit unit322 = Unit.INSTANCE;
                                        CloseableKt.closeFinally(cursor2, null);
                                        Unit unit2222 = Unit.INSTANCE;
                                        i5 = i2 + 1;
                                        sparseArray2 = sparseArray;
                                        str = str3;
                                        strArr2 = strArr;
                                        str9 = str2;
                                        i3 = 2;
                                        i = 1;
                                        i4 = 0;
                                    } catch (Throwable th9) {
                                    }
                                } catch (Exception e7) {
                                    e2 = e7;
                                    str2 = str9;
                                    sparseArray = sparseArray2;
                                    cursor2 = cursor3;
                                    System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                    Unit unit3222 = Unit.INSTANCE;
                                    CloseableKt.closeFinally(cursor2, null);
                                    Unit unit22222 = Unit.INSTANCE;
                                    i5 = i2 + 1;
                                    sparseArray2 = sparseArray;
                                    str = str3;
                                    strArr2 = strArr;
                                    str9 = str2;
                                    i3 = 2;
                                    i = 1;
                                    i4 = 0;
                                } catch (Throwable th10) {
                                }
                            }
                            str2 = str9;
                            sparseArray = sparseArray2;
                            cursor2 = cursor3;
                            str3 = str;
                            strArr = strArr2;
                            i2 = i5;
                            Unit unit32222 = Unit.INSTANCE;
                            try {
                                CloseableKt.closeFinally(cursor2, null);
                                Unit unit222222 = Unit.INSTANCE;
                                i5 = i2 + 1;
                                sparseArray2 = sparseArray;
                                str = str3;
                                strArr2 = strArr;
                                str9 = str2;
                                i3 = 2;
                                i = 1;
                                i4 = 0;
                            } catch (Exception e8) {
                                e = e8;
                            }
                        } catch (Throwable th11) {
                            th2 = th11;
                            cursor = cursor3;
                            th = th2;
                        }
                    }
                }
//                str2 = str9;
//                sparseArray = sparseArray2;
//                str3 = str;
//                strArr = strArr2;
//                i2 = i5;
//                i5 = i2 + 1;
//                sparseArray2 = sparseArray;
//                str = str3;
//                strArr2 = strArr;
//                str9 = str2;
//                i3 = 2;
//                i = 1;
//                i4 = 0;
            }
            sparseArray = sparseArray2;
            SparseArray invoke$default = GetPhoneNumbersHelper.invoke$default(new GetPhoneNumbersHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default2 = GetEmailsHelper.invoke$default(new GetEmailsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default3 = GetEventsHelper.invoke$default(new GetEventsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default4 = GetWebsitesHelper.invoke$default(new GetWebsitesHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default5 = GetOrganisationsHelper.invoke$default(new GetOrganisationsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default6 = GetNotesHelper.invoke$default(new GetNotesHelper(), this.mContext, null, this.accountList, 2, null);
            int size = invoke$default.size();
            for (int i7 = 0; i7 < size; i7++) {
                int keyAt = invoke$default.keyAt(i7);
                if (sparseArray.get(keyAt) != null) {
                    ArrayList<PhoneNumber> arrayList10 = (ArrayList) invoke$default.valueAt(i7);
                    sparseArray.get(keyAt).setContactNumber(arrayList10);
                }
            }
            int size2 = invoke$default2.size();
            for (int i8 = 0; i8 < size2; i8++) {
                Contact contact = sparseArray.get(invoke$default2.keyAt(i8));
                Object valueAt = invoke$default2.valueAt(i8);
                contact.setContactEmail((ArrayList) valueAt);
            }
            int size3 = invoke$default3.size();
            for (int i9 = 0; i9 < size3; i9++) {
                Contact contact2 = sparseArray.get(invoke$default3.keyAt(i9));
                if (contact2 != null) {
                    Object valueAt2 = invoke$default3.valueAt(i9);
                    contact2.setContactEvent((ArrayList) valueAt2);
                }
            }
            int size4 = invoke$default4.size();
            for (int i10 = 0; i10 < size4; i10++) {
                Contact contact3 = sparseArray.get(invoke$default4.keyAt(i10));
                if (contact3 != null) {
                    Object valueAt3 = invoke$default4.valueAt(i10);
                    contact3.setWebsites((ArrayList) valueAt3);
                }
            }
            int size5 = invoke$default6.size();
            for (int i11 = 0; i11 < size5; i11++) {
                Contact contact4 = sparseArray.get(invoke$default6.keyAt(i11));
                if (contact4 != null) {
                    Object valueAt4 = invoke$default6.valueAt(i11);
                    contact4.setContactNotes((ArrayList) valueAt4);
                }
            }
            int size6 = invoke$default5.size();
            for (int i12 = 0; i12 < size6; i12++) {
                int keyAt2 = invoke$default5.keyAt(i12);
                ArrayList arrayList11 = (ArrayList) invoke$default5.valueAt(i12);
                if (size6 > 0) {
                    Contact contact5 = sparseArray.get(keyAt2);
                    if (contact5 != null) {
                        contact5.setCompany(((Organization) arrayList11.get(0)).getCompany());
                    }
                    Contact contact6 = sparseArray.get(keyAt2);
                    if (contact6 != null) {
                        contact6.setJobTitle(((Organization) arrayList11.get(0)).getJobTitle());
                    }
                    Contact contact7 = sparseArray.get(keyAt2);
                    if (contact7 != null) {
                        contact7.setJobPosition(((Organization) arrayList11.get(0)).getJobPosition());
                        Unit unit4 = Unit.INSTANCE;
                    }
                }
                Unit unit42 = Unit.INSTANCE;
            }
        } catch (Exception e9) {
            e = e9;
            sparseArray = sparseArray2;
            e.printStackTrace();
            return sparseArray;
        }
        return sparseArray;
    }



    public SparseArray<Contact> invoke2() {
        Exception e;
        int i;
        String str;
        String str2 = null;
        int i2 = 0;
        String[] strArr = new String[0];
        String str3 = null;
        Exception e2;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9 = "vnd.android.cursor.item/name";
        SparseArray<Contact> sparseArray = new SparseArray<>();
        try {
            ArrayList arrayList = new ArrayList();
            Iterator<ContactSource> it = this.mContactDAO.contactSourceDAO().getAllAccounts().iterator();
            while (true) {
                i = 1;
                str = "";
                if (!it.hasNext()) {
                    break;
                }
                ContactSource next = it.next();
                if (true ^ Intrinsics.areEqual(next.getName(), str)) {
                    arrayList.add(next);
                }
            }
            ArrayList<ContactSource> arrayList2 = arrayList;
            ArrayList<String> arrayList3 = new ArrayList<>(CollectionsKt.collectionSizeOrDefault(arrayList2, 10));
            for (ContactSource contactSource : arrayList2) {
                arrayList3.add(contactSource.getName());
            }
            this.accountList = arrayList3;
            int[] intArray = this.mContext.getResources().getIntArray(R.array.thumb_color);
            int i3 = 2;
            String[] strArr2 = {"vnd.android.cursor.item/organization", str9};
            int i4 = 0;
            int i5 = 0;
            while (i5 < i3) {
                String[] strArr3 = new String[i];
                strArr3[i4] = strArr2[i5];
                if (isAllPermissionGranted(this.mContext)) {
                    Uri uri = ContactsContract.Data.CONTENT_URI;
                    Cursor cursor4 = this.mContext.getContentResolver().query(uri, this.projection, "mimetype = ?", strArr3, this.sorting);
                    if (cursor4 != null) {
                        try {
                            while (cursor4.moveToNext()) {
                                try {
                                    int i6 = this.colorPosition + i;
                                    this.colorPosition = i6;
                                    if (i6 >= intArray.length) {
                                        this.colorPosition = i4;
                                    }
//                                    cursor4.getColumnIndex("raw_contact_id");
                                    int columnIndex = cursor4.getColumnIndex("starred");
                                    int columnIndex2 = cursor4.getColumnIndex(MyContactsContentProvider.COL_PHOTO_URI);
                                    int columnIndex3 = cursor4.getColumnIndex("photo_thumb_uri");
                                    int columnIndex4 = cursor4.getColumnIndex("data4");
                                    int columnIndex5 = cursor4.getColumnIndex("data2");
                                    int columnIndex6 = cursor4.getColumnIndex("data5");
                                    str3 = str;
                                    int columnIndex7 = cursor4.getColumnIndex("data3");
                                    strArr = strArr2;
                                    int columnIndex8 = cursor4.getColumnIndex("data6");
                                    i2 = i5;
                                    String string = cursor4.getString(cursor4.getColumnIndexOrThrow("mimetype"));
                                    if (Intrinsics.areEqual(string, str9)) {
                                        str4 = cursor4.getString(columnIndex4);
                                        if (str4 == null) {
                                            str2 = str9;
                                            str4 = str3;
                                        }
                                        str8 = cursor4.getString(columnIndex5);
                                        if (str8 == null) {
                                            str8 = str3;
                                        }
                                        str7 = cursor4.getString(columnIndex6);
                                        if (str7 == null) {
                                            str7 = str3;
                                        }
                                        str6 = cursor4.getString(columnIndex7);
                                        if (str6 == null) {
                                            str6 = str3;
                                        }
                                        str5 = cursor4.getString(columnIndex8);
                                        if (str5 == null) {
                                            str5 = str3;
                                        }
                                    } else {
                                        str2 = str9;
                                        str8 = str3;
                                        str7 = str8;
                                        str6 = str7;
                                        str5 = str6;
                                        str4 = str5;
                                    }
                                    String string2 = cursor4.getString(cursor4.getColumnIndexOrThrow("account_name"));
                                    if (string2 == null) {
                                        string2 = str3;
                                    }
                                    int intValue = CursorExtenUtils.getIntValue(cursor4, "contact_id");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str8);
                                    try {
                                        sb.append(' ');
                                        if (!str7.isEmpty()) {
                                            sb.append(str7);
                                            sb.append(' ');
                                        }
                                        sb.append(str6);
                                        String sb2 = sb.toString();
                                        if (!Intrinsics.areEqual(string2, "Skype")) {
                                            int intValue2 = CursorExtenUtils.getIntValue(cursor4, "raw_contact_id");
                                            String obj = StringsKt.trim((CharSequence) str4).toString();
                                            String obj2 = StringsKt.trim((CharSequence) str8).toString();
                                            String obj3 = StringsKt.trim((CharSequence) StringsKt.trim((CharSequence) sb2).toString()).toString();
                                            String obj4 = StringsKt.trim((CharSequence) str7).toString();
                                            String obj5 = StringsKt.trim((CharSequence) str6).toString();
                                            String obj6 = StringsKt.trim((CharSequence) str5).toString();
                                            ArrayList arrayList4 = new ArrayList();
                                            ArrayList arrayList5 = new ArrayList();
                                            ArrayList arrayList6 = new ArrayList();
                                            ArrayList arrayList7 = new ArrayList();
                                            ArrayList arrayList8 = new ArrayList();
                                            ArrayList arrayList9 = new ArrayList();
                                            String string3 = cursor4.getString(columnIndex2);
                                            String str10 = string3 == null ? null : string3;
                                            String string4 = cursor4.getString(columnIndex3);
                                            try {
                                                sparseArray.put(CursorExtenUtils.getIntValue(cursor4, "raw_contact_id"), new Contact(null, intValue2, intValue, obj, obj2, obj3, obj4, obj5, obj6, str10, string4 == null ? null : string4, cursor4.getInt(columnIndex) == 1, null, arrayList4, arrayList5, arrayList7, arrayList6, arrayList8, arrayList9, string2, new ArrayList(), "", "", "", string, Integer.valueOf(intArray[this.colorPosition]), 1, null));
                                                str = str3;
                                                strArr2 = strArr;
                                                i5 = i2;
                                                str9 = str2;
                                            } catch (Exception e5) {
                                                e2 = e5;
                                                System.out.println((Object) ("contact loading error : " + e2.getMessage()));
                                                if (cursor4 != null) {
                                                    cursor4.close();
                                                }
//                                                i5 = i2 + 1;
//                                                str = str3;
//                                                strArr2 = strArr;
//                                                str9 = str2;
//                                                i3 = 2;
//                                                i = 1;
//                                                i4 = 0;
                                            } catch (Throwable th8) {
                                            }
                                        } else {
                                            str = str3;
                                            strArr2 = strArr;
                                            i5 = i2;
                                            str9 = str2;
                                        }
                                        i = 1;
                                        i4 = 0;
                                    } catch (Exception e6) {
                                        e2 = e6;
                                        if (cursor4 != null) {
                                            cursor4.close();
                                        }
//                                        i5 = i2 + 1;
//                                        str = str3;
//                                        strArr2 = strArr;
//                                        str9 = str2;
//                                        i3 = 2;
//                                        i = 1;
//                                        i4 = 0;
                                    } catch (Throwable th9) {
                                    }
                                } catch (Exception e7) {
                                    e2 = e7;
//                                    str2 = str9;
                                    if (cursor4 != null) {
                                        cursor4.close();
                                    }
//                                    i5 = i2 + 1;
//                                    str = str3;
//                                    strArr2 = strArr;
//                                    str9 = str2;
//                                    i3 = 2;
//                                    i = 1;
//                                    i4 = 0;
                                } catch (Throwable th10) {
                                }
                            }
                            str2 = str9;
                            str3 = str;
                            strArr = strArr2;
                            i2 = i5;
                            try {
                                if (cursor4 != null) {
                                    cursor4.close();
                                }
                                i5 = i2 + 1;
                                str = str3;
                                strArr2 = strArr;
                                str9 = str2;
                                i3 = 2;
                                i = 1;
                                i4 = 0;
                            } catch (Exception e8) {
                                e = e8;
                            }
                        } catch (Throwable th11) {
                        }
                    }
                }
//                str2 = str9;
//                sparseArray = sparseArray2;
//                str3 = str;
//                strArr = strArr2;
//                i2 = i5;
//                i5 = i2 + 1;
//                sparseArray2 = sparseArray;
//                str = str3;
//                strArr2 = strArr;
//                str9 = str2;
//                i3 = 2;
//                i = 1;
//                i4 = 0;
            }
            SparseArray invoke$default = GetPhoneNumbersHelper.invoke$default(new GetPhoneNumbersHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default2 = GetEmailsHelper.invoke$default(new GetEmailsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default3 = GetEventsHelper.invoke$default(new GetEventsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default4 = GetWebsitesHelper.invoke$default(new GetWebsitesHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default5 = GetOrganisationsHelper.invoke$default(new GetOrganisationsHelper(), this.mContext, null, this.accountList, 2, null);
            SparseArray invoke$default6 = GetNotesHelper.invoke$default(new GetNotesHelper(), this.mContext, null, this.accountList, 2, null);
            int size = invoke$default.size();
            for (int i7 = 0; i7 < size; i7++) {
                int keyAt = invoke$default.keyAt(i7);
                if (sparseArray.get(keyAt) != null) {
                    ArrayList<PhoneNumber> arrayList10 = (ArrayList) invoke$default.valueAt(i7);
                    sparseArray.get(keyAt).setContactNumber(arrayList10);
                }
            }
            int size2 = invoke$default2.size();
            for (int i8 = 0; i8 < size2; i8++) {
                Contact contact = sparseArray.get(invoke$default2.keyAt(i8));
                Object valueAt = invoke$default2.valueAt(i8);
                contact.setContactEmail((ArrayList) valueAt);
            }
            int size3 = invoke$default3.size();
            for (int i9 = 0; i9 < size3; i9++) {
                Contact contact2 = sparseArray.get(invoke$default3.keyAt(i9));
                if (contact2 != null) {
                    Object valueAt2 = invoke$default3.valueAt(i9);
                    contact2.setContactEvent((ArrayList) valueAt2);
                }
            }
            int size4 = invoke$default4.size();
            for (int i10 = 0; i10 < size4; i10++) {
                Contact contact3 = sparseArray.get(invoke$default4.keyAt(i10));
                if (contact3 != null) {
                    Object valueAt3 = invoke$default4.valueAt(i10);
                    contact3.setWebsites((ArrayList) valueAt3);
                }
            }
            int size5 = invoke$default6.size();
            for (int i11 = 0; i11 < size5; i11++) {
                Contact contact4 = sparseArray.get(invoke$default6.keyAt(i11));
                if (contact4 != null) {
                    Object valueAt4 = invoke$default6.valueAt(i11);
                    contact4.setContactNotes((ArrayList) valueAt4);
                }
            }
            int size6 = invoke$default5.size();
            for (int i12 = 0; i12 < size6; i12++) {
                int keyAt2 = invoke$default5.keyAt(i12);
                ArrayList arrayList11 = (ArrayList) invoke$default5.valueAt(i12);
                if (size6 > 0) {
                    Contact contact5 = sparseArray.get(keyAt2);
                    if (contact5 != null) {
                        contact5.setCompany(((Organization) arrayList11.get(0)).getCompany());
                    }
                    Contact contact6 = sparseArray.get(keyAt2);
                    if (contact6 != null) {
                        contact6.setJobTitle(((Organization) arrayList11.get(0)).getJobTitle());
                    }
                    Contact contact7 = sparseArray.get(keyAt2);
                    if (contact7 != null) {
                        contact7.setJobPosition(((Organization) arrayList11.get(0)).getJobPosition());
                    }
                }
            }
        } catch (Exception e9) {
            e = e9;
            e.printStackTrace();
            return sparseArray;
        }
        return sparseArray;
    }



    public SparseArray<Contact> invoke3() {
        SparseArray<Contact> sparseArray = new SparseArray<>();
        try {
            // Collect contact sources
            ArrayList<ContactSource> sourceList = new ArrayList<>();
            Iterator<ContactSource> it = this.mContactDAO.contactSourceDAO().getAllAccounts().iterator();
            while (it.hasNext()) {
                ContactSource next = it.next();
                if (!Intrinsics.areEqual(next.getName(), "")) {
                    sourceList.add(next);
                }
            }
            ArrayList<String> accountList = new ArrayList<>();
            for (ContactSource contactSource : sourceList) {
                accountList.add(contactSource.getName());
            }
            this.accountList = accountList;
            int[] intArray = this.mContext.getResources().getIntArray(R.array.thumb_color);
            // Query contacts data
            String[] strArr2 = {
                    "vnd.android.cursor.item/organization", "vnd.android.cursor.item/name"
            };
            for (int i = 0; i < strArr2.length; i++) {
                String[] strArr3 = { strArr2[i] };
                if (isAllPermissionGranted(this.mContext)) {
                    Uri uri = ContactsContract.Data.CONTENT_URI;
                    try (Cursor cursor4 = this.mContext.getContentResolver().query(
                            uri, this.projection, "mimetype = ?", strArr3, this.sorting)) {
                        if (cursor4 != null) {
                            while (cursor4.moveToNext()) {
                                int colorIndex = colorPosition++;
                                if (colorPosition >= intArray.length) {
                                    colorPosition = 0;
                                }
                                int columnIndexStarred = cursor4.getColumnIndex("starred");
                                int columnIndexPhotoUri = cursor4.getColumnIndex("photo_uri");
                                int columnIndexPhotoThumbUri = cursor4.getColumnIndex("photo_thumb_uri");
                                int columnIndexData4 = cursor4.getColumnIndex("data4");
                                int columnIndexData2 = cursor4.getColumnIndex("data2");
                                int columnIndexData5 = cursor4.getColumnIndex("data5");
                                int columnIndexData3 = cursor4.getColumnIndex("data3");
                                int columnIndexData6 = cursor4.getColumnIndex("data6");
                                int columnIndexAccountName = cursor4.getColumnIndex("account_name");
                                String mimetype = cursor4.getString(cursor4.getColumnIndexOrThrow("mimetype"));
                                String data4 = cursor4.getString(columnIndexData4);
                                String data2 = cursor4.getString(columnIndexData2);
                                String data5 = cursor4.getString(columnIndexData5);
                                String data3 = cursor4.getString(columnIndexData3);
                                String data6 = cursor4.getString(columnIndexData6);
                                String photoUri = cursor4.getString(columnIndexPhotoUri);
                                String photoThumbUri = cursor4.getString(columnIndexPhotoThumbUri);
                                boolean starred = cursor4.getInt(columnIndexStarred) == 1;
                                if ("vnd.android.cursor.item/name".equals(mimetype)) {
                                    String accountName = cursor4.getString(columnIndexAccountName);
                                    int contactId = CursorExtenUtils.getIntValue(cursor4, "contact_id");
                                    int rawContactId = CursorExtenUtils.getIntValue(cursor4, "raw_contact_id");
                                    String contactName = (data2 != null ? data2 : "") +
                                            (data5 != null ? " " + data5 : "") +
                                            (data3 != null ? " " + data3 : "") +
                                            (data6 != null ? " " + data6 : "");
                                    Contact contact = new Contact(
                                            null, rawContactId, contactId, data4, data2, contactName,
                                            data3, data5, data6, photoUri, photoThumbUri, starred,
                                            null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                            accountName, new ArrayList<>(), "", "", "", mimetype,
                                            intArray[colorIndex], 1, null
                                    );
                                    sparseArray.put(rawContactId, contact);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // Fetch additional contact details
            SparseArray<ArrayList<PhoneNumber>> phoneNumbers = GetPhoneNumbersHelper.invoke$default(new GetPhoneNumbersHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < phoneNumbers.size(); i++) {
                int key = phoneNumbers.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    contact.setContactNumber((ArrayList<PhoneNumber>) phoneNumbers.valueAt(i));
                }
            }
            SparseArray<ArrayList<Email>> emails = GetEmailsHelper.invoke$default(new GetEmailsHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < emails.size(); i++) {
                int key = emails.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    contact.setContactEmail((ArrayList<Email>) emails.valueAt(i));
                }
            }
            SparseArray<ArrayList<Event>> events = GetEventsHelper.invoke$default(new GetEventsHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < events.size(); i++) {
                int key = events.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    contact.setContactEvent((ArrayList<Event>) events.valueAt(i));
                }
            }
            SparseArray<ArrayList<String>> websites = GetWebsitesHelper.invoke$default(new GetWebsitesHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < websites.size(); i++) {
                int key = websites.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    contact.setWebsites((ArrayList<String>) websites.valueAt(i));
                }
            }
            SparseArray<ArrayList<Organization>> organizations = GetOrganisationsHelper.invoke$default(new GetOrganisationsHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < organizations.size(); i++) {
                int key = organizations.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null && !organizations.valueAt(i).isEmpty()) {
                    Organization organization = organizations.valueAt(i).get(0);
                    contact.setCompany(organization.getCompany());
                    contact.setJobTitle(organization.getJobTitle());
                    contact.setJobPosition(organization.getJobPosition());
                }
            }
            SparseArray<ArrayList<String>> notes = GetNotesHelper.invoke$default(new GetNotesHelper(), mContext, null, this.accountList, 2, null);
            for (int i = 0; i < notes.size(); i++) {
                int key = notes.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    contact.setContactNotes((ArrayList<String>) notes.valueAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sparseArray;
    }





    public SparseArray<Contact> invoke() {
        SparseArray<Contact> sparseArray = new SparseArray<>();
        if (!isAllPermissionGranted(mContext)) {
            return sparseArray;
        }
        try {
            // Collect contact sources
            List<ContactSource> sourceList = new ArrayList<>();
            for (ContactSource contactSource : this.mContactDAO.contactSourceDAO().getAllAccounts()) {
                if (!Intrinsics.areEqual(contactSource.getName(), "")) {
                    sourceList.add(contactSource);
                }
            }
            this.accountList.clear();
            for (ContactSource contactSource : sourceList) {
                this.accountList.add(contactSource.getName());
            }
            int[] intArray = this.mContext.getResources().getIntArray(R.array.thumb_color);
            // Query contacts data
            Uri uri = ContactsContract.Data.CONTENT_URI;
            String[] mimetypes = {"vnd.android.cursor.item/organization", "vnd.android.cursor.item/name"};
            for (String mimetype : mimetypes) {
                try (Cursor cursor = mContext.getContentResolver().query(
                        uri, this.projection, "mimetype = ?", new String[]{mimetype}, this.sorting)) {
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            int colorIndex = colorPosition++;
                            if (colorPosition >= intArray.length) {
                                colorPosition = 0;
                            }
                            String data4 = cursor.getString(cursor.getColumnIndexOrThrow("data4"));
                            String data2 = cursor.getString(cursor.getColumnIndexOrThrow("data2"));
                            String data5 = cursor.getString(cursor.getColumnIndexOrThrow("data5"));
                            String data3 = cursor.getString(cursor.getColumnIndexOrThrow("data3"));
                            String data6 = cursor.getString(cursor.getColumnIndexOrThrow("data6"));
                            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow("photo_uri"));
                            String photoThumbUri = cursor.getString(cursor.getColumnIndexOrThrow("photo_thumb_uri"));
                            boolean starred = cursor.getInt(cursor.getColumnIndexOrThrow("starred")) == 1;
                            String accountName = cursor.getString(cursor.getColumnIndexOrThrow("account_name"));
                            int contactId = CursorExtenUtils.getIntValue(cursor, "contact_id");
                            int rawContactId = CursorExtenUtils.getIntValue(cursor, "raw_contact_id");
                            // Construct contact name
                            String contactName = (data2 != null ? data2 : "") +
                                    (data5 != null ? " " + data5 : "") +
                                    (data3 != null ? " " + data3 : "") +
                                    (data6 != null ? " " + data6 : "");
                            Contact contact = new Contact(
                                    null, rawContactId, contactId, data4, data2, contactName,
                                    data3, data5, data6, photoUri, photoThumbUri, starred,
                                    null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    accountName, new ArrayList<>(), "", "", "", mimetype,
                                    intArray[colorIndex], 1, null
                            );
                            sparseArray.put(rawContactId, contact);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Fetch additional contact details
            updateContactsWithAdditionalDetails(sparseArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sparseArray;
    }
    private void updateContactsWithAdditionalDetails(SparseArray<Contact> sparseArray) {
        // Example of parallel processing using AsyncTask or ExecutorService could be implemented here
        updateContactsWithHelper(GetPhoneNumbersHelper.class, sparseArray, "contactNumber");
        updateContactsWithHelper(GetEmailsHelper.class, sparseArray, "contactEmail");
        updateContactsWithHelper(GetEventsHelper.class, sparseArray, "contactEvent");
        updateContactsWithHelper(GetWebsitesHelper.class, sparseArray, "websites");
        updateContactsWithHelper(GetNotesHelper.class, sparseArray, "contactNotes");
        updateContactsWithHelper(GetOrganisationsHelper.class, sparseArray, "organization");
    }
    private <T> void updateContactsWithHelper(Class<?> helperClass, SparseArray<Contact> sparseArray, String methodName) {
        try {
            SparseArray<T> helperData = (SparseArray<T>) helperClass.getMethod("invoke", Context.class, Object.class, List.class, int.class)
                    .invoke(helperClass.newInstance(), mContext, null, accountList, 2);
            for (int i = 0; i < helperData.size(); i++) {
                int key = helperData.keyAt(i);
                Contact contact = sparseArray.get(key);
                if (contact != null) {
                    T value = helperData.valueAt(i);
                    if ("contactNumber".equals(methodName)) {
                        contact.setContactNumber((ArrayList<PhoneNumber>) value);
                    } else if ("contactEmail".equals(methodName)) {
                        contact.setContactEmail((ArrayList<Email>) value);
                    } else if ("contactEvent".equals(methodName)) {
                        contact.setContactEvent((ArrayList<Event>) value);
                    } else if ("websites".equals(methodName)) {
                        contact.setWebsites((ArrayList<String>) value);
                    } else if ("contactNotes".equals(methodName)) {
                        contact.setContactNotes((ArrayList<String>) value);
                    } else if ("organization".equals(methodName)) {
                        if (!((ArrayList<Organization>) value).isEmpty()) {
                            Organization organization = ((ArrayList<Organization>) value).get(0);
                            contact.setCompany(organization.getCompany());
                            contact.setJobTitle(organization.getJobTitle());
                            contact.setJobPosition(organization.getJobPosition());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
