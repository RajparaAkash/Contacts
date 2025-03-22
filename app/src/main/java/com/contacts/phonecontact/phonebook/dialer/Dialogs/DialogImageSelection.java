package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.contacts.phonecontact.phonebook.dialer.databinding.DialogImageSelectionBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnImageSelectionOptionSelect;

public class DialogImageSelection extends AlertDialog {
    private final Context context;
    private DialogImageSelectionBinding binding;
    private OnImageSelectionOptionSelect mOnImageSelectionOptionSelect;
    String img;

    public DialogImageSelection(Context context, String img) {
        super(context);
        this.context = context;
        this.img = img;
    }

    public Context getMContact() {
        return this.context;
    }

    public void setListener(OnImageSelectionOptionSelect onImageSelectionOptionSelect) {
        this.mOnImageSelectionOptionSelect = onImageSelectionOptionSelect;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DialogImageSelectionBinding.inflate(LayoutInflater.from(context));
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Window window2 = getWindow();
        if (window2 != null) {
            window2.setLayout(-1, -2);
        }
        setContentView(binding.getRoot());

        if (img != null && !img.isEmpty()) {
            binding.tvRemovePhoto.setVisibility(View.VISIBLE);
        } else {
            binding.tvRemovePhoto.setVisibility(View.GONE);
        }

        binding.tvRemovePhoto.setOnClickListener(view -> {
            dismiss();
            mOnImageSelectionOptionSelect.onRemovePhoto();
        });
        binding.tvSelectPhoto.setOnClickListener(view -> {
            dismiss();
            mOnImageSelectionOptionSelect.onSelectPhoto();
        });
        binding.tvNewPhoto.setOnClickListener(view -> {
            dismiss();
            mOnImageSelectionOptionSelect.onTakeNewPhoto();
        });
        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });
    }

}
