package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllAccountsNameHelper2;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadAllAccountsNameHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadContactWithCursorLoader;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public class DialerViewModel extends ViewModel {
    private final MutableLiveData<List<ListObject>> stateOfContacts = new MutableLiveData<>();
    private final MutableLiveData<List<Contact>> stateOhHistory = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalContactInAccount = new MutableLiveData<>();
    private ArrayList<ContactSource> accounts = new ArrayList<>();
    private List<Contact> rawContacts = CollectionsKt.emptyList();

    public MutableLiveData<List<ListObject>> getStateOfContacts() {
        return this.stateOfContacts;
    }

    public MutableLiveData<List<Contact>> getStateOhHistory() {
        return this.stateOhHistory;
    }

    public ArrayList<ContactSource> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(ArrayList<ContactSource> arrayList) {
        this.accounts = arrayList;
    }

    public List<Contact> getRawContacts() {
        return this.rawContacts;
    }

    public void setRawContacts(List<Contact> list) {
        this.rawContacts = list;
    }

    public MutableLiveData<Integer> getTotalContactInAccount() {
        return this.totalContactInAccount;
    }

    public void allHistoryWithKey(ContactDatabase contactDatabase) {
        ArrayList<Contact> arrayList = new ArrayList<>();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new DialerViewModelAllHistoryWithKey2(contactDatabase, this, arrayList), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOhHistory().setValue(arrayList);
                return Unit.INSTANCE;
            }
        });
    }

    public void loadAllContacts(Context context, ContactDatabase contactDatabase) {
        ArrayList<Contact> arrayList = new ArrayList<>();
        CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new DialerViewModelLoadAllContacts2(context, contactDatabase, arrayList, new LinkedHashMap()), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfContacts().setValue(Collections.unmodifiableList(arrayList));
                return Unit.INSTANCE;
            }
        });
    }

    public void loadRawContact(Context context, ContactDatabase contactDatabase) {
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                List<ContactSource> allAccounts = contactDatabase.contactSourceDAO().getAllAccounts();
                if (!allAccounts.isEmpty()) {
                    setAccounts((ArrayList<ContactSource>) allAccounts);
                }
                if (getAccounts().isEmpty()) {
                    ArrayList<ContactSource> arrayList = new ArrayList<>();
//                    for (ContactSource t : new LoadAllAccountsNameHelper().invoke(context, contactDatabase.contactSourceDAO())) {
//                        if (!Intrinsics.areEqual(t.getName(), "")) {
//                            arrayList.add(t);
//                        }
//                    }
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
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                try {
                    ArrayList<ContactSource> accounts = getAccounts();
                    ArrayList<String> arrayList = new ArrayList<>();
                    Iterator<ContactSource> it = accounts.iterator();
                    while (it.hasNext()) {
                        arrayList.add(it.next().getName());
                    }
                    List<String> list = CollectionsKt.toList(arrayList);
                    setRawContacts(new LoadContactWithCursorLoader(context, (ArrayList<String>) list).invoke());
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    for (Contact t : getRawContacts()) {
                        Integer valueOf = t.getContactIdSimple();
                        Object obj = linkedHashMap.get(valueOf);
                        if (obj == null) {
                            obj = (List) new ArrayList();
                            linkedHashMap.put(valueOf, obj);
                        }
                        ((List) obj).add(t);
                    }
                    Log.e("fatal", "TotalListSize: " + String.valueOf(linkedHashMap.size()));
                    loadContactMainThread(context, contactDatabase);
                    ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(DialerViewModel.this), new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return Unit.INSTANCE;
                        }
                    }, new Function0<Unit>() {

                        @Override
                        public Unit invoke() {
                            contactDatabase.contactDAO().deleteAllContact();
                            contactDatabase.contactDAO().addAllContacts(getRawContacts());
                            return Unit.INSTANCE;
                        }
                    }, new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return Unit.INSTANCE;
                        }
                    });
                    Log.e("fatal", "getRawContacts: " + String.valueOf(getRawContacts().size()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            }
        });
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
        }, new DialerViewModelLoadContactMainThread2(this, context, contactDatabase, objectRef, linkedHashMap), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfContacts().postValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public List<ListObject> generateListFromMap(Map<String, ? extends List<Contact>> map) {
        ArrayList<ListObject> arrayList = new ArrayList<>();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            Intrinsics.checkNotNull(obj);
            arrayList.addAll((List) obj);
        }
        return arrayList;
    }

    public Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "", null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", null, -16776961, 16777217, null);
    }

}
