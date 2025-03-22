package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.telecom.Call;

import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityConferenceBinding;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterConferenceCall;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.util.CallManager;

import java.util.ArrayList;

public class ConferenceActivity extends ActBase<ActivityConferenceBinding> {

    ActivityConferenceBinding binding;

    private ArrayList<Call> callList = new ArrayList<>();
    private AdapterConferenceCall adapterConferenceCall;

    @Override
    public void onContactUpdate() {
    }

    @Override
    public ActivityConferenceBinding setViewBinding() {
        binding = ActivityConferenceBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));

        callList = new ArrayList<>(CallManager.Companion.getConferenceCalls());
        adapterConferenceCall = new AdapterConferenceCall(this);
    }

    @Override
    public void bindListeners() {
        binding.icBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void bindMethods() {
        adapterConferenceCall.setCallList(callList);
        binding.conferenceCallsList.setLayoutManager(new GridLayoutManager(this, 1));
        binding.conferenceCallsList.setAdapter(adapterConferenceCall);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
