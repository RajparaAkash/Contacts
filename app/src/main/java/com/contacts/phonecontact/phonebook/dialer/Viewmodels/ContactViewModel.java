package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.DeleteContactsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllAccountsNameHelper2;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadContactWithCursorLoader;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public class ContactViewModel extends ViewModel {

    private final MutableLiveData<List<ListObject>> stateOfContacts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> stateOfDeleteContact = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalContactInAccount = new MutableLiveData<>();
    private ArrayList<ContactSource> accounts = new ArrayList<>();
    private List<Contact> rawContacts = new ArrayList<>();

    public MutableLiveData<List<ListObject>> getStateOfContacts() {
        return stateOfContacts;
    }

    public MutableLiveData<Integer> getTotalContactInAccount() {
        return totalContactInAccount;
    }

    public MutableLiveData<Boolean> getStateOfDeleteContact() {
        return stateOfDeleteContact;
    }

    public ArrayList<ContactSource> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<ContactSource> accounts) {
        this.accounts = accounts;
    }

    public List<Contact> getRawContacts() {
        return rawContacts;
    }

    public void setRawContacts(List<Contact> rawContacts) {
        this.rawContacts = rawContacts;
    }

    public void deleteContact(Context context, List<Contact> list) {
        Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        ThreadExtensionUtils.executeAsyncTask(new Runnable() {
            @Override
            public void run() {
                booleanRef.element = new DeleteContactsHelper().invoke(context, list);
            }
        }, new Runnable() {
            @Override
            public void run() {
                getStateOfDeleteContact().setValue(booleanRef.element);
            }
        });
    }

    public List<ListObject> generateListFromMap(Map<String, List<Contact>> map) {
        List<ListObject> listObjects = new ArrayList<>();
        for (Map.Entry<String, List<Contact>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Contact> contacts = entry.getValue();

            if (contacts != null && !contacts.isEmpty()) {
                HeaderModel header = new HeaderModel(key, contacts.get(0));
                listObjects.add(header);
                listObjects.addAll(contacts);
            }
        }
        return listObjects;
    }

    public void loadContactMainThread(Context context, ContactDatabase contactDatabase) {
        LinkedHashMap<String, List<Contact>> linkedHashMap = new LinkedHashMap<>();
        Ref.ObjectRef<List<ListObject>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new ContactViewModelLoadContactMainThread2(this, context, contactDatabase, linkedHashMap, objectRef), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getStateOfContacts().setValue((List<ListObject>) objectRef.element);
                    }
                });
                return Unit.INSTANCE;
            }
        });
    }

    public void loadRawContact(Context context, ContactDatabase contactDatabase) {
        ThreadExtensionUtils.executeAsyncTask(new Runnable() {
            @Override
            public void run() {
                List<ContactSource> allAccounts = contactDatabase.contactSourceDAO().getAllAccounts();
                if (!allAccounts.isEmpty()) {
                    setAccounts((ArrayList<ContactSource>) allAccounts);
                }
                if (getAccounts().isEmpty()) {
                    ArrayList<ContactSource> arrayList = new ArrayList<>();
                    for (ContactSource t : new LoadAllAccountsNameHelper2().invoke(context, contactDatabase.contactSourceDAO())) {
                        if (!Intrinsics.areEqual(t.getName(), "")) {
                            arrayList.add(t);
                        }
                    }
                }
                if (allAccounts.isEmpty()) {
                    allAccounts = contactDatabase.contactSourceDAO().getAllAccounts();
                }
                if (!allAccounts.isEmpty()) {
                    setAccounts((ArrayList<ContactSource>) allAccounts);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<ContactSource> accounts = getAccounts();
                    ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(accounts, 10));
                    Iterator<ContactSource> it = accounts.iterator();
                    while (it.hasNext()) {
                        arrayList.add(it.next().getName());
                    }
                    List list = CollectionsKt.toList(arrayList);
                    setRawContacts(new LoadContactWithCursorLoader(context, (ArrayList) list).invoke());
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    for (Contact t : getRawContacts()) {
                        Integer valueOf = Integer.valueOf(t.getContactIdSimple());
                        Object obj = linkedHashMap.get(valueOf);
                        if (obj == null) {
                            obj = (List) new ArrayList();
                            linkedHashMap.put(valueOf, obj);
                        }
                        ((List) obj).add(t);
                    }
                    loadContactMainThread(context, contactDatabase);

                    ThreadExtensionUtils.executeAsyncTask(new Runnable() {
                        @Override
                        public void run() {
                            contactDatabase.contactDAO().deleteAllContact();
                            contactDatabase.contactDAO().addAllContacts(getRawContacts());
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
