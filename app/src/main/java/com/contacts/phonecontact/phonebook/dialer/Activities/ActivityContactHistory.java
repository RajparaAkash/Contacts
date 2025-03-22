package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSelectedCallLog;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogCommon;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.CallLogViewModel;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactInfoViewModel;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityContactHistoryBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSelectedCallLogClick;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class ActivityContactHistory extends ActBase<ActivityContactHistoryBinding> {

    public ArrayList<ObjectCallLog> callList = new ArrayList<>();
    public boolean isDragEnabled = true;
    public AdapterSelectedCallLog adapterSelectedCallLog;
    public CallLogViewModel mCallLogViewModel;
    public ContactInfoViewModel mContactInfoViewModel;
    public ContactDatabase mDatabase;
    public CallLogModel mSelectedCallLogModel;
    public Contact selectedContact;
    public ActivityContactHistoryBinding binding;
    private boolean isVideoCall;

    @Override
    public void onContactUpdate() {
    }

    public boolean isVideoCall() {
        return this.isVideoCall;
    }

    public void setVideoCall(boolean z) {
        this.isVideoCall = z;
    }

    @Override
    public ActivityContactHistoryBinding setViewBinding() {
        binding = ActivityContactHistoryBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));

        mDatabase = ContactDatabase.Companion.invoke(ActivityContactHistory.this);
        adapterSelectedCallLog = new AdapterSelectedCallLog(ActivityContactHistory.this);
        mCallLogViewModel = (CallLogViewModel) new ViewModelProvider(ActivityContactHistory.this).get(CallLogViewModel.class);
        mContactInfoViewModel = (ContactInfoViewModel) new ViewModelProvider(ActivityContactHistory.this).get(ContactInfoViewModel.class);
    }

    @Override
    public void bindListeners() {
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
        adapterSelectedCallLog.setOnCallLogClick(new OnSelectedCallLogClick() {
            @Override
            public void onClick(CallLogModel callLogModel) {
            }

            @Override
            public void onDelete(CallLogModel callLogModel) {
                mCallLogViewModel.deleteCallLog(ActivityContactHistory.this, callLogModel, mDatabase);
            }
        });
        binding.ivDeleteAll.setOnClickListener(view -> {
            deleteAllHistory();
        });
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
            finish();
        });
    }

    @Override
    public void bindMethods() {
        boolean z = true;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityContactHistory.this, RecyclerView.VERTICAL, false);
//        linearLayoutManager.setStackFromEnd(true);
        binding.rvHistory.setLayoutManager(linearLayoutManager);
        binding.rvHistory.setAdapter(adapterSelectedCallLog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Serializable serializable = extras.getSerializable("mSelectedCallLogModel");
            mSelectedCallLogModel = (CallLogModel) serializable;
            if (Intrinsics.areEqual(mSelectedCallLogModel.getName(), mSelectedCallLogModel.getPhoneNumber())) {
                binding.tvContactName.setText(PhoneNumberUtils.formatNumber(StringsKt.trim((CharSequence) mSelectedCallLogModel.getName()).toString(), "IN"));
            } else {
                binding.tvContactName.setText(mSelectedCallLogModel.getName());
            }
            binding.tvNumber.setText(PhoneNumberUtils.formatNumber(mSelectedCallLogModel.getPhoneNumber(), "IN"));
            String name = mSelectedCallLogModel.getName();
            mCallLogViewModel.getSelectedKeyHistory(ActivityContactHistory.this, name, mDatabase);
            String name2 = mSelectedCallLogModel.getName();
            try {
                binding.tvFirstLetter.setText(String.valueOf((!(name2 == null || name2.length() == 0) ? mSelectedCallLogModel.getName() : mSelectedCallLogModel.getPhoneNumber()).charAt(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Integer bgColor = mSelectedCallLogModel.getBgColor();
            binding.ivUserProfile.setColorFilter(bgColor);
            String contactId = mSelectedCallLogModel.getContactId();
            if (!(contactId == null || contactId.length() == 0)) {
                z = false;
            }
            if (z) {
                ViewExtensionUtils.show(binding.btnAddContact);
                ViewExtensionUtils.show(binding.ivAddContact);
                ViewExtensionUtils.gone(binding.btnMakeVideoCall);
                ViewExtensionUtils.gone(binding.tvFirstLetter);
            } else {
                ViewExtensionUtils.show(binding.btnMakeVideoCall);
                ViewExtensionUtils.gone(binding.btnAddContact);
                ViewExtensionUtils.gone(binding.ivAddContact);
                ViewExtensionUtils.show(binding.btnMakeVideoCall);
                ViewExtensionUtils.show(binding.tvFirstLetter);
                String contactId2 = mSelectedCallLogModel.getContactId();
                if (contactId2 != null) {
                    mContactInfoViewModel.getContactById(ContactDatabase.Companion.invoke(ActivityContactHistory.this), contactId2);
                }
                mContactInfoViewModel.getStateOfContactById().observe(this, new Observer<Contact>() {
                    @Override
                    public void onChanged(Contact contact) {



                        if (mSelectedCallLogModel.getContactId() != null && contact != null) {
                            selectedContact = contact;
//                            Log.e("iddddd", String.valueOf(selectedContact.getContactId()));
                            if (selectedContact.getContactIdSimple() != 0) {
                                ContactDatabase invoke = ContactDatabase.Companion.invoke(ActivityContactHistory.this);
                                mContactInfoViewModel.getsourcenumber(invoke, String.valueOf(selectedContact.getContactIdSimple()));
                            }
                            mContactInfoViewModel.getViewNumberSorcs().observe(ActivityContactHistory.this, new Observer<List<String>>() {
                                @Override
                                public void onChanged(List<String> list) {
                                    if (list != null && list.size() > 0) {
                                        for (String t : list) {
                                            if (t.contentEquals("Duo") || t.contentEquals("Meet")) {
                                                setVideoCall(true);
                                                binding.tvVcall.setTextColor(ContextCompat.getColor(ActivityContactHistory.this, R.color.black));
                                                binding.icVCall.setColorFilter(ContextCompat.getColor(ActivityContactHistory.this, R.color.black));
                                            }
                                        }
                                    }
                                }
                            });
                            String contactPhotoUri = selectedContact.getContactPhotoUri();
                            if (contactPhotoUri == null || contactPhotoUri.length() == 0) {
                                ViewExtensionUtils.show(binding.tvFirstLetter);
                                Integer bgColor = selectedContact.getBgColor();
                                binding.ivUserProfile.setColorFilter(bgColor.intValue());
                            } else {
                                binding.ivUserProfile.setColorFilter((ColorFilter) null);
                                ViewExtensionUtils.invisible(binding.tvFirstLetter);
                                Glide.with(binding.getRoot().getContext()).load(selectedContact.getContactPhotoUri()).into(binding.ivUserProfile);
                                String contactPhotoUri2 = selectedContact.getContactPhotoUri();
//                                Log.e("fdvdgvsfg", contactPhotoUri2);
                            }
                        }


                    }
                });
                if (!mSelectedCallLogModel.getName().isEmpty()) {
                    binding.tvFirstLetter.setText(String.valueOf(mSelectedCallLogModel.getName().charAt(0)));
                }
                Integer bgColor2 = mSelectedCallLogModel.getBgColor();
                binding.ivUserProfile.setColorFilter(bgColor2);
                String name3 = mSelectedCallLogModel.getName();
                mCallLogViewModel.getSelectedKeyHistory(ActivityContactHistory.this, name3, mDatabase);
            }
        }
        mCallLogViewModel.getStateOfHistory().observe(ActivityContactHistory.this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> list) {
                if (list != null && list.size() > 0) {
                    callList = (ArrayList) list;
                    adapterSelectedCallLog.setData(TypeIntrinsics.asMutableList(list));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isDragEnabled = true;
    //                      binding.rvHistory.smoothScrollToPosition(list.size());
                        }
                    }, 500);
                    ViewExtensionUtils.gone(binding.loadingBar);
                }
            }
        });
        mCallLogViewModel.getHistoryListState().observe(ActivityContactHistory.this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> objectCallLogs) {
                String name = mSelectedCallLogModel.getName();
                mCallLogViewModel.getSelectedKeyHistory(ActivityContactHistory.this, name, mDatabase);
            }
        });
        mCallLogViewModel.getStateOfDelete().observe(ActivityContactHistory.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ViewExtensionUtils.gone(binding.loadingBar);
                String name = mSelectedCallLogModel.getName();
                mCallLogViewModel.getSelectedKeyHistory(ActivityContactHistory.this, name, mDatabase);
            }
        });
        mCallLogViewModel.getStateOfDeleteAll().observe(ActivityContactHistory.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    finish();
                } else {
                    String name = mSelectedCallLogModel.getName();
                    mCallLogViewModel.getSelectedKeyHistory(ActivityContactHistory.this, name, mDatabase);
                }
            }
        });
        binding.btnMakeCall.setOnClickListener(view -> {
            ContaxtExtUtils.makeACall(ActivityContactHistory.this, mSelectedCallLogModel.getPhoneNumber());
        });
        binding.btnMakeVideoCall.setOnClickListener(view -> {
            if (isVideoCall) {
                ContaxtExtUtils.makeAVideoCall(ActivityContactHistory.this, mSelectedCallLogModel.getPhoneNumber());
            }
        });
        binding.btnSendTextMessage.setOnClickListener(view -> {
            ContaxtExtUtils.sendTextMessage(ActivityContactHistory.this, mSelectedCallLogModel.getPhoneNumber());
        });
        binding.btnAddContact.setOnClickListener(view -> {
            Contact emptyContact = getEmptyContact();
            selectedContact = emptyContact;
            ArrayList<PhoneNumber> contactNumber = emptyContact.getContactNumber();
            String phoneNumber = mSelectedCallLogModel.getPhoneNumber();
            PhoneNumberType phoneNumberType = PhoneNumberType.NO_LABEL;
            contactNumber.add(new PhoneNumber(phoneNumber, phoneNumberType, "", mSelectedCallLogModel.getPhoneNumber(), true));
            Intent intent = new Intent(ActivityContactHistory.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", selectedContact);
            intent.putExtra("isUpdate", false);
            startActivity(intent);
        });

    }


    private void deleteAllHistory() {
        new DialogCommon(this, getString(R.string.delete), getString(R.string.message_delete_history), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
            @Override
            public void onDoneClick() {
                ViewExtensionUtils.show(binding.loadingBar);
                mCallLogViewModel.deleteAllCallLog(ActivityContactHistory.this, callList, mDatabase);
            }
        }).show();

    }

    private Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "",
                "", null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", null, Integer.valueOf(ContextCompat.getColor(this, R.color.app_color)), 16777217, null);
    }

}
