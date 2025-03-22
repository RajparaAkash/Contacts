package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSelectContact;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactInfoViewModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityBlockContactBinding;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterBlockNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnUnblockClick;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.BlockNumberViewModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class BlockContactAct extends ActBase<ActivityBlockContactBinding> {

    public ActivityResultLauncher<Intent> launcherAddBlockContact;
    public AdapterBlockNumber adapterBlockNumber;
    public BlockNumberViewModel mBlockNumberViewModel;
    ActivityBlockContactBinding binding;

    @Override
    public void onContactUpdate() {
    }

    @Override
    public ActivityBlockContactBinding setViewBinding() {
        binding = ActivityBlockContactBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {

        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));

        mContactInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(ContactInfoViewModel.class);
        mBlockNumberViewModel = (BlockNumberViewModel) new ViewModelProvider(this).get(BlockNumberViewModel.class);
        adapterBlockNumber = new AdapterBlockNumber();
        launcherAddBlockContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    mBlockNumberViewModel.getAllBlockNumber(BlockContactAct.this);
                }
            }
        });
    }

    @Override
    public void bindListeners() {
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.switchBlockUnknown.setChecked(MyApplication.getInstance().getBlockUnknownNum());
        binding.switchBlockUnknown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MyApplication.getInstance().setBlockUnknownNum(b);
            }
        });
        binding.llBlockUnknown.setOnClickListener(view -> {
            binding.switchBlockUnknown.setChecked(!binding.switchBlockUnknown.isChecked());
        });

        binding.ivSelectContact.setOnClickListener(view -> {
//            Intent intent = new Intent(BlockContactAct.this, ActivityContactPicker.class);
//            launcherAddBlockContact.launch(intent);

            new DialogSelectContact(BlockContactAct.this, new DialogSelectContact.OnContactSelectListener() {
                @Override
                public void onSelected(Contact contact) {
                    blockContact(contact);
                }
            }).show(getSupportFragmentManager(), "");
        });
        binding.tvAdd.setOnClickListener(view -> {
            String str = binding.etPhoneNumber.getText().toString().trim();
            if (str != null && !str.isEmpty()) {
                List<BlockContact> objectRef = new GetBlockContactHelper(BlockContactAct.this).invoke();
                if (objectRef != null && !objectRef.isEmpty()) {
                    for (int i = 0; i < objectRef.size(); i++) {
                        if (PhoneNumberUtils.formatNumber(objectRef.get(i).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(i).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
                            Toast.makeText(BlockContactAct.this, R.string.toast_number_already_blocked, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                mBlockNumberViewModel.blockThisNumber(BlockContactAct.this, str);
                binding.etPhoneNumber.setText("");
            } else {
                Toast.makeText(this, getString(R.string.block_enter_valid_number), Toast.LENGTH_SHORT).show();
            }
        });
        adapterBlockNumber.setOnUnblockClick(new OnUnblockClick() {
            @Override
            public void onClick(String str) {
                mBlockNumberViewModel.unBlockThisNumber(BlockContactAct.this, str);
                mBlockNumberViewModel.getAllBlockNumber(BlockContactAct.this);
            }
        });

    }

    private ContactInfoViewModel mContactInfoViewModel;

    private void blockContact(Contact contact) {

        Log.e("fatal", "onChanged: " + "enter" );
        if (contact.getContactNumber().isEmpty()) {
            Log.e("fatal", "onChanged ContactId: " + contact.getContactId() );
            mContactInfoViewModel.refreshPhoneNumber(BlockContactAct.this, contact.getContactId());
            mContactInfoViewModel.getStateOfPhoneNumber().observe(this, new Observer<ArrayList<PhoneNumber>>() {
                @Override
                public void onChanged(ArrayList<PhoneNumber> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {
                        for (PhoneNumber t : arrayList) {
                            if (contact != null) {
                                if (contact.getContactNumber().isEmpty()) {
                                    contact.getContactNumber().add(t);
                                }
                            }
                        }


                        if (contact.getContactNumber() != null && contact.getContactNumber().size() > 0) {

                            int size = contact.getContactNumber().size();
                            for (int i = 0; i < size; i++) {
                                String str = contact.getContactNumber().get(i).getValue();
                                List<BlockContact> objectRef = new GetBlockContactHelper(BlockContactAct.this).invoke();
                                if (objectRef != null && objectRef.size() > 0) {
                                    for (int j = 0; j < objectRef.size(); j++) {
                                        if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
//                                        if (objectRef.get(j).getValue().equals(str) || objectRef.get(j).getNormalizedNumber().equals(str) || ("+91" + objectRef.get(j).getValue()).equals(str) || (objectRef.get(j).getValue().replace("+91", "").trim()).equals(str) || objectRef.get(j).getValue().equals(PhoneNumberUtils.formatNumber("+91" + str, "IN"))) {
                                            Toast.makeText(BlockContactAct.this, R.string.toast_number_already_blocked, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }

                                Log.e("fatal", "onChanged: " + "333" );
                                mBlockNumberViewModel.blockThisNumber(BlockContactAct.this, contact.getContactNumber().get(i).getValue());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BlockContactAct.this, getString(R.string.toast_block_contact), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }



                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BlockContactAct.this, getString(R.string.block_error_message), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


            return;
        }

        /*if (contact.getContactNumber().isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BlockContactAct.this, getString(R.string.block_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        } else */if (Build.VERSION.SDK_INT >= 24) {





//            ContentValues contentValues = new ContentValues();
            if (contact.getContactNumber() != null && contact.getContactNumber().size() > 0) {

                int size = contact.getContactNumber().size();
                for (int i = 0; i < size; i++) {
                    String str = contact.getContactNumber().get(i).getValue();
                    List<BlockContact> objectRef = new GetBlockContactHelper(BlockContactAct.this).invoke();
                    if (objectRef != null && objectRef.size() > 0) {
                        for (int j = 0; j < objectRef.size(); j++) {
                            if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
//                            if (objectRef.get(j).getValue().equals(str) || objectRef.get(j).getNormalizedNumber().equals(str) || ("+91" + objectRef.get(j).getValue()).equals(str) || (objectRef.get(j).getValue().replace("+91", "").trim()).equals(str) || objectRef.get(j).getValue().equals(PhoneNumberUtils.formatNumber("+91" + str, "IN"))) {
                                Toast.makeText(BlockContactAct.this, R.string.toast_number_already_blocked, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    mBlockNumberViewModel.blockThisNumber(BlockContactAct.this, contact.getContactNumber().get(i).getValue());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BlockContactAct.this, getString(R.string.toast_block_contact), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BlockContactAct.this, getString(R.string.block_not_supported), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void bindMethods() {

        binding.rcvBlockNumber.setLayoutManager(new GridLayoutManager(BlockContactAct.this, 1));
        binding.rcvBlockNumber.setAdapter(adapterBlockNumber);
        if (ContaxtExtUtils.isDefault(BlockContactAct.this)) {
            mBlockNumberViewModel.getAllBlockNumber(BlockContactAct.this);
        }
        mBlockNumberViewModel.getStateOfBlockNumber().observe(this, new Observer<List<BlockContact>>() {
            @Override
            public void onChanged(List<BlockContact> list) {
                if (!list.isEmpty()) {
                    binding.rcvBlockNumber.setVisibility(View.VISIBLE);
//                    binding.noData.setVisibility(View.GONE);
                } else {
                    binding.rcvBlockNumber.setVisibility(View.GONE);
//                    binding.noData.setVisibility(View.VISIBLE);
                }
                ArrayList arrayList = new ArrayList();
                for (BlockContact t : list) {
                    if (!Intrinsics.areEqual(t.getValue(), "")) {
                        arrayList.add(t);
                    }
                }
                adapterBlockNumber.setBlockList(arrayList);
            }
        });

    }

}
