package com.contacts.phonecontact.phonebook.dialer.Viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;

import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.BlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.UnBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ThreadExtensionUtils;

import java.util.List;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Ref;

public class BlockNumberViewModel extends ViewModel {
    private final MutableLiveData<List<BlockContact>> stateOfBlockNumber = new MutableLiveData<>();

    public MutableLiveData<List<BlockContact>> getStateOfBlockNumber() {
        return this.stateOfBlockNumber;
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
                getAllBlockNumber(context);
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
                getAllBlockNumber(context);
                return Unit.INSTANCE;
            }
        });
    }
}
