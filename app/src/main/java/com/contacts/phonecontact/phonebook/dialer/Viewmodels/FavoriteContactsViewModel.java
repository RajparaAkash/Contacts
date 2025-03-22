package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.DeleteContactsHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public class FavoriteContactsViewModel extends ViewModel {
    private final MutableLiveData<Boolean> stateOfDeleteContact = new MutableLiveData<>();
    private final MutableLiveData<List<ListObject>> stateOfFavoriteContacts = new MutableLiveData<>();

    public MutableLiveData<Boolean> getStateOfDeleteContact() {
        return this.stateOfDeleteContact;
    }

    public MutableLiveData<List<ListObject>> getStateOfFavoriteContacts() {
        return this.stateOfFavoriteContacts;
    }

    public void loadAllFavoriteContacts(Context context, ContactDatabase contactDatabase, List<Contact> list) {
        LinkedHashMap<String, List<Contact>> linkedHashMap = new LinkedHashMap<>();
        Ref.ObjectRef<List<ListObject>> objectRef = new Ref.ObjectRef<>();
        objectRef.element = CollectionsKt.emptyList();
        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return Unit.INSTANCE;
            }
        }, new FavoriteContactsViewModelLoadAllFavoriteContacts2(context, list, contactDatabase, objectRef, this, linkedHashMap), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getStateOfFavoriteContacts().setValue(objectRef.element);
                return Unit.INSTANCE;
            }
        });
    }

    public List<ListObject> generateListFromMap(Map<String, ? extends List<Contact>> map) {
        ArrayList<ListObject> arrayList = new ArrayList();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            Intrinsics.checkNotNull(obj);
            List list = (List) obj;
            HeaderModel headerModel = new HeaderModel(str, (Contact) list.get(0));
            list.remove(0);
            arrayList.add(headerModel);
            arrayList.addAll(list);
        }
        return arrayList;
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

//    public void shareContact(Context context, List<Contact> list) {
//        ThreadExtensionUtils.executeAsyncTask(ViewModelKt.getViewModelScope(this), new Function0<Unit>() {
//            @Override
//            public Unit invoke() {
//                return Unit.INSTANCE;
//            }
//        }, new Function0<Unit>() {
//            @Override
//            public Unit invoke() {
//                new ShareContactHelper().invoke(context, list);
//                return Unit.INSTANCE;
//            }
//        }, new Function0<Unit>() {
//            @Override
//            public Unit invoke() {
//                return Unit.INSTANCE;
//            }
//        });
//    }

}
