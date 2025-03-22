package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Address;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.Utils.TypeGetterUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import kotlin.io.ByteStreamsKt;
import kotlin.jvm.internal.Intrinsics;

public class CreateNewContactHelper {
    private final Contact contact;
    private final Context context;
    private final ContactDatabase mDatabase;

    public CreateNewContactHelper(Contact contact2, Context context2, ContactDatabase contactDatabase) {
        this.contact = contact2;
        this.context = context2;
        this.mDatabase = contactDatabase;
    }

    public int invoke() {
        ContactSource t;
        String str;
        byte[] bArr;
        try {
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            ContentProviderOperation.Builder newInsert = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
            newInsert.withValue("account_name", contact.getContactSource());
            Iterator<ContactSource> it = new LoadAllAccountsHelper().invoke(mDatabase).iterator();
            while (true) {
                if (!it.hasNext()) {
                    t = null;
                    break;
                }
                t = it.next();
                if (Intrinsics.areEqual(t.getName(), contact.getContactSource())) {
                    break;
                }
            }
            ContactSource t2 = t;
            if (t2 == null || (str = t2.getType()) == null) {
                str = "";
            }
            newInsert.withValue("account_type", str);
            arrayList.add(newInsert.build());
            ContentProviderOperation.Builder newInsert2 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
            boolean z = false;
            newInsert2.withValueBackReference("raw_contact_id", 0);
            newInsert2.withValue("mimetype", "vnd.android.cursor.item/name");
            newInsert2.withValue("data4", contact.getNamePrefix());
            newInsert2.withValue("data2", contact.getFirstName());
            newInsert2.withValue("data5", contact.getMiddleName());
            newInsert2.withValue("data3", contact.getSurName());
            newInsert2.withValue("data6", contact.getNameSuffix());
            arrayList.add(newInsert2.build());
            for (PhoneNumber t3 : contact.getContactNumber()) {
                if (!Intrinsics.areEqual(t3.getValue(), "")) {
                    ContentProviderOperation.Builder newInsert3 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                    newInsert3.withValueBackReference("raw_contact_id", 0);
                    newInsert3.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
                    newInsert3.withValue("data1", t3.getValue());
                    newInsert3.withValue("data4", t3.getValue());
                    newInsert3.withValue("data2", TypeGetterUtils.getOriginalType(t3.getType()));
                    newInsert3.withValue("data3", t3.getLabel());
                    arrayList.add(newInsert3.build());
                }
            }
            for (Email t4 : contact.getContactEmail()) {
                ContentProviderOperation.Builder newInsert4 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert4.withValueBackReference("raw_contact_id", 0);
                newInsert4.withValue("mimetype", "vnd.android.cursor.item/email_v2");
                newInsert4.withValue("data1", t4.getValue());
                newInsert4.withValue("data2", TypeGetterUtils.getOriginalType(t4.getType()));
                newInsert4.withValue("data3", t4.getLabel());
                arrayList.add(newInsert4.build());
            }
            for (Address t5 : contact.getContactAddresses()) {
                ContentProviderOperation.Builder newInsert5 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert5.withValueBackReference("raw_contact_id", 0);
                newInsert5.withValue("mimetype", "vnd.android.cursor.item/postal-address_v2");
                newInsert5.withValue("data1", t5.getValue());
                newInsert5.withValue("data2", 1);
                newInsert5.withValue("data3", t5.getLabel());
                arrayList.add(newInsert5.build());
            }
            for (Event t6 : contact.getContactEvent()) {
                ContentProviderOperation.Builder newInsert6 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert6.withValueBackReference("raw_contact_id", 0);
                newInsert6.withValue("mimetype", "vnd.android.cursor.item/contact_event");
                newInsert6.withValue("data1", t6.getValue());
                newInsert6.withValue("data2", TypeGetterUtils.getOriginalType(t6.getType()));
                arrayList.add(newInsert6.build());
            }
            Iterator<String> it2 = contact.getContactNotes().iterator();
            while (it2.hasNext()) {
                ContentProviderOperation.Builder newInsert7 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert7.withValueBackReference("raw_contact_id", 0);
                newInsert7.withValue("mimetype", "vnd.android.cursor.item/note");
                newInsert7.withValue("data1", it2.next());
                arrayList.add(newInsert7.build());
            }
            if (contact.getCompany().length() > 0) {
                ContentProviderOperation.Builder newInsert8 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert8.withValueBackReference("raw_contact_id", 0);
                newInsert8.withValue("mimetype", "vnd.android.cursor.item/organization");
                newInsert8.withValue("data1", contact.getCompany());
                newInsert8.withValue("data5", contact.getJobPosition());
                newInsert8.withValue("data4", contact.getJobTitle());
                arrayList.add(newInsert8.build());
            }
            Iterator<String> it3 = contact.getWebsites().iterator();
            while (it3.hasNext()) {
                ContentProviderOperation.Builder newInsert9 = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
                newInsert9.withValueBackReference("raw_contact_id", 0);
                newInsert9.withValue("mimetype", "vnd.android.cursor.item/website");
                newInsert9.withValue("data1", it3.next());
                newInsert9.withValue("data2", 1);
                arrayList.add(newInsert9.build());
            }
            ContentProviderResult[] applyBatch = context.getContentResolver().applyBatch("com.android.contacts", arrayList);
            Uri uri = applyBatch[0].uri;
            String lastPathSegment = uri != null ? uri.getLastPathSegment() : null;
            StringBuilder sb = new StringBuilder();
            sb.append("contact creation result : ");
            Uri uri2 = applyBatch[0].uri;
            sb.append(uri2 != null ? uri2.getLastPathSegment() : null);
            System.out.println((Object) sb.toString());
            String contactPhotoUri = contact.getContactPhotoUri();
            if (!(contactPhotoUri == null || contactPhotoUri.length() == 0)) {
                InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(contact.getContactPhotoUri()));
                bArr = openInputStream != null ? ByteStreamsKt.readBytes(openInputStream) : null;
            } else {
                bArr = null;
            }
            Uri uri3 = applyBatch[0].uri;
            Intrinsics.checkNotNull(uri3);
            long parseId = ContentUris.parseId(uri3);
            String contactPhotoUri2 = contact.getContactPhotoUri();
            if (contactPhotoUri2 == null || contactPhotoUri2.length() == 0) {
                z = true;
            }
            if (!z && bArr != null) {
                addFullSizePhoto(parseId, bArr);
            }
            if (Intrinsics.areEqual(lastPathSegment, "") || lastPathSegment == null) {
                return -1;
            }
            return Integer.parseInt(lastPathSegment);
        } catch (Exception e) {
            System.out.println((Object) ("contact creation : " + e.getMessage()));
            return -1;
        }
    }

    private void addFullSizePhoto(long j, byte[] bArr) {
        Uri withAppendedId = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, j);
        AssetFileDescriptor openAssetFileDescriptor = null;
        try {
            openAssetFileDescriptor = context.getContentResolver().openAssetFileDescriptor(Uri.withAppendedPath(withAppendedId, "display_photo"), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream createOutputStream = openAssetFileDescriptor.createOutputStream();
            createOutputStream.write(bArr);
            createOutputStream.close();
            openAssetFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
