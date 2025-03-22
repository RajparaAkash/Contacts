package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogFilterBinding;

public class DialogFilter extends BottomSheetDialogFragment {

    Activity activity;
    DialogFilterBinding binding;
    OnFilterApplyListener listener;
    String calls = "";

    public DialogFilter(Activity activity, String calls, OnFilterApplyListener listener) {
        this.activity = activity;
        this.calls = calls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return super.onCreateDialog(bundle);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogFilterBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);

        if (calls.equals("All Calls")) {
            binding.rbAllCalls.setChecked(true);
        } else if (calls.equals(getString(R.string.missed_Call))) {
            binding.rbMissesCalls.setChecked(true);
        } else if (calls.equals(getString(R.string.incoming))) {
            binding.rbIncomingCalls.setChecked(true);
        } else if (calls.equals(getString(R.string.outgoing))) {
            binding.rbOutgoingCalls.setChecked(true);
        } else {
            binding.rbAllCalls.setChecked(true);
        }

        binding.tvReset.setOnClickListener(view -> {
            binding.rbAllCalls.setChecked(true);
        });

        binding.tvApply.setOnClickListener(view -> {
            String calls = "";

            int selectedCalls = binding.rgCallType.getCheckedRadioButtonId();
            if (selectedCalls == R.id.rbAllCalls) {
                calls = "All Calls";
            } else if (selectedCalls == R.id.rbMissesCalls) {
                calls = getString(R.string.missed_Call);
            } else if (selectedCalls == R.id.rbIncomingCalls) {
                calls = getString(R.string.incoming);
            } else if (selectedCalls == R.id.rbOutgoingCalls) {
                calls = getString(R.string.outgoing);
            }

            listener.onApplied(calls);
            dismiss();
        });
    }

    public interface OnFilterApplyListener {
        void onApplied(String calls);
    }
}
