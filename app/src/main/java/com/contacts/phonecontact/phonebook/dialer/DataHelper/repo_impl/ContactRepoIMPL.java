package com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.dao.ContactDAO;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo.ContactRepo;

import java.util.List;

public class ContactRepoIMPL implements ContactRepo {
    private final ContactDAO mContactDAO;

    public ContactRepoIMPL(ContactDAO contactDAO) {
        this.mContactDAO = contactDAO;
    }

    @Override
    public void addAllContacts(List<Contact> list) {
    }

    @Override
    public List<Contact> getAllContacts() {
        return this.mContactDAO.getAllContacts();
    }

    @Override
    public long addContact(Contact contact) {
        return this.mContactDAO.addContact(contact);
    }

    @Override
    public void deleteAllContact() {
        this.mContactDAO.deleteAllContact();
    }

    @Override
    public void deleteContact(Contact contact) {
        this.mContactDAO.deleteContact(contact);
    }

    @Override
    public void refreshAccounts(Contact contact) {
        this.mContactDAO.refreshAccounts(contact);
    }

    @Override
    public Contact getContactWithId(String str) {
        return this.mContactDAO.getContactWithId(str);
    }

    @Override
    public List<Contact> getContactWithSimpleId(String str) {
        return this.mContactDAO.getContactWithSimpleId(str);
    }

}
