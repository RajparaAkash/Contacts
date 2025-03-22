package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityPopUpBinding;

public class ActivityPopUp extends BaseActivity {

    ActivityPopUpBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.switchAnimation.setOnClickListener(view -> {
            finish();
        });
        binding.topLayout.setOnClickListener(view -> {
            finish();
        });

    }

}
