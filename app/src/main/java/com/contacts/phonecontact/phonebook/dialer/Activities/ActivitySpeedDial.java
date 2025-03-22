package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSelectContact;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivitySpeedDialBinding;

public class ActivitySpeedDial extends BaseActivity {

    ActivitySpeedDialBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpeedDialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        setDialText(binding.tvDial2, "2");
        setDialText(binding.tvDial3, "3");
        setDialText(binding.tvDial4, "4");
        setDialText(binding.tvDial5, "5");
        setDialText(binding.tvDial6, "6");
        setDialText(binding.tvDial7, "7");
        setDialText(binding.tvDial8, "8");
        setDialText(binding.tvDial9, "9");

        binding.llDial2.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("2", contact);
                    binding.tvDial2.setText(MyApplication.getInstance().getSpeedDialContact("2").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial3.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("3", contact);
                    binding.tvDial3.setText(MyApplication.getInstance().getSpeedDialContact("3").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial4.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("4", contact);
                    binding.tvDial4.setText(MyApplication.getInstance().getSpeedDialContact("4").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial5.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("5", contact);
                    binding.tvDial5.setText(MyApplication.getInstance().getSpeedDialContact("5").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial6.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("6", contact);
                    binding.tvDial6.setText(MyApplication.getInstance().getSpeedDialContact("6").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial7.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("7", contact);
                    binding.tvDial7.setText(MyApplication.getInstance().getSpeedDialContact("7").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial8.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("8", contact);
                    binding.tvDial8.setText(MyApplication.getInstance().getSpeedDialContact("8").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.llDial9.setOnClickListener(view -> {
            new DialogSelectContact(ActivitySpeedDial.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    MyApplication.getInstance().setSpeedDialContact("9", contact);
                    binding.tvDial9.setText(MyApplication.getInstance().getSpeedDialContact("9").getNameToDisplay());
                }
            }).show(getSupportFragmentManager(), "");
        });

    }

    public void setDialText(TextView textView, String key) {
        if (MyApplication.getInstance().getSpeedDialContact(key) != null) {
            textView.setText(MyApplication.getInstance().getSpeedDialContact(key).getNameToDisplay());
        } else {
            textView.setText(getString(R.string.speed_dial_not_assign));
        }
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
            finish();
        });
    }

}
