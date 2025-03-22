package com.contacts.phonecontact.phonebook.dialer.Activities;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityProfileViewBinding;

public class ActivityProfileView extends ActBase<ActivityProfileViewBinding> {

    ActivityProfileViewBinding binding;

    private String profileImage = "";

    @Override
    public void bindMethods() {
    }

    @Override
    public void onContactUpdate() {
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String str) {
        this.profileImage = str;
    }

    @Override
    public ActivityProfileViewBinding setViewBinding() {
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        profileImage = getIntent().getStringExtra("profilePath");
        binding.ivDisplay.getController().getSettings().setMaxZoom(6.0f).setMinZoom(0.0f).setDoubleTapZoom(3.0f);
        Glide.with((FragmentActivity) this).load(profileImage).into(binding.ivDisplay);
    }

    @Override
    public void bindListeners() {
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });
    }
}
