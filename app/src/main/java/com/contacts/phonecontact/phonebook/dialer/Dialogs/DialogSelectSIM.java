package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.telecom.PhoneAccountHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogDataModels.SIMAccount;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSelectSimBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.CallLogDataFetch.AddAllCallLogsInDatabaseUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class DialogSelectSIM {
    private final Activity activity;
    private final Function1<PhoneAccountHandle, Unit> callback;
    private final String phoneNumber;
    private AlertDialog dialog;
    private DialogSelectSimBinding mDialogSelectSimBinding;

    public DialogSelectSIM(Activity activity2, String str, Function1<? super PhoneAccountHandle, Unit> function1) {
        this.activity = activity2;
        this.phoneNumber = str;
        this.callback = (Function1<PhoneAccountHandle, Unit>) function1;
        mDialogSelectSimBinding = DialogSelectSimBinding.inflate(LayoutInflater.from(activity2));
        mDialogSelectSimBinding.selectSimRememberHolder.setOnClickListener(view -> {
            mDialogSelectSimBinding.selectSimRemember.toggle();
        });
        int i = 0;
        for (SIMAccount t : AddAllCallLogsInDatabaseUtils.getAvailableSIMCardLabels(activity2)) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            SIMAccount t2 = t;
            View inflate2 = activity.getLayoutInflater().inflate(R.layout.radio_button, (ViewGroup) null);
            Intrinsics.checkNotNull(inflate2, "null cannot be cast to non-null type android.widget.RadioButton");
            RadioButton radioButton = (RadioButton) inflate2;
            radioButton.setText(i2 + " - " + t2.getLabel());
            radioButton.setId(i);
            radioButton.setOnClickListener(view -> {
                selectedSIM(t2.getHandle(), t2.getLabel());
            });
            mDialogSelectSimBinding.selectSimRadioGroup.addView(radioButton, new RadioGroup.LayoutParams(-1, -2));
            i = i2;
        }
        dialog = new AlertDialog.Builder(activity).create();
    }

    public Activity getActivity() {
        return activity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Function1<PhoneAccountHandle, Unit> getCallback() {
        return callback;
    }

    private void selectedSIM(PhoneAccountHandle phoneAccountHandle, String str) {
        if (mDialogSelectSimBinding.selectSimRemember.isChecked()) {
            ContaxtExtUtils.getConfig(activity).saveCustomSIM(phoneNumber, str);
        }
        callback.invoke(phoneAccountHandle);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
