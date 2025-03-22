package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.UserContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityCallerIdBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.MyContactsContentProvider;
import com.contacts.phonecontact.phonebook.dialer.DialerCode.recievers.CallStateReceiver;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactInfoViewModel;

import java.util.ArrayList;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class ActivityCallerId extends ActBase<ActivityCallerIdBinding> {

    public static final Companion Companion = new Companion(null);
    public static ActivityCallerId activity;
    public Animation animation;
    ActivityCallerIdBinding binding;
    private UserContact contact;
    private boolean isBlock;
    private boolean isSaveContact;
    private ContactInfoViewModel mContactInfoViewModel;
    private String phoneNumber = "";
    private Contact selectedContact;

    @Override
    public void bindMethods() {
    }

    @Override
    public void onContactUpdate() {
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public UserContact getContact() {
        return this.contact;
    }

    public void setContact(UserContact userContact) {
        this.contact = userContact;
    }

    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    public void setSelectedContact(Contact contact2) {
        this.selectedContact = contact2;
    }

    public boolean isSaveContact() {
        return this.isSaveContact;
    }

    public void setSaveContact(boolean z) {
        this.isSaveContact = z;
    }

    public Animation getAnimation() {
        if (animation != null) {
            return animation;
        }
        return null;
    }

    public void setAnimation(Animation animation2) {
        this.animation = animation2;
    }

    @Override
    public ActivityCallerIdBinding setViewBinding() {
        overridePendingTransition(R.anim.slide_left, R.anim.no_anim);
        binding = ActivityCallerIdBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        String str;
        Companion.setActivity(this);
        CallStateReceiver.isShowCaller = false;
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        setAnimation(loadAnimation);
        getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finishAndRemoveTask();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContactInfoViewModel = (ContactInfoViewModel) new ViewModelProvider(this).get(ContactInfoViewModel.class);
        if (!(getIntent() == null || getIntent().getStringExtra(ConstantsUtils.PHONE_NUMBER) == null)) {
            phoneNumber = getIntent().getStringExtra(ConstantsUtils.PHONE_NUMBER);
        }
        boolean z = true;
        if (!(phoneNumber == null || phoneNumber.length() == 0)) {
            contact = getContact(ActivityCallerId.this, this.phoneNumber);
            if (contact == null) {
                this.isSaveContact = false;
                this.contact = new UserContact(0, "", "", "");
            } else {
                if (contact.getContactId() == 0) {
                    this.isSaveContact = false;
                } else {
                    this.isSaveContact = true;
                }
            }
            if (contact.getContactId() != 0) {
                ContactDatabase invoke = ContactDatabase.Companion.invoke(ActivityCallerId.this);
                mContactInfoViewModel.getContactBySimpleId(invoke, String.valueOf(contact.getContactId()));
                mContactInfoViewModel.getStateOfContactById().observe(this, new Observer<Contact>() {
                    @Override
                    public void onChanged(Contact contact) {
                        if (contact != null) {
                            setSelectedContact(contact);
                        }
                    }
                });
            }
            String nameSuffix = contact.getNameSuffix();
            if (nameSuffix == null || nameSuffix.length() == 0) {
                str = getString(R.string.unknown);
            } else {
                str = contact.getNameSuffix();
            }
            binding.contactName.setText(str);
            binding.contactNumber.setText(PhoneNumberUtils.formatNumber(phoneNumber, "IN"));
            binding.profileImageLayout.setVisibility(View.GONE);
            binding.ivAddContact.setVisibility(View.GONE);
            binding.profileImage.setVisibility(View.VISIBLE);
            binding.ivProfileSmall.setColorFilter(ContextCompat.getColor(ActivityCallerId.this, R.color.theme_light_color));
            String contactPhotoUri = contact.getContactPhotoUri();
            if (!(contactPhotoUri == null || contactPhotoUri.length() == 0)) {
                Glide.with(ActivityCallerId.this).load(contact.getContactPhotoUri()).into(binding.profileImage);
                return;
            }
            String contactPhotoThumbUri = contact.getContactPhotoThumbUri();
            if (!(contactPhotoThumbUri == null || contactPhotoThumbUri.length() == 0)) {
                Glide.with(ActivityCallerId.this).load(contact.getContactPhotoThumbUri()).into(binding.profileImage);
                return;
            }
            if (contact != null) {
                String nameSuffix2 = contact.getNameSuffix();
                if (!(nameSuffix2 == null || nameSuffix2.length() == 0)) {
                    binding.profileImageLayout.setVisibility(View.VISIBLE);
                    binding.profileImage.setVisibility(View.GONE);
                    binding.ivAddContact.setVisibility(View.GONE);
                    String obj = StringsKt.trim((CharSequence) contact.getNameSuffix()).toString();
                    if (obj.length() <= 0) {
                        z = false;
                    }
                    if (z) {
                        binding.tvFirstLetter.setText(String.valueOf(obj.charAt(0)));
                        return;
                    }
                    return;
                }
            }
            binding.profileImageLayout.setVisibility(View.VISIBLE);
            binding.ivAddContact.setVisibility(View.VISIBLE);
            binding.profileImage.setVisibility(View.GONE);
            return;
        }
        binding.contactName.setText(getString(R.string.unknown));
        binding.contactNumber.setVisibility(View.INVISIBLE);
        binding.ivAddContact.setVisibility(View.VISIBLE);
        binding.profileImageLayout.setVisibility(View.VISIBLE);
        binding.profileImage.setVisibility(View.GONE);
        binding.ivProfileSmall.setColorFilter(ContextCompat.getColor(this, R.color.theme_light_color));
    }

    @Override
    public void bindListeners() {
        binding.mainLayout.setOnClickListener(view -> {
        });
        binding.getRoot().setOnClickListener(view -> {
            CallStateReceiver.isShowCaller = false;
            binding.mainLayout.startAnimation(getAnimation());
        });
        binding.actionClose.setOnClickListener(view -> {
            CallStateReceiver.isShowCaller = false;
            binding.mainLayout.startAnimation(getAnimation());
        });
    }

    private UserContact getContact(Context context, String str) {
        UserContact userContact = new UserContact(0, "", "", "");
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", MyContactsContentProvider.COL_PHOTO_URI, "photo_thumb_uri", "contact_id"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                String string = query.getString(query.getColumnIndexOrThrow("display_name"));
                String string2 = query.getString(query.getColumnIndexOrThrow(MyContactsContentProvider.COL_PHOTO_URI));
                String string3 = query.getString(query.getColumnIndexOrThrow("photo_thumb_uri"));
                int i = query.getInt(query.getColumnIndexOrThrow("contact_id"));
                userContact = new UserContact(i, string, string2, string3);
            }
            if (!query.isClosed()) {
                query.close();
            }
            return userContact;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userContact;
    }

    private Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "", null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", null, Integer.valueOf(ContextCompat.getColor(this, R.color.app_color)), 16777217, null);
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public ActivityCallerId getActivity() {
            ActivityCallerId callerIdActivity = ActivityCallerId.activity;
            if (callerIdActivity != null) {
                return callerIdActivity;
            }
            Intrinsics.throwUninitializedPropertyAccessException("activity");
            return null;
        }

        public void setActivity(ActivityCallerId callerIdActivity) {
            ActivityCallerId.activity = callerIdActivity;
        }
    }

}
