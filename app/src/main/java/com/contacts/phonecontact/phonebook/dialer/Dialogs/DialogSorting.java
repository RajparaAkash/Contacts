package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSortingBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnContactSortingChangeListener;
import com.contacts.phonecontact.phonebook.dialer.types.ContactSorting;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

public class DialogSorting extends AlertDialog {
    private  Context mContact;
    private DialogSortingBinding mDialogNameFormatBinding;
    private OnContactSortingChangeListener mOnNameFormatChangeListener;

    public DialogSorting(Context context) {
        super(context);
        this.mContact = context;
    }


    public Context getMContact() {
        return this.mContact;
    }

    public void setNameFormatChangeListener(OnContactSortingChangeListener onContactSortingChangeListener) {
        this.mOnNameFormatChangeListener = onContactSortingChangeListener;
    }

    public void onCreate(Bundle bundle) {
        Integer num = null;
        super.onCreate(bundle);
        mDialogNameFormatBinding = DialogSortingBinding.inflate(LayoutInflater.from(mContact));
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(17170445);
        }
        Window window2 = getWindow();
        if (window2 != null) {
            window2.setLayout(-1, -2);
        }
        setContentView(mDialogNameFormatBinding.getRoot());
        SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(mContact);
        String string = "KeyContactSorting";
        Integer valueOf = Integer.valueOf(ContactSorting.FIRST_NAME.getSelectedType());
        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
        int i = 0;
        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
            if (valueOf != null) {
                i = valueOf.intValue();
            }
            num = Integer.valueOf(contactAppPreference.getInt(string, i));
        }
        int intValue = num.intValue();
        if (intValue == ContactSorting.FIRST_NAME.getSelectedType()) {
            mDialogNameFormatBinding.ivFirstName.setImageResource(R.drawable.ic_radio_button_checked);
            mDialogNameFormatBinding.ivSurname.setImageResource(R.drawable.ic_radio_button_unchecked);
        } else if (intValue == ContactSorting.SURNAME.getSelectedType()) {
            mDialogNameFormatBinding.ivSurname.setImageResource(R.drawable.ic_radio_button_checked);
            mDialogNameFormatBinding.ivFirstName.setImageResource(R.drawable.ic_radio_button_unchecked);
        }
        setListener();
    }

    private void setListener() {
        mDialogNameFormatBinding.btnCancel.setOnClickListener(view -> {
            dismiss();
        });
        mDialogNameFormatBinding.layoutFirstName.setOnClickListener(view -> {
            SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(mContact);
            String string = "KeyContactSorting";
            Integer valueOf = Integer.valueOf(ContactSorting.FIRST_NAME.getSelectedType());
            SharedPreferences.Editor edit = contactAppPreference.edit();
            KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
            if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                edit.putInt(string, valueOf.intValue());
            }
            edit.commit();
            mOnNameFormatChangeListener.sortedByFirstName();
            dismiss();
        });
        mDialogNameFormatBinding.layoutSurname.setOnClickListener(view -> {
            SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(mContact);
            String string = "KeyContactSorting";
            Integer valueOf = Integer.valueOf(ContactSorting.SURNAME.getSelectedType());
            SharedPreferences.Editor edit = contactAppPreference.edit();
            KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
            if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                edit.putInt(string, valueOf.intValue());
            }
            edit.commit();
            mOnNameFormatChangeListener.sortedBySurname();
            dismiss();
        });
    }

}
