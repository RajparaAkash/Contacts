package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogNameFormatBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnNameFormatChangeListener;
import com.contacts.phonecontact.phonebook.dialer.types.ContactNameFormatType;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

public class DialogNameFormat extends AlertDialog {
    private Context context;
    private DialogNameFormatBinding mDialogNameFormatBinding;
    private OnNameFormatChangeListener mOnNameFormatChangeListener;

    public DialogNameFormat(Context context) {
        super(context);
        this.context = context;
    }


    public Context getMContact() {
        return context;
    }

    public void setNameFormatChangeListener(OnNameFormatChangeListener onNameFormatChangeListener) {
        this.mOnNameFormatChangeListener = onNameFormatChangeListener;
    }


    public void onCreate(Bundle bundle) {
        Integer num = null;
        super.onCreate(bundle);
        mDialogNameFormatBinding = DialogNameFormatBinding.inflate(LayoutInflater.from(context));
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(17170445);
        }
        Window window2 = getWindow();
        if (window2 != null) {
            window2.setLayout(-1, -2);
        }
        setContentView(mDialogNameFormatBinding.getRoot());
        SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
        String string = "KeyNameFormat";
        Integer valueOf = ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType();
        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
        int i = 0;
        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
            if (valueOf != null) {
                i = valueOf.intValue();
            }
            num = Integer.valueOf(contactAppPreference.getInt(string, i));
        }
        int intValue = num.intValue();
        if (intValue == ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType()) {
            mDialogNameFormatBinding.ivFirstName.setImageResource(R.drawable.ic_radio_button_checked);
            mDialogNameFormatBinding.ivSurname.setImageResource(R.drawable.ic_radio_button_unchecked);
        } else if (intValue == ContactNameFormatType.SURNAME_FIRST.getSelectedType()) {
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
            SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
            String string = "KeyNameFormat";
            Integer valueOf = ContactNameFormatType.FIRST_NAME_FIRST.getSelectedType();
            SharedPreferences.Editor edit = contactAppPreference.edit();
            KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
            if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                edit.putInt(string, valueOf);
            }
            edit.commit();
            if (mOnNameFormatChangeListener != null) {
                mOnNameFormatChangeListener.onFirstnameSelected();
            }
            dismiss();
        });
        mDialogNameFormatBinding.layoutSurname.setOnClickListener(view -> {
            SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(context);
            String string = "KeyNameFormat";
            Integer valueOf = ContactNameFormatType.SURNAME_FIRST.getSelectedType();
            SharedPreferences.Editor edit = contactAppPreference.edit();
            KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Integer.class);
            if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
                edit.putInt(string, valueOf.intValue());
            }
            edit.commit();
            if (mOnNameFormatChangeListener != null) {
                mOnNameFormatChangeListener.onSurnameSelected();
            }
            dismiss();
        });
    }

}
