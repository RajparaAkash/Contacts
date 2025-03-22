package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;
import android.util.SparseArray;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.repo_impl.ContactRepoIMPL;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.BlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ChangeFavoriteHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.DeleteContactsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadContactByIdHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadContactByLookUpHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.LoadContactBySimpleIdHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.UnBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.ContactDataFetch.GetPhoneNumbersHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Ref;

public class ContactInfoViewModel extends ViewModel {
    private final MutableLiveData<List<BlockContact>> stateOfBlockNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> stateOfDeleteContact = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<PhoneNumber>> stateOfPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> stateOfStarred = new MutableLiveData<>();
    private final MutableLiveData<List<String>> viewNumberSorcs = new MutableLiveData<>();
    private MutableLiveData<Contact> stateOfContactById = new MutableLiveData<>();

    public MutableLiveData<Boolean> getStateOfStarred() {
        return this.stateOfStarred;
    }

    public MutableLiveData<ArrayList<PhoneNumber>> getStateOfPhoneNumber() {
        return this.stateOfPhoneNumber;
    }

    public MutableLiveData<Contact> getStateOfContactById() {
        return this.stateOfContactById;
    }

    public void setStateOfContactById(MutableLiveData<Contact> mutableLiveData) {
        this.stateOfContactById = mutableLiveData;
    }

    public MutableLiveData<Boolean> getStateOfDeleteContact() {
        return this.stateOfDeleteContact;
    }

    public MutableLiveData<List<BlockContact>> getStateOfBlockNumber() {
        return this.stateOfBlockNumber;
    }

    public MutableLiveData<List<String>> getViewNumberSorcs() {
        return this.viewNumberSorcs;
    }

    public void updateStarred(Context context, List<Contact> list, int i) {
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                new ChangeFavoriteHelper().invoke(context, list, i);
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfStarred().setValue(true);
                return Unit.INSTANCE;
            }
        });
    }

    public void refreshPhoneNumber(Context context, int i) {
        Ref.ObjectRef<ArrayList<PhoneNumber>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = new ArrayList<>();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                SparseArray<ArrayList<PhoneNumber>> invoke$default = GetPhoneNumbersHelper.invoke$default(new GetPhoneNumbersHelper(), context, i, null, 4, null);
                if (invoke$default != null) {
                    if (invoke$default.size() != 0) {
                        ArrayList<PhoneNumber> t = invoke$default.get(i);
                        objectRef.element = t;
                        System.out.println((Object) ("" + invoke$default));
                    }
                }
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfPhoneNumber().setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public void getAllBlockNumber(Context context) {
        Ref.ObjectRef<List<BlockContact>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                objectRef.element = new GetBlockContactHelper(context).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfBlockNumber().setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public void blockThisNumber(Context context, String str) {
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                new BlockContactHelper(context, str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });
    }

    public void unBlockThisNumber(Context context, String str) {
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                new UnBlockContactHelper(context, str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        });
    }

    public void getContactByLookupKey(Context context, ContactDatabase contactDatabase, String str) {
        Ref.ObjectRef<Contact> objectRef = new Ref.ObjectRef<>();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                objectRef.element = new LoadContactByLookUpHelper(context, new ContactRepoIMPL(contactDatabase.contactDAO()), str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfContactById().setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public void getContactById(ContactDatabase contactDatabase, String str) {
        Ref.ObjectRef<Contact> objectRef = new Ref.ObjectRef<>();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                objectRef.element = new LoadContactByIdHelper(new ContactRepoIMPL(contactDatabase.contactDAO()), str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfContactById().setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public void getsourcenumber(ContactDatabase contactDatabase, String str) {
        Ref.ObjectRef<List<Contact>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                objectRef.element = new LoadContactBySimpleIdHelper(new ContactRepoIMPL(contactDatabase.contactDAO()), str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                ArrayList<String> arrayList = new ArrayList<>();
                for (Contact contact : objectRef.element) {
                    arrayList.addAll(CollectionsKt.listOf(contact.getContactSource()));
                }
                getViewNumberSorcs().setValue(arrayList);
                return Unit.INSTANCE;
            }
        });
    }

    public void getContactBySimpleId(ContactDatabase contactDatabase, String str) {
        Ref.ObjectRef<List<Contact>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                objectRef.element = new LoadContactBySimpleIdHelper(new ContactRepoIMPL(contactDatabase.contactDAO()), str).invoke();
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                ArrayList<PhoneNumber> arrayList = new ArrayList<>();
                for (Contact contact : objectRef.element) {
                    arrayList.addAll(contact.getContactNumber());
                }
                if (!objectRef.element.isEmpty()) {
                    getStateOfContactById().setValue(objectRef.element.get(0));
                }
                getStateOfPhoneNumber().setValue(arrayList);
                return Unit.INSTANCE;
            }
        });
    }

    public void deleteContact(Context context, List<Contact> list) {
        Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                booleanRef.element = new DeleteContactsHelper().invoke(context, list);
                return Unit.INSTANCE;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfDeleteContact().setValue(booleanRef.element);
                return Unit.INSTANCE;
            }
        });
    }

}
