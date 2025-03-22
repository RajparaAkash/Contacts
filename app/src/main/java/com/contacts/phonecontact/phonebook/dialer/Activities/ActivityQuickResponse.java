package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ModelQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityQuickResponseBinding;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogAddQuickResponse;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.QuickResponseViewModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuickResponse extends BaseActivity {

    ActivityQuickResponseBinding binding;
    AdapterQuickResponse adapterQuickResponse;
    QuickResponseViewModel quickResponseViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuickResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApplication.getInstance().showNativeBannerAd(this, findViewById(R.id.flNativeBanner));


        quickResponseViewModel = new ViewModelProvider(ActivityQuickResponse.this).get(QuickResponseViewModel.class);

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.tvAddResponse.setOnClickListener(view -> {
            new DialogAddQuickResponse(ActivityQuickResponse.this, null, new DialogAddQuickResponse.OnAddListener() {
                @Override
                public void onAdded() {

                }
            }).show(getSupportFragmentManager(), "DialogAddQuickResponse");
        });


        binding.rvQuickResponse.setLayoutManager(new GridLayoutManager(ActivityQuickResponse.this, 1));
        adapterQuickResponse = new AdapterQuickResponse(ActivityQuickResponse.this, new AdapterQuickResponse.OnItemClickListener() {
            @Override
            public void onClicked(ModelQuickResponse modelQuickResponse) {
                DialogAddQuickResponse dialogAddQuickResponse = new DialogAddQuickResponse(ActivityQuickResponse.this, modelQuickResponse, new DialogAddQuickResponse.OnAddListener() {
                    @Override
                    public void onAdded() {

                    }
                });
                dialogAddQuickResponse.show(getSupportFragmentManager(), dialogAddQuickResponse.getTag());
            }
        });
        binding.rvQuickResponse.setAdapter(adapterQuickResponse);

        quickResponseViewModel.getResponseList().observe(this, new Observer<List<ModelQuickResponse>>() {
            @Override
            public void onChanged(List<ModelQuickResponse> webHistories) {

                if (adapterQuickResponse != null) {
                    adapterQuickResponse.setDataList((ArrayList<ModelQuickResponse>) webHistories);
                }
                if (webHistories.size() > 0) {
                    binding.rvQuickResponse.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);
                } else {
                    binding.rvQuickResponse.setVisibility(View.GONE);
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
