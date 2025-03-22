package com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;

import java.util.List;

@Keep
@Dao
public interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllContacts(List<Contact> list);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addContact(Contact contact);

    @Query("DELETE FROM Contact")
    void deleteAllContact();

    @Delete
    void deleteContact(Contact contact);

    @Query("SELECT * FROM Contact")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM Contact WHERE contactId = :str")
    Contact getContactWithId(String str);

    @Query("SELECT * FROM Contact WHERE contactIdSimple = :str")
    List<Contact> getContactWithSimpleId(String str);

    void refreshAccounts(Contact contact);

    public static final class DefaultImpls {
        public static void refreshAccounts(ContactDAO contactDAO, Contact contact) {
            contactDAO.deleteContact(contact);
            contactDAO.addContact(contact);
        }
    }
}
