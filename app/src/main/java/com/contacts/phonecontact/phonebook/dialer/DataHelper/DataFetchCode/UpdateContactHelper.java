package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ContactPhotoUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.TypeGetterUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import kotlin.jvm.internal.Intrinsics;

public class UpdateContactHelper {

    public boolean invoke(Context context, Contact contact, int i) {
        Exception e;
        boolean z;
        String str;
        String str2 = "data5";
        try {
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            ContentProviderOperation.Builder newUpdate = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
            try {
                String[] strArr = {String.valueOf(contact.getContactIdSimple())};
                String str3 = "vnd.android.cursor.item/website";
                Log.e("gergeragregh", String.valueOf(contact.getContactId()));
                newUpdate.withSelection("contact_id = ?", strArr);
                newUpdate.withValue("data4", contact.getNamePrefix());
                newUpdate.withValue("data2", contact.getFirstName());
                newUpdate.withValue(str2, contact.getMiddleName());
                newUpdate.withValue("data3", contact.getSurName());
                newUpdate.withValue("data6", contact.getNameSuffix());
                arrayList.add(newUpdate.build());
                ContentProviderOperation.Builder newDelete = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/nickname"});
                arrayList.add(newDelete.build());
                ContentProviderOperation.Builder newDelete2 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete2.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/phone_v2"});
                arrayList.add(newDelete2.build());
                Iterator<PhoneNumber> it = contact.getContactNumber().iterator();
                while (it.hasNext()) {
                    PhoneNumber next = it.next();
                    ContentProviderOperation.Builder newInsert = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                    newInsert.withValue("raw_contact_id", contact.getContactId());
                    newInsert.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
                    newInsert.withValue("data1", next.getValue());
                    newInsert.withValue("data4", next.getNormalizedNumber());
                    newInsert.withValue("data2", TypeGetterUtils.getOriginalType(next.getType()));
                    newInsert.withValue("data3", next.getLabel());
                    arrayList.add(newInsert.build());
                    it = it;
                    str2 = str2;
                }
                ContentProviderOperation.Builder newDelete3 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete3.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/email_v2"});
                arrayList.add(newDelete3.build());
                for (Email t : contact.getContactEmail()) {
                    ContentProviderOperation.Builder newInsert2 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                    newInsert2.withValue("raw_contact_id", contact.getContactId());
                    newInsert2.withValue("mimetype", "vnd.android.cursor.item/email_v2");
                    newInsert2.withValue("data1", t.getValue());
                    newInsert2.withValue("data2", TypeGetterUtils.getOriginalType(t.getType()));
                    newInsert2.withValue("data3", t.getLabel());
                    arrayList.add(newInsert2.build());
                }
                ContentProviderOperation.Builder newDelete4 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete4.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/postal-address_v2"});
                arrayList.add(newDelete4.build());
                ContentProviderOperation.Builder newDelete5 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete5.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/im"});
                arrayList.add(newDelete5.build());
                ContentProviderOperation.Builder newDelete6 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete6.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/contact_event"});
                arrayList.add(newDelete6.build());
                for (Event t2 : contact.getContactEvent()) {
                    ContentProviderOperation.Builder newInsert3 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                    newInsert3.withValue("raw_contact_id", contact.getContactId());
                    newInsert3.withValue("mimetype", "vnd.android.cursor.item/contact_event");
                    newInsert3.withValue("data1", t2.getValue());
                    newInsert3.withValue("data2", TypeGetterUtils.getOriginalType(t2.getType()));
                    arrayList.add(newInsert3.build());
                }
                ContentProviderOperation.Builder newDelete7 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete7.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/note"});
                arrayList.add(newDelete7.build());
                Iterator<String> it2 = contact.getContactNotes().iterator();
                while (it2.hasNext()) {
                    ContentProviderOperation.Builder newInsert4 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                    newInsert4.withValue("raw_contact_id", contact.getContactId());
                    newInsert4.withValue("mimetype", "vnd.android.cursor.item/note");
                    newInsert4.withValue("data1", it2.next());
                    arrayList.add(newInsert4.build());
                }
                ContentProviderOperation.Builder newDelete8 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete8.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), "vnd.android.cursor.item/organization"});
                arrayList.add(newDelete8.build());
                ContentProviderOperation.Builder newInsert5 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert5.withValue("raw_contact_id", contact.getContactId());
                newInsert5.withValue("mimetype", "vnd.android.cursor.item/organization");
                if (contact.getCompany().length() > 0) {
                    newInsert5.withValue("data1", contact.getCompany());
                    newInsert5.withValue("data2", 1);
                }
                if (contact.getJobPosition().length() > 0) {
                    newInsert5.withValue(str2, contact.getJobPosition());
                    newInsert5.withValue("data2", 1);
                }
                if (contact.getJobTitle().length() > 0) {
                    newInsert5.withValue("data4", contact.getJobTitle());
                    newInsert5.withValue("data2", 1);
                }
                arrayList.add(newInsert5.build());
                ContentProviderOperation.Builder newDelete9 = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
                newDelete9.withSelection("raw_contact_id = ? AND mimetype = ? ", new String[]{String.valueOf(contact.getContactId()), str3});
                arrayList.add(newDelete9.build());
                for (String t3 : contact.getWebsites()) {
                    if (!Intrinsics.areEqual(t3, "")) {
                        ContentProviderOperation.Builder newInsert6 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                        newInsert6.withValue("raw_contact_id", contact.getContactId());
                        str = str3;
                        newInsert6.withValue("mimetype", str);
                        newInsert6.withValue("data1", t3);
                        newInsert6.withValue("data2", 1);
                        arrayList.add(newInsert6.build());
                    } else {
                        str = str3;
                    }
                    str3 = str;
                }
                try {
                    Uri withAppendedPath = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact.getContactIdSimple()));
                    ContentValues contentValues = new ContentValues(2);
                    contentValues.put("starred", contact.getContactIsStared());
                    if (contact.getRingtone() != null) {
                        contentValues.put("custom_ringtone", contact.getRingtone());
                    }
                    context.getContentResolver().update(withAppendedPath, contentValues, null, null);
                } catch (Exception e2) {
                    System.out.println((Object) ("contact creation : inner --> " + e2.getMessage()));
                }
                if (i == 0) {
                    z = true;
                    addPhoto(context, contact, arrayList);
                } else if (i != 1) {
                    z = true;
                } else {
                    z = true;
                    removePhoto(contact, arrayList);
                }
                context.getContentResolver().applyBatch("com.android.contacts", arrayList);
                return z;
            } catch (Exception e3) {
                e = e3;
                System.out.println((Object) ("contact creation : last --> " + e.getMessage()));
                return false;
            }
        } catch (Exception e4) {
            e = e4;
            System.out.println((Object) ("contact creation : last --> " + e.getMessage()));
            return false;
        }
    }

    private ArrayList<ContentProviderOperation> addPhoto(final Context context, final Contact contact, final ArrayList<ContentProviderOperation> list) {
        final String contactPhotoUri = contact.getContactPhotoUri();
        final int n = 1;
        int n2;
        if (contactPhotoUri != null && contactPhotoUri.length() > 0) {
            n2 = n;
        } else {
            n2 = 0;
        }
        if (n2 != 0) {
            final Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(contact.getContactPhotoUri()));
                final int photoThumbnailSize = ContactPhotoUtils.getPhotoThumbnailSize(context);
                final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, photoThumbnailSize, photoThumbnailSize, false);
                final byte[] byteArray = ContactPhotoUtils.getByteArray(scaledBitmap);
                scaledBitmap.recycle();
                final byte[] byteArray2 = ContactPhotoUtils.getByteArray(bitmap);
                bitmap.recycle();
                final ContentProviderOperation.Builder insert = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                insert.withValue("raw_contact_id", (Object) contact.getContactIdSimple());
                insert.withValue("mimetype", (Object) "vnd.android.cursor.item/photo");
                insert.withValue("data15", (Object) byteArray);
                list.add(insert.build());
                this.addFullSizePhoto(context, contact.getContactId(), byteArray2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private ArrayList<ContentProviderOperation> removePhoto(Contact contact, ArrayList<ContentProviderOperation> arrayList) {
        ContentProviderOperation.Builder newDelete = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
        newDelete.withSelection("contact_id = ? AND mimetype = ?", new String[]{String.valueOf(contact.getContactIdSimple()), "vnd.android.cursor.item/photo"});
        arrayList.add(newDelete.build());
        return arrayList;
    }

    private void addFullSizePhoto(Context context, long j, byte[] bArr) {
        Uri withAppendedId = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, j);
        AssetFileDescriptor openAssetFileDescriptor = null;
        try {
            openAssetFileDescriptor = context.getContentResolver().openAssetFileDescriptor(Uri.withAppendedPath(withAppendedId, "display_photo"), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream createOutputStream = null;
        try {
            createOutputStream = openAssetFileDescriptor.createOutputStream();
            createOutputStream.write(bArr);
            createOutputStream.close();
            openAssetFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
