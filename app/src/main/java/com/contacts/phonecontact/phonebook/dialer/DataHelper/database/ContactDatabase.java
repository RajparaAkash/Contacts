package com.contacts.phonecontact.phonebook.dialer.DataHelper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.CallLogDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactSourcesDAO;

import kotlin.jvm.internal.DefaultConstructorMarker;

@Database(entities = {CallLogModel.class, Contact.class, ContactSource.class}, version = 3, exportSchema = false)
@TypeConverters(MyConverters.class)
public abstract class ContactDatabase extends RoomDatabase {
    public static final Companion Companion = new Companion(null);
    private static final Object LOCK = new Object();
    private static volatile ContactDatabase instance;

    public abstract CallLogDAO callLogDAO();

    public abstract ContactDAO contactDAO();

    public abstract ContactSourcesDAO contactSourceDAO();

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public ContactDatabase invoke(Context context) {
            ContactDatabase contactDatabase;
            ContactDatabase contactDatabase2 = ContactDatabase.instance;
            if (contactDatabase2 != null) {
                return contactDatabase2;
            }
            synchronized (ContactDatabase.LOCK) {
                ContactDatabase contactDatabase3 = ContactDatabase.instance;
                if (contactDatabase3 == null) {
                    contactDatabase = ContactDatabase.Companion.buildDatabase(context);
                    Companion companion = ContactDatabase.Companion;
                    ContactDatabase.instance = contactDatabase;
                } else {
                    contactDatabase = contactDatabase3;
                }
            }
            return contactDatabase;
        }

        private ContactDatabase buildDatabase(Context context) {
            return (ContactDatabase) Room.databaseBuilder(context, ContactDatabase.class, "ContactDb.db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
    }
}
