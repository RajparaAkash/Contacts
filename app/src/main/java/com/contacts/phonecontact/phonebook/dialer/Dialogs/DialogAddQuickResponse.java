package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.database.QuickResponseDatabase;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogAddQuickResponseBinding;

public class DialogAddQuickResponse extends BottomSheetDialogFragment {
    Activity activity;
    DialogAddQuickResponseBinding binding;
    OnAddListener listener;
    ModelQuickResponse modelQuickResponse;

    public interface OnAddListener {
        void onAdded();
    }

    public DialogAddQuickResponse(Activity activity, ModelQuickResponse modelQuickResponse, OnAddListener listener) {
        this.activity = activity;
        this.modelQuickResponse = modelQuickResponse;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        return onCreateDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogAddQuickResponseBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);

        if (modelQuickResponse != null) {
            binding.tvTitle.setText(getString(R.string.dialog_quick_response_edit));
            binding.tvAdd.setText(getString(R.string.update));
            binding.etMessage.setText(modelQuickResponse.getMessage());
        }

        binding.etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.etMessage.setBackgroundResource(R.drawable.bg_edittext_response_sel);
                } else {
                    binding.etMessage.setBackgroundResource(R.drawable.bg_edittext_response);
                }
            }
        });

        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        binding.tvAdd.setOnClickListener(view -> {
            String message = binding.etMessage.getText().toString().trim();
            if (message != null && !message.isEmpty()) {
                dismiss();

                if (modelQuickResponse != null) {
                    modelQuickResponse.setMessage(message);
                    QuickResponseDatabase.getInstance(activity).quickResponseDao().update(modelQuickResponse);
                } else {
                    QuickResponseDatabase.getInstance(activity).quickResponseDao().insert(new ModelQuickResponse(message));
                }

                listener.onAdded();
            }
        });

    }

}
