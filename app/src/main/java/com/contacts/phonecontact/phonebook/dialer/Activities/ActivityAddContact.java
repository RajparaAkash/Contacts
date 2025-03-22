package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityAddContactBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAddEmail;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAddEvent;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAddPhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAddWebsite;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAddNewPhoneNumberFields;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnImageSelectionOptionSelect;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSavingAccountSelect;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogImageSelection;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.PopupSelectAccount;
import com.contacts.phonecontact.phonebook.dialer.types.EmailType;
import com.contacts.phonecontact.phonebook.dialer.types.EventType;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.FileExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.IntentExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.AddPhoneNumberViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.ranges.IntRange;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt;

public class ActivityAddContact extends ActBase<ActivityAddContactBinding> {

    public List<ContactSource> accountList = CollectionsKt.emptyList();
    public AddPhoneNumberViewModel mAddPhoneNumberViewModel;
    public AdapterAddEmail adapterAddEmail;
    public AdapterAddEvent adapterAddEvent;
    public AdapterAddPhoneNumber adapterAddPhoneNumber;
    public AdapterAddWebsite adapterAddWebsite;
    public ContactDatabase mContactDatabase;
    public int profileChange = -1;
    public String selectedAccountForSaving = "Phone storage";
    public int selectedAccountForSavingColor;
    public Contact selectedContact;
    ActivityAddContactBinding binding;
    private int bgcolor;
    private String img;
    private boolean isUpdate;
    private Uri lastPhotoIntentUri;
    ActivityResultLauncher<Intent> launcherCrop = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            if (activityResult.getResultCode() == -1) {
                profileChange = 0;
                Glide.with(ActivityAddContact.this).load(lastPhotoIntentUri).into(binding.btnUploadImage);
                img = String.valueOf(lastPhotoIntentUri);
            }
        }
    });
    ActivityResultLauncher<Intent> launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (activityResult.getResultCode() == -1) {
                startCropPhotoIntent(lastPhotoIntentUri, (activityResult == null || data == null) ? null : data.getData());
            }
        }
    });
    ActivityResultLauncher<Intent> launcherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            Intent data = activityResult.getData();
            if (activityResult.getResultCode() == -1) {
                startCropPhotoIntent(lastPhotoIntentUri, (activityResult == null || data == null) ? null : data.getData());
            }
        }
    });


    public void bindListeners8(final ActivityAddContact activityAddContact, final View view) {
        final String string = StringsKt.trim((CharSequence) String.valueOf(binding.edtNamePrefix.getText())).toString();
        final String string2 = StringsKt.trim((CharSequence) String.valueOf(binding.etFirstName.getText())).toString();
        final boolean b = true;
        final Iterable iterable = CollectionsKt.arrayListOf((Object[]) new String[]{string, string2, StringsKt.trim((CharSequence) String.valueOf(binding.edtMiddleName.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.etSurname.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.edtNameSuffix.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.etCompanyName.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.edtDepartment.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.etTitle.getText())).toString()});
        boolean b2 = false;
        Label_0325:
        {
            if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                final Iterator iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    if (((CharSequence) iterator.next()).length() != 0) {
                        b2 = false;
                        break Label_0325;
                    }
                }
            }
            b2 = true;
        }
        if (b2) {
            final Iterable iterable2 = mAddPhoneNumberViewModel.getPhoneNumberList();
            int n = 0;
            Label_0426:
            {
                if (iterable2 instanceof Collection && ((Collection) iterable2).isEmpty()) {
                    n = (b ? 1 : 0);
                } else {
                    final Iterator iterator2 = iterable2.iterator();
                    do {
                        n = (b ? 1 : 0);
                        if (iterator2.hasNext()) {
                            continue;
                        }
                        break Label_0426;
                    } while (!(Intrinsics.areEqual((Object) ((PhoneNumber) iterator2.next()).getValue(), (Object) "") ^ true));
                    n = 0;
                }
            }
            if (n != 0) {
                Toast.makeText((Context) activityAddContact, R.string.fields_empty, Toast.LENGTH_SHORT).show();
            } else {
                activityAddContact.createContact();
            }
        } else {
            activityAddContact.createContact();
        }
    }

    public void bindListeners15(ActivityAddContact activityAddContact, View view) {
        PopupSelectAccount popupSelectAccount = new PopupSelectAccount(activityAddContact);
        popupSelectAccount.setAccountList(activityAddContact.accountList);
        popupSelectAccount.setOnMenuItemClickListener(new OnSavingAccountSelect() {
            @Override
            public void onAccountSelect(String str, int i) {
                selectedAccountForSaving = str;
                binding.tvSelectedAccount.setText(selectedAccountForSaving);
                if (selectedAccountForSavingColor == -592138) {
                    binding.ivSelectedAccount.setColorFilter(ContextCompat.getColor(ActivityAddContact.this, R.color.gray_color_text));
                } else {
                    binding.ivSelectedAccount.setColorFilter(selectedAccountForSavingColor);
                }
                if (!selectedAccountForSaving.isEmpty()) {
                    binding.tvSelectedAccountFirstLetter.setText(String.valueOf(selectedAccountForSaving.charAt(0)));
                }
            }
        });
        popupSelectAccount.show(view);
    }

    @Override
    public void onContactUpdate() {
    }

    public final String getImg() {
        return this.img;
    }

    public final void setImg(String str) {
        this.img = str;
    }

    public final int getBgcolor() {
        return this.bgcolor;
    }

    public final void setBgcolor(int i) {
        this.bgcolor = i;
    }

    @Override
    public ActivityAddContactBinding setViewBinding() {
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {

        adapterAddPhoneNumber = new AdapterAddPhoneNumber(ActivityAddContact.this, binding.ivMobile, new AdapterAddPhoneNumber.OnMobileDetectListener() {
            @Override
            public void onDetected() {
                binding.llMobileFirst.setVisibility(View.GONE);
                binding.llMobileRecycler.setVisibility(View.VISIBLE);
                binding.viewAddNewField.setVisibility(View.VISIBLE);
                binding.tvAddNewField.setVisibility(View.VISIBLE);
            }
        });
        adapterAddEmail = new AdapterAddEmail(ActivityAddContact.this);
        adapterAddEvent = new AdapterAddEvent(ActivityAddContact.this);
        adapterAddWebsite = new AdapterAddWebsite(ActivityAddContact.this);
        mContactDatabase = ContactDatabase.Companion.invoke(ActivityAddContact.this);
        selectedAccountForSavingColor = ContextCompat.getColor(ActivityAddContact.this, R.color.gray);
        mAddPhoneNumberViewModel = (AddPhoneNumberViewModel) new ViewModelProvider(this).get(AddPhoneNumberViewModel.class);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mAddPhoneNumberViewModel.loadAllAccounts(mContactDatabase).observe(ActivityAddContact.this, new Observer<ArrayList<ContactSource>>() {
                    @Override
                    public void onChanged(ArrayList<ContactSource> arrayList) {
                        ActivityAddContact.this.accountList = arrayList;
                    }
                });
            }
        }, 200);
        binding.rcvPhoneNumber.setLayoutManager(new GridLayoutManager(ActivityAddContact.this, 1));
        binding.rcvPhoneNumber.setAdapter(adapterAddPhoneNumber);
        binding.rcvEmail.setLayoutManager(new GridLayoutManager(ActivityAddContact.this, 1));
        binding.rcvEmail.setAdapter(adapterAddEmail);
        binding.rcvEvent.setLayoutManager(new GridLayoutManager(ActivityAddContact.this, 1));
        binding.rcvEvent.setAdapter(adapterAddEvent);
        binding.rcvWebsite.setLayoutManager(new GridLayoutManager(ActivityAddContact.this, 1));
        binding.rcvWebsite.setAdapter(adapterAddWebsite);
    }

    private EditText focusedEditText;

    @Override
    public void bindListeners() {
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (isUpdate) {
            binding.tvHeading.setText(R.string.edit_contact);
            binding.ivSelectedAccountArrow.setVisibility(View.GONE);
        }
        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.cvCancel.setOnClickListener(view -> {
            finish();
        });
        binding.tvAddNewField.setOnClickListener(view -> {
            adapterAddPhoneNumber.addNewField(binding.rcvPhoneNumber);
            binding.ivMobile.setColorFilter(getColor(R.color.app_color));
            checkFocus();
        });


        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && view instanceof EditText) {
                    focusedEditText = (EditText) view;

                    checkFocus();

                    if (focusedEditText == binding.etFirstName || focusedEditText == binding.etSurname) {
                        binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
                    }
                    if (focusedEditText == binding.etCompanyName) {
                        binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    }
                    if (focusedEditText == binding.edtDepartment) {
                        binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    }
                    if (focusedEditText == binding.etTitle) {
                        binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    }
                    if (focusedEditText == binding.edtNotes) {
                        binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    }

                }
            }
        };
        binding.etFirstName.setOnFocusChangeListener(focusChangeListener);
        binding.etSurname.setOnFocusChangeListener(focusChangeListener);
        binding.etCompanyName.setOnFocusChangeListener(focusChangeListener);
        binding.edtDepartment.setOnFocusChangeListener(focusChangeListener);
        binding.etTitle.setOnFocusChangeListener(focusChangeListener);
        binding.edtNotes.setOnFocusChangeListener(focusChangeListener);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
                } else {
                    if (binding.etFirstName.hasFocus() || binding.etSurname.hasFocus()) {
                        binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
                    } else {
                        binding.ivFirstName.setColorFilter(Color.parseColor("#A1A1A1"));
                    }
                }
            }
        };

        binding.etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
                } else {
//                    if (binding.etFirstName.hasFocus() || binding.etSurname.hasFocus()) {
//                        binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
//                    } else {
//                        binding.ivFirstName.setColorFilter(Color.parseColor("#A1A1A1"));
//                    }
                    binding.etSurname.addTextChangedListener(textWatcher);
                }
            }
        });

        binding.etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                } else {
                    if (binding.etCompanyName.hasFocus()) {
                        binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    } else {
                        binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    }
                }
            }
        });
        binding.edtDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                } else {
                    if (binding.edtDepartment.hasFocus()) {
                        binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    } else {
                        binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    }
                }
            }
        });
        binding.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                } else {
                    if (binding.etTitle.hasFocus()) {
                        binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    } else {
                        binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    }
                }
            }
        });
        binding.edtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                } else {
                    if (binding.edtNotes.hasFocus()) {
                        binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
                    } else {
                        binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    }
                }
            }
        });


        adapterAddPhoneNumber.setOnFocusChangeListener(new AdapterAddPhoneNumber.OnFocusChangeListener() {
            @Override
            public void onChanged(boolean hasFocus) {
                checkFocus();
            }
        });
        adapterAddEmail.setOnFocusChangeListener(new AdapterAddEmail.OnFocusChangeListener() {
            @Override
            public void onChanged(boolean hasFocus) {
                checkFocus();
            }
        });
        adapterAddEvent.setOnFocusChangeListener(new AdapterAddEvent.OnFocusChangeListener() {
            @Override
            public void onChanged(boolean hasFocus) {
                checkFocus();
            }
        });
        adapterAddWebsite.setOnFocusChangeListener(new AdapterAddWebsite.OnFocusChangeListener() {
            @Override
            public void onChanged(boolean hasFocus) {
                checkFocus();
            }
        });

        binding.llMobileFirst.setOnClickListener(v -> {
            checkFocus();
            binding.ivMobile.setColorFilter(getColor(R.color.app_color));
            binding.llMobileFirst.setVisibility(View.GONE);
            binding.llMobileRecycler.setVisibility(View.VISIBLE);
            binding.viewAddNewField.setVisibility(View.VISIBLE);
            binding.tvAddNewField.setVisibility(View.VISIBLE);
            adapterAddPhoneNumber.focusToEdittext(binding.rcvPhoneNumber);
        });

        binding.btnSaveContact.setOnClickListener(view -> {

            String firstName = binding.etFirstName.getText().toString().trim();
//            String surName = binding.etSurname.getText().toString().trim();
            if (firstName != null && firstName.length() == 0) {
                Toast.makeText(this, R.string.toast_please_enter_firstname, Toast.LENGTH_SHORT).show();
                return;
            }
//            if (surName != null && surName.length() == 0) {
//                Toast.makeText(this, R.string.toast_please_enter_surname, Toast.LENGTH_SHORT).show();
//                return;
//            }
            List<PhoneNumber> phoneNo = adapterAddPhoneNumber.getEnteredPhoneNumber();
            if (phoneNo != null && phoneNo.size() > 0) {
                for (int i = 0; i < phoneNo.size(); i++) {
//                    if (phoneNo.get(i).getValue().length() < 10 || phoneNo.get(i).getValue().length() > 16) {
                    if (phoneNo.get(i).getValue().length() <= 0) {
                        Toast.makeText(this, R.string.toast_please_enter_valid_phone_no, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            List<Email> eMail = adapterAddEmail.getEnteredEmail();
            if (eMail != null && eMail.size() > 0) {
                for (int i = 0; i < eMail.size(); i++) {
                    if (eMail.get(i).getValue().length() > 0) {
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!eMail.get(i).getValue().matches(emailPattern)) {
                            Toast.makeText(this, R.string.toast_please_enter_valid_email, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }


//            List<PhoneNumber> phoneNoList = adapterAddPhoneNumber.getEnteredPhoneNumber();
//            if (phoneNoList != null && phoneNoList.size() > 0) {
//                Log.e("fatal", "save contact 111: " + phoneNoList.size() );
//                for (int i = 0; i < phoneNoList.size(); i++) {
//
//                    List<PhoneNumber> numberList = mAddPhoneNumberViewModel.getPhoneNumberList();
//                    if (numberList != null && numberList.size() > 0) {
//                        Log.e("fatal", "save contact 222: " + numberList.size() );
////                        for (int j = 0; j < numberList.size(); j++) {
////
////                            if (!phoneNoList.get(i).getValue().equalsIgnoreCase(numberList.get(j).getValue())) {
////                                mAddPhoneNumberViewModel.getPhoneNumberList().add(phoneNoList.get(i));
////                            }
////
////                        }
//                    }
//
//                }
//            }0

            bindListeners8(ActivityAddContact.this, view);
        });
        binding.nameArrow.setOnClickListener(view -> {
//            if (binding.llNamePrefix.getVisibility() == View.VISIBLE) {
//                ViewExtensionUtils.gone(binding.llNamePrefix);
//                ViewExtensionUtils.gone(binding.llMiddleName);
//                ViewExtensionUtils.gone(binding.llNameSuffix);
//                binding.nameArrow.animate().rotation(0.0f);
//            } else {
//                ViewExtensionUtils.show(binding.llNamePrefix);
//                ViewExtensionUtils.show(binding.llMiddleName);
//                ViewExtensionUtils.show(binding.llNameSuffix);
//                binding.nameArrow.animate().rotation(-180.0f);
//            }

            if (binding.llSurname.getVisibility() == View.VISIBLE) {
                ViewExtensionUtils.gone(binding.llSurname);
                binding.nameArrow.animate().rotation(0.0f);
            } else {
                ViewExtensionUtils.show(binding.llSurname);
                binding.nameArrow.animate().rotation(-180.0f);
            }
        });
        adapterAddPhoneNumber.setONNewPhoneNumberFiledAdded(new OnAddNewPhoneNumberFields() {
            @Override
            public void onAdd(int i) {
                mAddPhoneNumberViewModel.initPhoneNumber(ActivityAddContact.this);
                List<PhoneNumber> phoneNumberList = mAddPhoneNumberViewModel.getPhoneNumberList();
                List<PhoneNumberType> addedPhoneNumber = mAddPhoneNumberViewModel.getAddedPhoneNumber();
                adapterAddPhoneNumber.setPhoneList(phoneNumberList, addedPhoneNumber, mAddPhoneNumberViewModel.getNextFieldAddedPosition());
            }
        });
        adapterAddEmail.setONNewPhoneNumberFiledAdded(new OnAddNewPhoneNumberFields() {
            @Override
            public void onAdd(int i) {
                mAddPhoneNumberViewModel.initEmail(ActivityAddContact.this);
                List<Email> emailList = mAddPhoneNumberViewModel.getEmailList();
                List<EmailType> addedEmail = mAddPhoneNumberViewModel.getAddedEmail();
                adapterAddEmail.setPhoneList(emailList, addedEmail, mAddPhoneNumberViewModel.getNextFieldAddedPositionEmail());
            }
        });
        adapterAddEvent.setONNewPhoneNumberFiledAdded(new OnAddNewPhoneNumberFields() {
            @Override
            public void onAdd(int i) {
                mAddPhoneNumberViewModel.initEvent();
                List<Event> eventList = mAddPhoneNumberViewModel.getEventList();
                List<EventType> addedEvent = mAddPhoneNumberViewModel.getAddedEvent();
                adapterAddEvent.setPhoneList(eventList, addedEvent, mAddPhoneNumberViewModel.getNextFieldAddedPositionEvent());
            }
        });
        adapterAddWebsite.setONNewPhoneNumberFiledAdded(new OnAddNewPhoneNumberFields() {
            @Override
            public void onAdd(int i) {
                mAddPhoneNumberViewModel.initWebsite();
                List<String> websiteList = mAddPhoneNumberViewModel.getWebsiteList();
                adapterAddWebsite.setPhoneList(websiteList, mAddPhoneNumberViewModel.getNextFieldAddedPositionWebsite());
            }
        });
        binding.tvMoreFields.setOnClickListener(view -> {
            ViewExtensionUtils.gone(binding.tvMoreFields);
            ViewExtensionUtils.show(binding.layoutMoreFields);
            ViewExtensionUtils.show(binding.layoutMoreCompany);
        });
        binding.btnUploadImage.setOnClickListener(view -> {
            DialogImageSelection dialogImageSelection = new DialogImageSelection(ActivityAddContact.this, getImg());
            dialogImageSelection.setListener(new OnImageSelectionOptionSelect() {
                @Override
                public void onRemovePhoto() {
                    profileChange = 1;
                    setImg(null);
                    binding.btnUploadImage.setImageResource(R.drawable.add_contact_icon_photo);
                }

                @Override
                public void onSelectPhoto() {
                    startChoosePhotoIntent();
                }

                @Override
                public void onTakeNewPhoto() {
                    captureImage();
                }
            });
            dialogImageSelection.show();
        });


        binding.layoutSelectedAccount.setOnClickListener(view -> {
            bindListeners15(ActivityAddContact.this, view);
        });

    }

    public void checkFocus() {
        String firstName = binding.etFirstName.getText().toString().trim();
        String surName = binding.etSurname.getText().toString().trim();
        if (firstName.length() > 0 || surName.length() > 0) {
            binding.ivFirstName.setColorFilter(getColor(R.color.app_color));
        } else {
            binding.ivFirstName.setColorFilter(Color.parseColor("#A1A1A1"));
        }

        String companyName = binding.etCompanyName.getText().toString().trim();
        if (companyName.length() > 0) {
            binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
        } else {
            binding.etCompanyName.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        }

        String department = binding.edtDepartment.getText().toString().trim();
        if (department.length() > 0) {
            binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
        } else {
            binding.edtDepartment.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        }

        String title = binding.etTitle.getText().toString().trim();
        if (title.length() > 0) {
            binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
        } else {
            binding.etTitle.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        }

        String notes = binding.edtNotes.getText().toString().trim();
        if (notes.length() > 0) {
            binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.app_color)));
        } else {
            binding.edtNotes.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        }
    }

    public void startChoosePhotoIntent() {
        lastPhotoIntentUri = FileExtUtils.getCachePhotoUri$default(ActivityAddContact.this, null, 1, null);
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        intent.setClipData(new ClipData("Attachment", new String[]{"text/uri-list"}, new ClipData.Item(lastPhotoIntentUri)));
        intent.addFlags(3);
        intent.putExtra("output", lastPhotoIntentUri);
        intent.putExtra("return-data", true);
        try {
            launcherGallery.launch(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(ActivityAddContact.this, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void startCropPhotoIntent(Uri uri, Uri uri2) {
        if (uri == null) {
            Toast.makeText(ActivityAddContact.this, R.string.unknown_error_occurred, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (MediaStore.Images.Media.getBitmap(getContentResolver(), uri) == null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                    if (bitmap != null) {
                        File cachePhoto = FileExtUtils.getCachePhoto(this);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cachePhoto));
                        uri = FileExtUtils.getCachePhotoUri(this, cachePhoto);
                    } else {
                        return;
                    }
                } catch (Exception exception) {
                    return;
                }
            }
            lastPhotoIntentUri = FileExtUtils.getCachePhotoUri$default(this, null, 1, null);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("output", lastPhotoIntentUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", "true");
            intent.putExtra("scaleUpIfNeeded", "true");
            intent.setClipData(new ClipData("Attachment", new String[]{"text/primaryUri-list"}, new ClipData.Item(lastPhotoIntentUri)));
            intent.addFlags(3);
            try {
                launcherCrop.launch(intent);
            } catch (ActivityNotFoundException exception) {
                Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("fatal3", "startCropPhotoIntent 444: " + e.getMessage() );
        }
    }

    private void createContact() {
        if (!isUpdate) {
            Contact contact = getContact();
            mAddPhoneNumberViewModel.saveContact(contact, mContactDatabase, this).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer num) {
                    System.out.println((Object) ("contact creation : " + num));
                    Contact access$getContact = getContact();
                    access$getContact.setContactId(num.intValue());
                    access$getContact.setContactIdSimple(num.intValue());
                    Intent intent = new Intent(ActivityAddContact.this, ActivityContactInformation.class);
                    intent.putExtra("selectedContact", access$getContact);
                    startActivity(intent);
                    finish();
                }
            });
            return;
        }
        mAddPhoneNumberViewModel.updateContact(getContact(), profileChange, this).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(ActivityAddContact.this, ActivityContactInformation.class);
                intent.putExtra("selectedContact", getContact());
                intent.putExtra("isStared", getContact().getContactIsStared());
                setResult(-1, intent);
                finish();
            }
        });
    }

    @Override
    public void bindMethods() {
        String str = "";
        Integer num = 0;
        int i;
        if (Intrinsics.areEqual(getIntent().getAction(), "android.intent.action.INSERT")) {
            Intent intent = getIntent();
            String valueOf = String.valueOf(IntentExtUtils.getPhoneNumberFromIntent(intent));
            PhoneNumberType phoneNumberType = PhoneNumberType.NO_LABEL;
            Intent intent2 = getIntent();
            PhoneNumber phoneNumber = new PhoneNumber(valueOf, phoneNumberType, getString(R.string.title_no_lable), String.valueOf(IntentExtUtils.getPhoneNumberFromIntent(intent2)), null, 16, null);
            mAddPhoneNumberViewModel.getPhoneNumberList().add(phoneNumber);
            mAddPhoneNumberViewModel.getAddedPhoneNumber().add(PhoneNumberType.NO_LABEL);
        }


        SharedPreferences contactAppPreference = ContaxtExtUtils.getContactAppPreference(ActivityAddContact.this);
        String string = "KeyDefaultAccountForNewContact";
        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(String.class);
        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(String.class))) {
            str = contactAppPreference.getString(string, "Phone storage");
        }
        selectedAccountForSaving = str;
        String string2 = "selectedAccountForSavingColor";
        int valueOf2 = ContextCompat.getColor(ActivityAddContact.this, R.color.gray);
        KClass orCreateKotlinClass2 = Reflection.getOrCreateKotlinClass(Integer.class);
        if (Intrinsics.areEqual(orCreateKotlinClass2, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
            num = contactAppPreference.getInt(string2, valueOf2);
        }
        selectedAccountForSavingColor = num;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Contact contact = (Contact) extras.getSerializable("selectedContact");
            this.selectedContact = contact;
            if (contact != null) {

                if (contact.getContactSource() != null) {
                    binding.layoutSelectedAccount.setEnabled(false);
                    if (contact.getContactSource().equals("vnd.sec.contact.phone") || contact.getContactSource().equals("Phone storage")) {
                        selectedAccountForSaving = "Phone storage";
                    } else {
                        if (!contact.getContactSource().isEmpty()) {
                            selectedAccountForSaving = contact.getContactSource();
                        } else {
                            selectedAccountForSaving = str;
                        }
                    }
                } else {
                    selectedAccountForSaving = str;
                }

                isUpdate = getIntent().getBooleanExtra("isUpdate", false);
                if (isUpdate) {
                    binding.layoutSelectedAccount.setEnabled(false);
                } else {
                    binding.layoutSelectedAccount.setEnabled(true);
                }

                binding.etFirstName.setText(contact.getFirstName());
                binding.edtMiddleName.setText(contact.getMiddleName());
                binding.etSurname.setText(contact.getSurName());
                binding.edtNamePrefix.setText(contact.getNamePrefix());
                binding.edtNameSuffix.setText(contact.getNameSuffix());
                binding.etCompanyName.setText(contact.getCompany());
                binding.edtDepartment.setText(contact.getJobPosition());
                binding.etTitle.setText(contact.getJobTitle());
                if (contact.getContactNotes().size() > 0) {
                    binding.edtNotes.setText(contact.getContactNotes().get(0));
                }
                List<PhoneNumberType> addedPhoneNumber = mAddPhoneNumberViewModel.getAddedPhoneNumber();
                ArrayList<PhoneNumber> contactNumber = contact.getContactNumber();
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(contactNumber, 10));
                Iterator<PhoneNumber> it = contactNumber.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().getType());
                }
                addedPhoneNumber.addAll(arrayList);
                mAddPhoneNumberViewModel.getNextFieldAddedPosition().addAll(CollectionsKt.toList(new IntRange(0, contact.getContactNumber().size())));
                mAddPhoneNumberViewModel.getPhoneNumberList().addAll(contact.getContactNumber());
                List<EmailType> addedEmail = mAddPhoneNumberViewModel.getAddedEmail();
                ArrayList<Email> contactEmail = contact.getContactEmail();
                ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(contactEmail, 10));
                Iterator<Email> it2 = contactEmail.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(it2.next().getType());
                }
                addedEmail.addAll(arrayList2);
                mAddPhoneNumberViewModel.getNextFieldAddedPositionEmail().addAll(CollectionsKt.toList(new IntRange(0, contact.getContactEmail().size())));
                mAddPhoneNumberViewModel.getEmailList().addAll(contact.getContactEmail());
                List<EventType> addedEvent = mAddPhoneNumberViewModel.getAddedEvent();
                ArrayList<Event> contactEvent = contact.getContactEvent();
                ArrayList arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(contactEvent, 10));
                Iterator<Event> it3 = contactEvent.iterator();
                while (it3.hasNext()) {
                    arrayList3.add(it3.next().getType());
                }
                addedEvent.addAll(arrayList3);
                mAddPhoneNumberViewModel.getNextFieldAddedPositionEvent().addAll(CollectionsKt.toList(new IntRange(0, contact.getContactEvent().size())));
                mAddPhoneNumberViewModel.getEventList().addAll(contact.getContactEvent());
                mAddPhoneNumberViewModel.getNextFieldAddedPositionWebsite().addAll(CollectionsKt.toList(new IntRange(0, contact.getWebsites().size())));
                mAddPhoneNumberViewModel.getWebsiteList().addAll(contact.getWebsites());
                if (contact.getBgColor() != null) {
                    i = contact.getBgColor().intValue();
                } else {
                    i = ContextCompat.getColor(this, R.color.app_color);
                }
                this.bgcolor = i;
                Glide.with((FragmentActivity) this).load(contact.getContactPhotoUri()).placeholder(R.drawable.add_contact_icon_photo).into(binding.btnUploadImage);
//                Log.e("vddvdvdv", String.valueOf(contact.getContactPhotoUri()));
                if (contact.getContactPhotoUri() == null) {
                    this.profileChange = 1;
                    this.img = null;
                    binding.btnUploadImage.setImageResource(R.drawable.add_contact_icon_photo);
                } else {
                    this.img = contact.getContactPhotoUri().toString();
                }
            }
        }


        binding.tvSelectedAccount.setText(selectedAccountForSaving);
        binding.ivSelectedAccount.setColorFilter(selectedAccountForSavingColor);
        try {
            if (!selectedAccountForSaving.isEmpty()) {
                binding.tvSelectedAccountFirstLetter.setText(String.valueOf(selectedAccountForSaving.charAt(0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getSavedInstanceState() == null) {
            if (mAddPhoneNumberViewModel.getPhoneNumberList().isEmpty()) {
                mAddPhoneNumberViewModel.initPhoneNumber(ActivityAddContact.this);
            }
            List<PhoneNumber> phoneNumberList = mAddPhoneNumberViewModel.getPhoneNumberList();
            List<PhoneNumberType> addedPhoneNumber2 = mAddPhoneNumberViewModel.getAddedPhoneNumber();
            adapterAddPhoneNumber.setPhoneList(phoneNumberList, addedPhoneNumber2, mAddPhoneNumberViewModel.getNextFieldAddedPosition());
            if (mAddPhoneNumberViewModel.getEmailList().isEmpty()) {
                mAddPhoneNumberViewModel.initEmail(ActivityAddContact.this);
            }
            List<Email> emailList = mAddPhoneNumberViewModel.getEmailList();
            List<EmailType> addedEmail2 = mAddPhoneNumberViewModel.getAddedEmail();
            adapterAddEmail.setPhoneList(emailList, addedEmail2, mAddPhoneNumberViewModel.getNextFieldAddedPositionEmail());
            if (mAddPhoneNumberViewModel.getEventList().isEmpty()) {
                mAddPhoneNumberViewModel.initEvent();
            }
            List<Event> eventList = mAddPhoneNumberViewModel.getEventList();
            List<EventType> addedEvent2 = mAddPhoneNumberViewModel.getAddedEvent();
            adapterAddEvent.setPhoneList(eventList, addedEvent2, mAddPhoneNumberViewModel.getNextFieldAddedPositionEvent());
            if (mAddPhoneNumberViewModel.getWebsiteList().isEmpty()) {
                mAddPhoneNumberViewModel.initWebsite();
            }
            List<String> websiteList = mAddPhoneNumberViewModel.getWebsiteList();
            adapterAddWebsite.setPhoneList(websiteList, mAddPhoneNumberViewModel.getNextFieldAddedPositionWebsite());
            return;
        }
        List<PhoneNumber> phoneNumberList2 = mAddPhoneNumberViewModel.getPhoneNumberList();
        List<PhoneNumberType> addedPhoneNumber3 = mAddPhoneNumberViewModel.getAddedPhoneNumber();
        adapterAddPhoneNumber.setPhoneList(phoneNumberList2, addedPhoneNumber3, mAddPhoneNumberViewModel.getNextFieldAddedPosition());
        List<Email> emailList2 = mAddPhoneNumberViewModel.getEmailList();
        List<EmailType> addedEmail3 = mAddPhoneNumberViewModel.getAddedEmail();
        adapterAddEmail.setPhoneList(emailList2, addedEmail3, mAddPhoneNumberViewModel.getNextFieldAddedPositionEmail());
        List<Event> eventList2 = mAddPhoneNumberViewModel.getEventList();
        List<EventType> addedEvent3 = mAddPhoneNumberViewModel.getAddedEvent();
        adapterAddEvent.setPhoneList(eventList2, addedEvent3, mAddPhoneNumberViewModel.getNextFieldAddedPositionEvent());
        List<String> websiteList2 = mAddPhoneNumberViewModel.getWebsiteList();
        adapterAddWebsite.setPhoneList(websiteList2, mAddPhoneNumberViewModel.getNextFieldAddedPositionWebsite());

    }

    public void captureImage() {
        lastPhotoIntentUri = FileExtUtils.getPhotoUri$default(ActivityAddContact.this, null, 1, null);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", lastPhotoIntentUri);
        try {
            launcherCamera.launch(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(ActivityAddContact.this, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            Toast.makeText(ActivityAddContact.this, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }

    public Contact getContact() {
        Contact contact;
        if (selectedContact == null) {
            final Iterable iterable = mAddPhoneNumberViewModel.getPhoneNumberList();
            boolean b = false;
            Label_0122:
            {
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    final Iterator iterator = iterable.iterator();
                    while (iterator.hasNext()) {
                        if (Intrinsics.areEqual((Object) ((PhoneNumber) iterator.next()).getValue(), (Object) "") ^ true) {
                            b = true;
                            break Label_0122;
                        }
                    }
                }
                b = false;
            }
            List list;
            if (b) {
                final Iterable iterable2 = mAddPhoneNumberViewModel.getPhoneNumberList();
                final Collection collection = new ArrayList();
                for (final Object next : iterable2) {
                    if (Intrinsics.areEqual((Object) ((PhoneNumber) next).getValue(), (Object) "") ^ true) {
                        collection.add(next);
                    }
                }
                list = (List) collection;
            } else {
                list = new ArrayList();
            }
            final Iterable iterable3 = mAddPhoneNumberViewModel.getEmailList();
            boolean b2 = false;
            Label_0359:
            {
                if (!(iterable3 instanceof Collection) || !((Collection) iterable3).isEmpty()) {
                    final Iterator iterator3 = iterable3.iterator();
                    while (iterator3.hasNext()) {
                        if (Intrinsics.areEqual((Object) ((Email) iterator3.next()).getValue(), (Object) "") ^ true) {
                            b2 = true;
                            break Label_0359;
                        }
                    }
                }
                b2 = false;
            }
            List list2;
            if (b2) {
                final Iterable iterable4 = mAddPhoneNumberViewModel.getEmailList();
                final Collection collection2 = new ArrayList();
                for (final Object next2 : iterable4) {
                    if (Intrinsics.areEqual((Object) ((Email) next2).getValue(), (Object) "") ^ true) {
                        collection2.add(next2);
                    }
                }
                list2 = (List) collection2;
            } else {
                list2 = new ArrayList();
            }
            final Iterable iterable5 = mAddPhoneNumberViewModel.getEventList();
            boolean b3 = false;
            Label_0596:
            {
                if (!(iterable5 instanceof Collection) || !((Collection) iterable5).isEmpty()) {
                    final Iterator iterator5 = iterable5.iterator();
                    while (iterator5.hasNext()) {
                        if (Intrinsics.areEqual((Object) ((Event) iterator5.next()).getValue(), (Object) "") ^ true) {
                            b3 = true;
                            break Label_0596;
                        }
                    }
                }
                b3 = false;
            }
            List list3;
            if (b3) {
                final Iterable iterable6 = mAddPhoneNumberViewModel.getEventList();
                final Collection collection3 = new ArrayList();
                for (final Object next3 : iterable6) {
                    if (Intrinsics.areEqual((Object) ((Event) next3).getValue(), (Object) "") ^ true) {
                        collection3.add(next3);
                    }
                }
                list3 = (List) collection3;
            } else {
                list3 = new ArrayList();
            }
            final Iterable iterable7 = mAddPhoneNumberViewModel.getWebsiteList();
            boolean b4 = false;
            Label_0830:
            {
                if (!(iterable7 instanceof Collection) || !((Collection) iterable7).isEmpty()) {
                    final Iterator iterator7 = iterable7.iterator();
                    while (iterator7.hasNext()) {
                        if (Intrinsics.areEqual((Object) iterator7.next(), (Object) "") ^ true) {
                            b4 = true;
                            break Label_0830;
                        }
                    }
                }
                b4 = false;
            }
            List list4;
            if (b4) {
                final Iterable iterable8 = mAddPhoneNumberViewModel.getWebsiteList();
                final Collection collection4 = new ArrayList();
                for (final Object next4 : iterable8) {
                    if (Intrinsics.areEqual((Object) next4, (Object) "") ^ true) {
                        collection4.add(next4);
                    }
                }
                list4 = (List) collection4;
            } else {
                list4 = new ArrayList();
            }
            final String string = StringsKt.trim((CharSequence) String.valueOf(binding.etFirstName.getText())).toString();
            final String string2 = StringsKt.trim((CharSequence) String.valueOf(binding.etSurname.getText())).toString();
            final String string3 = StringsKt.trim((CharSequence) String.valueOf(binding.edtNamePrefix.getText())).toString();
            final String string4 = StringsKt.trim((CharSequence) String.valueOf(binding.edtNameSuffix.getText())).toString();
            final String string5 = StringsKt.trim((CharSequence) String.valueOf(binding.edtMiddleName.getText())).toString();
            contact = new Contact((Integer) null, 0, 0, string3, string, "", string5, string2, string4, img, "", selectedContact != null && selectedContact.getContactIsStared(), "", (ArrayList) list, (ArrayList) list2, (ArrayList) list3, new ArrayList(), (ArrayList) list4, new ArrayList(), this.selectedAccountForSaving, CollectionsKt.arrayListOf((Object[]) new String[]{StringsKt.trim((CharSequence) String.valueOf(binding.edtNotes.getText())).toString()}), StringsKt.trim((CharSequence) String.valueOf(binding.etCompanyName.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.edtDepartment.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.etTitle.getText())).toString(), (String) null, this.bgcolor, 16777217, (DefaultConstructorMarker) null);
        } else {
            int contactId;
            if (selectedContact != null) {
                contactId = selectedContact.getContactId();
            } else {
                contactId = 0;
            }
            int contactIdSimple;
            if (selectedContact != null) {
                contactIdSimple = selectedContact.getContactIdSimple();
            } else {
                contactIdSimple = 0;
            }
            final String string6 = StringsKt.trim(String.valueOf(binding.etFirstName.getText())).toString();
            final String string7 = StringsKt.trim(String.valueOf(binding.etSurname.getText())).toString();
            final String string8 = StringsKt.trim(String.valueOf(binding.edtNamePrefix.getText())).toString();
            final String string9 = StringsKt.trim(String.valueOf(binding.edtNameSuffix.getText())).toString();
            final String string10 = StringsKt.trim(String.valueOf(binding.edtMiddleName.getText())).toString();
            final boolean b5 = selectedContact != null && selectedContact.getContactIsStared();
            final List phoneNumberList = mAddPhoneNumberViewModel.getPhoneNumberList();
            final ArrayList list5 = (ArrayList) phoneNumberList;
            final List emailList = mAddPhoneNumberViewModel.getEmailList();
            final ArrayList list6 = (ArrayList) emailList;
            final List eventList = mAddPhoneNumberViewModel.getEventList();
            final ArrayList list7 = (ArrayList) eventList;
            final ArrayList list8 = new ArrayList();
            final List websiteList = mAddPhoneNumberViewModel.getWebsiteList();
            contact = new Contact((Integer) null, contactId, contactIdSimple, string8, string6, "", string10, string7, string9, img, "", b5, "", list5, list6, list7, list8, (ArrayList) websiteList, new ArrayList(), this.selectedAccountForSaving, CollectionsKt.arrayListOf((Object[]) new String[]{StringsKt.trim((CharSequence) String.valueOf(binding.edtNotes.getText())).toString()}), StringsKt.trim((CharSequence) String.valueOf(binding.etCompanyName.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.edtDepartment.getText())).toString(), StringsKt.trim((CharSequence) String.valueOf(binding.etTitle.getText())).toString(), (String) null, bgcolor, 16777217, (DefaultConstructorMarker) null);
        }
        return contact;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
            finish();
        });
    }

}
