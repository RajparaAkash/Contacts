package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSelectedCallLog;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.CallLogViewModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityCallHistoryBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.TypeIntrinsics;

public class ActivityCallHistory extends BaseActivity {

    public ArrayList<ObjectCallLog> callList = new ArrayList<>();
    public AdapterSelectedCallLog adapterSelectedCallLog;
    public CallLogViewModel mCallLogViewModel;
    public ContactDatabase mDatabase;
    ActivityCallHistoryBinding binding;
    String contactName = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));


        contactName = getIntent().getStringExtra("contactName");

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        mCallLogViewModel = (CallLogViewModel) new ViewModelProvider(ActivityCallHistory.this).get(CallLogViewModel.class);
        mDatabase = ContactDatabase.Companion.invoke(ActivityCallHistory.this);
        adapterSelectedCallLog = new AdapterSelectedCallLog(ActivityCallHistory.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityCallHistory.this, RecyclerView.VERTICAL, false);
//        linearLayoutManager.setStackFromEnd(true);
        binding.rvHistory.setLayoutManager(linearLayoutManager);
        binding.rvHistory.setAdapter(adapterSelectedCallLog);


        mCallLogViewModel.getSelectedKeyHistory(ActivityCallHistory.this, contactName, mDatabase);

        mCallLogViewModel.getStateOfHistory().observe(ActivityCallHistory.this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> list) {
                if (list != null && list.size() > 0) {
                    callList = new ArrayList<>();
                    callList = (ArrayList) list;
                    adapterSelectedCallLog.setData(TypeIntrinsics.asMutableList(callList));

                    binding.llNoData.setVisibility(View.GONE);

                } else {
                    binding.llNoData.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
            finish();
        });
    }

}
