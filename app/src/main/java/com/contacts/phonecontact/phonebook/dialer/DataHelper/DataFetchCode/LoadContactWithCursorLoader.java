package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.SparseArray;

import androidx.loader.content.CursorLoader;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Organization;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetEmailsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetEventsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetNotesHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetOrganisationsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetPhoneNumbersHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetWebsitesHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.CursorExtenUtils;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.PreferencesManager;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

public class LoadContactWithCursorLoader {

    private final ArrayList<String> accountList;
    private final Context mContext;
    private final String sorting = "data2 ASC";
    private int colorPosition;
    private String[] projection = {"mimetype", "contact_id", "raw_contact_id", "data4", "data2", "data5", "data3", "data6", "photo_uri", "photo_thumb_uri", "starred", "custom_ringtone", "account_name", "account_type", "mimetype"};

    public LoadContactWithCursorLoader(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.accountList = arrayList;
    }

    private boolean isAllPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PreferencesManager.hasPermission(context, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"});
        }
        return true;
    }


    public ArrayList<Contact> invoke() {
        String str = null;
        int i = 0;
        String[] strArr = new String[0];
        Cursor cursor2 = null;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7 = null;
        SparseArray sparseArray = new SparseArray();
        ArrayList<Contact> arrayList = new ArrayList<>();
        int[] intArray = mContext.getResources().getIntArray(R.array.thumb_color);
        String str8 = "vnd.android.cursor.item/name";
        String[] strArr2 = {"vnd.android.cursor.item/organization", str8};
        int i2 = 0;
        int i3 = 0;
        while (i3 < 2) {
            String str9 = strArr2[i3];
            int i4 = 1;
            if (isAllPermissionGranted(mContext)) {
                String[] strArr3 = new String[1];
                strArr3[i2] = str9;
                Uri uri = ContactsContract.Data.CONTENT_URI;
                Cursor loadInBackground = new CursorLoader(mContext, uri, projection, "mimetype = ?", strArr3, sorting).loadInBackground();
                if (loadInBackground != null) {
                    Cursor cursor3 = loadInBackground;
                    try {
                        Cursor cursor4 = cursor3;
                        try {
                            if (cursor4.moveToFirst()) {
                                while (true) {
                                    int i5 = colorPosition + i4;
                                    colorPosition = i5;
                                    if (i5 >= intArray.length) {
                                        colorPosition = i2;
                                    }
                                    cursor4.getColumnIndex("raw_contact_id");
                                    String ringtone = null;
                                    try {
                                        ringtone = cursor4.getString(cursor4.getColumnIndexOrThrow("custom_ringtone"));
                                    } catch (IllegalArgumentException ex) {
                                        ringtone = null;
                                    }
                                    int columnIndex = cursor4.getColumnIndex("starred");
                                    int columnIndex2 = cursor4.getColumnIndex(MyContactsContentProvider.COL_PHOTO_URI);
                                    int columnIndex3 = cursor4.getColumnIndex("photo_thumb_uri");
                                    int columnIndex4 = cursor4.getColumnIndex("data4");
                                    int columnIndex5 = cursor4.getColumnIndex("data2");
                                    int columnIndex6 = cursor4.getColumnIndex("data5");
                                    int columnIndex7 = cursor4.getColumnIndex("data3");
                                    strArr = strArr2;
                                    int columnIndex8 = cursor4.getColumnIndex("data6");
                                    i = i3;
                                    String string = cursor4.getString(cursor4.getColumnIndexOrThrow("mimetype"));
                                    if (Intrinsics.areEqual(string, str8)) {
                                        str2 = cursor4.getString(columnIndex4);
                                        if (str2 == null) {
                                            str = str8;
                                            str2 = "";
                                        } else {
                                            str = str8;
                                            Intrinsics.checkNotNullExpressionValue(str2, "it.getString(indexPrefix) ?: \"\"");
                                        }
                                        str5 = cursor4.getString(columnIndex5);
                                        if (str5 == null) {
                                            str5 = "";
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str5, "it.getString(fNameIndex) ?: \"\"");
                                        }
                                        str4 = cursor4.getString(columnIndex6);
                                        if (str4 == null) {
                                            str4 = "";
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str4, "it.getString(mNameIndex) ?: \"\"");
                                        }
                                        str3 = cursor4.getString(columnIndex7);
                                        if (str3 == null) {
                                            str3 = "";
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str3, "it.getString(sNameIndex) ?: \"\"");
                                        }
                                        str6 = cursor4.getString(columnIndex8);
                                        if (str6 == null) {
                                            str6 = "";
                                        } else {
                                            Intrinsics.checkNotNullExpressionValue(str6, "it.getString(suffixIndex) ?: \"\"");
                                        }
                                    } else {
                                        str = str8;
                                        str6 = "";
                                        str5 = str6;
                                        str4 = str5;
                                        str3 = str4;
                                        str2 = str3;
                                    }
                                    String string2 = cursor4.getString(cursor4.getColumnIndexOrThrow("account_name"));
                                    if (string2 == null) {
                                        cursor2 = cursor3;
                                        str7 = "";
                                    } else {
                                        cursor2 = cursor3;
                                        try {
                                            Intrinsics.checkNotNullExpressionValue(string2, "it.getString(accountNameIndex) ?: \"\"");
                                            str7 = string2;
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        } catch (Throwable th5) {
                                        }
                                    }
                                    int intValue = CursorExtenUtils.getIntValue(cursor4, "contact_id");
                                    int intValue2 = CursorExtenUtils.getIntValue(cursor4, "raw_contact_id");
                                    String obj = StringsKt.trim((CharSequence) str2).toString();
                                    String obj2 = StringsKt.trim((CharSequence) str5).toString();
                                    String obj3 = StringsKt.trim((CharSequence) StringsKt.trim((CharSequence) (str5 + ' ' + str3)).toString()).toString();
                                    String obj4 = StringsKt.trim((CharSequence) str4).toString();
                                    String obj5 = StringsKt.trim((CharSequence) str3).toString();
                                    String obj6 = StringsKt.trim((CharSequence) str6).toString();
                                    ArrayList arrayList4 = new ArrayList();
                                    ArrayList arrayList5 = new ArrayList();
                                    ArrayList arrayList6 = new ArrayList();
                                    ArrayList arrayList7 = new ArrayList();
                                    ArrayList arrayList8 = new ArrayList();
                                    ArrayList arrayList9 = new ArrayList();
                                    String string3 = cursor4.getString(columnIndex2);
                                    String str10 = string3 == null ? null : string3;
                                    String string4 = cursor4.getString(columnIndex3);
                                    sparseArray.put(CursorExtenUtils.getIntValue(cursor4, "raw_contact_id"), new Contact(null, intValue2, intValue, obj, obj2, obj3, obj4, obj5, obj6, str10, string4 == null ? null : string4, cursor4.getInt(columnIndex) == 1, ringtone, arrayList4, arrayList5, arrayList7, arrayList6, arrayList8, arrayList9, str7, new ArrayList(), "", "", "", string, Integer.valueOf(intArray[this.colorPosition]), 1, null));
                                    if (!cursor4.moveToNext()) {
                                        break;
                                    }
                                    strArr2 = strArr;
                                    i3 = i;
                                    str8 = str;
                                    cursor3 = cursor2;
                                    i2 = 0;
                                    i4 = 1;
                                }
                            } else {
                                strArr = strArr2;
                                str = str8;
                                i = i3;
                                cursor2 = cursor3;
                            }
                        } catch (Exception e3) {
                            cursor2 = cursor3;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            if (sparseArray.size() == 0) {
                            }
                            arrayList = new ArrayList<>(sparseArray.size());
                            i3 = i + 1;
                            strArr2 = strArr;
                            str8 = str;
                            i2 = 0;
                        } catch (Throwable th6) {
                        }
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        i3 = i + 1;
                        strArr2 = strArr;
                        str8 = str;
                        i2 = 0;
                    } catch (Throwable th7) {
//                        throw th7;
                    }
                }
            }
        }

        if (sparseArray.size() > 0) {
            SparseArray invoke$default = GetPhoneNumbersHelper.invoke$default(new GetPhoneNumbersHelper(), mContext, null, accountList, 2, null);
            SparseArray invoke$default2 = GetEmailsHelper.invoke$default(new GetEmailsHelper(), mContext, null, accountList, 2, null);
            SparseArray invoke$default3 = GetEventsHelper.invoke$default(new GetEventsHelper(), mContext, null, accountList, 2, null);
            SparseArray invoke$default4 = GetWebsitesHelper.invoke$default(new GetWebsitesHelper(), mContext, null, accountList, 2, null);
            SparseArray invoke$default5 = GetOrganisationsHelper.invoke$default(new GetOrganisationsHelper(), mContext, null, accountList, 2, null);
            SparseArray invoke$default6 = GetNotesHelper.invoke$default(new GetNotesHelper(), mContext, null, accountList, 2, null);
            int size = invoke$default.size();
            for (int i6 = 0; i6 < size; i6++) {
                int keyAt = invoke$default.keyAt(i6);
                if (sparseArray.get(keyAt) != null) {
                    ArrayList<PhoneNumber> arrayList10 = (ArrayList) invoke$default.valueAt(i6);
                    ((Contact) sparseArray.get(keyAt)).setContactNumber(arrayList10);
                }
            }
            int size2 = invoke$default2.size();
            for (int i7 = 0; i7 < size2; i7++) {
                int keyAt2 = invoke$default2.keyAt(i7);
                if (sparseArray.size() != 0) {
                    try {
                        Object obj7 = sparseArray.get(keyAt2);
                        Intrinsics.checkNotNull(obj7);
                        Object valueAt = invoke$default2.valueAt(i7);
                        ((Contact) obj7).setContactEmail((ArrayList) valueAt);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            }
            int size3 = invoke$default3.size();
            for (int i8 = 0; i8 < size3; i8++) {
                Contact contact = (Contact) sparseArray.get(invoke$default3.keyAt(i8));
                if (contact != null) {
                    Object valueAt2 = invoke$default3.valueAt(i8);
                    contact.setContactEvent((ArrayList) valueAt2);
                }
            }
            int size4 = invoke$default4.size();
            for (int i9 = 0; i9 < size4; i9++) {
                Contact contact2 = (Contact) sparseArray.get(invoke$default4.keyAt(i9));
                if (contact2 != null) {
                    Object valueAt3 = invoke$default4.valueAt(i9);
                    contact2.setWebsites((ArrayList) valueAt3);
                }
            }
            int size5 = invoke$default6.size();
            for (int i10 = 0; i10 < size5; i10++) {
                Contact contact3 = (Contact) sparseArray.get(invoke$default6.keyAt(i10));
                if (contact3 != null) {
                    Object valueAt4 = invoke$default6.valueAt(i10);
                    contact3.setContactNotes((ArrayList) valueAt4);
                }
            }
            int size6 = invoke$default5.size();
            for (int i11 = 0; i11 < size6; i11++) {
                int keyAt3 = invoke$default5.keyAt(i11);
                ArrayList arrayList11 = (ArrayList) invoke$default5.valueAt(i11);
                if (size6 > 0) {
                    Contact contact4 = (Contact) sparseArray.get(keyAt3);
                    if (contact4 != null) {
                        contact4.setCompany(((Organization) arrayList11.get(0)).getCompany());
                    }
                    Contact contact5 = (Contact) sparseArray.get(keyAt3);
                    if (contact5 != null) {
                        contact5.setJobTitle(((Organization) arrayList11.get(0)).getJobTitle());
                    }
                    Contact contact6 = (Contact) sparseArray.get(keyAt3);
                    if (contact6 != null) {
                        contact6.setJobPosition(((Organization) arrayList11.get(0)).getJobPosition());
                    }
                }
            }
        }
        arrayList = new ArrayList<>(sparseArray.size());
        ArrayList<Number> arrayList222 = new ArrayList();
        for (Number obj8 : RangesKt.until(0, sparseArray.size())) {
            arrayList222.add((Number) obj8);
        }
        ArrayList<Contact> arrayList322 = arrayList;
        for (Number number : arrayList222) {
            arrayList.add((Contact) sparseArray.valueAt(number.intValue()));
        }
        return arrayList;
    }

}
