package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.contacts.phonecontact.phonebook.dialer.databinding.DialogCommonBinding;

public class DialogCommon extends Dialog {
    Activity activity;
    DialogCommonBinding binding;
    String title;
    String message;
    String positive;
    String negative;
    OnDialogSelectionListener listener;

    public interface OnDialogSelectionListener {
        void onDoneClick();
    }

    public DialogCommon(Activity activity, String title, String message, String positive, String negative, OnDialogSelectionListener listener) {
        super(activity);
        this.activity = activity;
        this.title = title;
        this.message = message;
        this.positive = positive;
        this.negative = negative;
        this.listener = listener;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DialogCommonBinding.inflate(LayoutInflater.from(activity));
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(binding.getRoot());
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }

        binding.tvTitle.setText(title);
        binding.tvMessage.setText(message);
        binding.tvYes.setText(positive);
        binding.tvCancel.setText(negative);

        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        binding.tvYes.setOnClickListener(view -> {
            dismiss();
            listener.onDoneClick();
        });


//        if (activity != null && !activity.isFinishing()) {
//            show();
//        }

    }

}
