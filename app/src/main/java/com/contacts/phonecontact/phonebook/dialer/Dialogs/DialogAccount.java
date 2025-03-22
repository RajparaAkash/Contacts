package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAccountList;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogAccountBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAccountSelectionChange;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSavingAccountSelect;

import java.util.List;

import kotlin.collections.CollectionsKt;

public class DialogAccount extends Dialog {
    Activity activity;
    public OnSavingAccountSelect mOnAccountSelectionChange;
    private List<ContactSource> aList = CollectionsKt.emptyList();
    private AdapterAccountList adapterAccountList;
    private DialogAccountBinding mDialogAccountBinding;


    public DialogAccount(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public Context getMContact() {
        return this.activity;
    }

    public List<ContactSource> getAList() {
        return this.aList;
    }

    public void setAList(List<ContactSource> list) {
        this.aList = list;
    }

    public void setOnAccountChangeListener(OnSavingAccountSelect onAccountSelectionChange) {
        this.mOnAccountSelectionChange = onAccountSelectionChange;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mDialogAccountBinding = DialogAccountBinding.inflate(LayoutInflater.from(activity));
        mDialogAccountBinding.getRoot().setBackground(ContextCompat.getDrawable(getContext(), R.color.transparent));
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(mDialogAccountBinding.getRoot());
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
        adapterAccountList = new AdapterAccountList(activity);
        adapterAccountList.setOnAccountChangeListener(new OnAccountSelectionChange() {
            @Override
            public void onChange(int i) {
                if (i == 0) {
                    dismiss();
                    return;
                }
                mOnAccountSelectionChange.onAccountSelect(aList.get(i).getName(), i);
                dismiss();
            }
        });
        mDialogAccountBinding.rcvAccountList.setLayoutManager(new GridLayoutManager(activity, 1));
        mDialogAccountBinding.rcvAccountList.setAdapter(adapterAccountList);
        mDialogAccountBinding.btnClose.setOnClickListener(view -> {
            dismiss();
        });

    }

    public void setAccountList(List<ContactSource> list) {
        this.aList = list;
        adapterAccountList.setAccountList(list);
    }

}
