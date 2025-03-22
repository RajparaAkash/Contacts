package com.contacts.phonecontact.phonebook.dialer.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterEmails;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterEvents;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterNotes;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterPhoneNumbers;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSelectedCallLog;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterWebsites;
import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Event;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.BuildConfig;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.observer.ObserveContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogCommon;
import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogSelectRingtone;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ReadContact;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.CallLogViewModel;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactInfoViewModel;
import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityContactInformationBinding;
import com.contacts.phonecontact.phonebook.dialer.types.EmailType;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

public class ActivityContactInformation extends ActBase<ActivityContactInformationBinding> {

    public boolean isBlock;
    public AdapterPhoneNumbers adapterPhoneNumbers;
    public Contact selectedContact;
    public ArrayList<ObjectCallLog> callList = new ArrayList<>();
    public AdapterSelectedCallLog adapterSelectedCallLog;
    public CallLogViewModel mCallLogViewModel;
    public ContactDatabase mDatabase;
    ActivityContactInformationBinding binding;
    String contactName = "";
    ActivityResultLauncher<Intent> launcherMakeDefaultApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });

    private boolean isEditShow = true;
    private boolean isVideoCall;
    private AdapterEmails adapterEmails;
    private AdapterEvents adapterEvents;
    private AdapterNotes adapterNotes;
    private AdapterWebsites adapterWebsites;
    private ContactInfoViewModel mContactInfoViewModel;
    private ActivityResultLauncher<Intent> mEditContactLauncher;

    public void ivProfileSmallClick(ActivityContactInformation contactInformationAct, View view) {
        String str;
        if (contactInformationAct.selectedContact != null) {
            String contactPhotoUri = contactInformationAct.selectedContact.getContactPhotoUri();
            if (contactPhotoUri == null || contactPhotoUri.isEmpty()) {
                String contactPhotoThumbUri = contactInformationAct.selectedContact.getContactPhotoThumbUri();
                if (contactPhotoThumbUri == null || contactPhotoThumbUri.isEmpty()) {
                    str = "";
                } else {
                    str = contactInformationAct.selectedContact.getContactPhotoThumbUri();
                }
            } else {
                str = contactInformationAct.selectedContact.getContactPhotoUri();
            }
            if (str == null || str.isEmpty()) {
                return;
            }
            contactInformationAct.startActivity(new Intent(contactInformationAct, ActivityProfileView.class).putExtra("profilePath", str));
        }
    }

    public void btnMakeVideoCallClick(ActivityContactInformation contactInformationAct, View view) {
        PhoneNumber t;
        String str;
        boolean z2 = false;
        if (isVideoCall) {
            boolean z3 = false;
            if (selectedContact != null) {
                if (!selectedContact.getContactNumber().isEmpty()) {
                    Iterator<PhoneNumber> it = selectedContact.getContactNumber().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            t = null;
                            break;
                        }
                        PhoneNumber next = it.next();
                        ArrayList<PhoneNumber> contactNumber = selectedContact.getContactNumber();
                        if (!(contactNumber instanceof Collection) || !contactNumber.isEmpty()) {

                            for (PhoneNumber phoneNumber2 : contactNumber) {
                                if (phoneNumber2.getType() == PhoneNumberType.MAIN) {
                                    z2 = true;
                                    break;
                                }
                            }

                        }

                        if (z2) {
                            t = next;
                            break;
                        }

                    }
                    PhoneNumber t2 = t;
                    if (t2 == null || (str = t2.getNormalizedNumber()) == null) {
                        str = selectedContact.getContactNumber().get(0).getNormalizedNumber();
                    }
                    if (str == null || str.isEmpty()) {
                        z3 = true;
                    }
                    if (!z3) {
                        ContaxtExtUtils.makeAVideoCall(contactInformationAct, str);
                        return;
                    }
                    return;
                }
            }
            Toast.makeText(contactInformationAct, getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contactInformationAct, getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
        }
    }

    public void btnSendTextMessageClick(ActivityContactInformation contactInformationAct, View view) {
        PhoneNumber t;
        String str;
        boolean z2 = false;
        if (selectedContact != null) {
            if (!selectedContact.getContactNumber().isEmpty()) {
                Iterator<PhoneNumber> it = selectedContact.getContactNumber().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        t = null;
                        break;
                    }
                    PhoneNumber next = it.next();
                    ArrayList<PhoneNumber> contactNumber = selectedContact.getContactNumber();
                    if (!(contactNumber instanceof Collection) || !contactNumber.isEmpty()) {
                        for (PhoneNumber phoneNumber2 : contactNumber) {
                            if (phoneNumber2.getType() == PhoneNumberType.MAIN) {
                                z2 = true;
                                break;
                            }
                        }
                    }

                    if (z2) {
                        t = next;
                        break;
                    }
                }
                PhoneNumber t2 = t;
                if (t2 == null || (str = t2.getNormalizedNumber()) == null) {
                    str = selectedContact.getContactNumber().get(0).getNormalizedNumber();
                }
                ContaxtExtUtils.sendTextMessage(contactInformationAct, str);
                return;
            }
        }
        Toast.makeText(contactInformationAct, contactInformationAct.getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
    }

    public void btnMakeMailClick(ActivityContactInformation contactInformationAct, View view) {
        Email t;
        String str;
        boolean z2 = false;
        if (selectedContact != null) {
            if (!selectedContact.getContactEmail().isEmpty()) {
                Iterator<Email> it = selectedContact.getContactEmail().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        t = null;
                        break;
                    }
                    Email next = it.next();
                    ArrayList<Email> contactEmail = selectedContact.getContactEmail();
                    if (!(contactEmail instanceof Collection) || !contactEmail.isEmpty()) {

                        for (Email email : contactEmail) {
                            if (email.getType() == EmailType.MAIN) {
                                z2 = true;
                                break;
                            }
                        }

                    }

                    if (z2) {
                        t = next;
                        break;
                    }

                }
                Email t2 = t;
                if (t2 == null || (str = t2.getValue()) == null) {
                    str = selectedContact.getContactEmail().get(0).getValue();
                }
                ContaxtExtUtils.sendMail(contactInformationAct, str);
                return;
            }
        }
        Toast.makeText(contactInformationAct, contactInformationAct.getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
    }

    public void bindListeners18(ActivityContactInformation contactInformationAct) {
        if (contactInformationAct.selectedContact != null) {
            if (contactInformationAct.mContactInfoViewModel == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mContactInfoViewModel");
            }
            contactInformationAct.mContactInfoViewModel.deleteContact(contactInformationAct, CollectionsKt.mutableListOf(contactInformationAct.selectedContact));
            if (!ContaxtExtUtils.checkCallLogwritePermission(contactInformationAct)) {
                Toast.makeText(contactInformationAct, contactInformationAct.getString(R.string.allow_permission_desription), Toast.LENGTH_SHORT).show();
                return;
            }
            if (contactInformationAct.selectedContact.getContactNumber().isEmpty()) {
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", contactInformationAct.selectedContact.getContactNumber().get(0).getValue());
            ContentResolver contentResolver = contactInformationAct.getContentResolver();
            Uri uri = CallLog.Calls.CONTENT_URI;
            StringBuilder sb = new StringBuilder();
            sb.append('%');
            sb.append(((PhoneNumber) contactInformationAct.selectedContact.getContactNumber().get(0)).getValue());
            sb.append('%');
            contentResolver.update(uri, contentValues, "number like ?", new String[]{sb.toString()});
        }
    }

    public void btnMakeCallClick(ActivityContactInformation contactInformationAct, View view) {
        PhoneNumber obj;
        String value;
        boolean z2 = false;
        Contact contact = contactInformationAct.selectedContact;
        if (contact == null) {
            Toast.makeText(contactInformationAct, contactInformationAct.getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
            return;
        }
        Intrinsics.checkNotNull(contact);
        if (!(!contact.getContactNumber().isEmpty())) {
            Toast.makeText(contactInformationAct, contactInformationAct.getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
            return;
        }
        Contact contact2 = contactInformationAct.selectedContact;
        Intrinsics.checkNotNull(contact2);
        Iterator<PhoneNumber> it = contact2.getContactNumber().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            PhoneNumber next = it.next();
            ArrayList<PhoneNumber> contactNumber = selectedContact.getContactNumber();
            if (!(contactNumber instanceof Collection) || !contactNumber.isEmpty()) {
                for (PhoneNumber phoneNumber2 : contactNumber) {
                    if (phoneNumber2.getType() == PhoneNumberType.MAIN) {
                        z2 = true;
                        break;
                    }
                }
            }
            if (z2) {
                obj = next;
                break;
            }
        }
        PhoneNumber phoneNumber3 = (PhoneNumber) obj;
        if (phoneNumber3 == null || (value = phoneNumber3.getNormalizedNumber()) == null) {
            value = selectedContact.getContactNumber().get(0).getValue();
        }
        ContaxtExtUtils.makeACall(contactInformationAct, value);
    }

    private void askForDefault() {
        new DialogCommon(this, getString(R.string.set_as_default), getString(R.string.block_contect_default_app), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
            @Override
            public void onDoneClick() {

                if (ContaxtExtUtils.isQPlus()) {
                    RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
                    if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                        Intent createRequestRoleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
                        launcherMakeDefaultApp.launch(createRequestRoleIntent);
                    }
                }
            }
        }).show();

    }

    public boolean checkAppIsInstallOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Contact getEmptyContact() {
        return new Contact((Integer) null, 0, 0, "", "", "", "", "", "", (String) null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", (String) null, Integer.valueOf(ContextCompat.getColor((Context) this, R.color.app_color)), 16777217, (DefaultConstructorMarker) null);
    }

    private String getLookupKeyFromUri(Uri uri) {
        if (isEncodedContactUri(uri)) {
            return null;
        }
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() < 3) {
            return null;
        }
        return Uri.encode(pathSegments.get(2));
    }

    private boolean isEncodedContactUri(Uri uri) {
        String lastPathSegment;
        if (uri == null || (lastPathSegment = uri.getLastPathSegment()) == null) {
            return false;
        }
        return Intrinsics.areEqual(lastPathSegment, "encoded");
    }

    private void manageIntentData(final Bundle bundle) {
        try {
            final Serializable serializable = bundle.getSerializable("selectedContact");
            if (serializable != null) {
                ViewExtensionUtils.gone(binding.btnAddContact);
                this.selectedContact = (Contact) serializable;
            }
            final String string = bundle.getString("selectedContactId");
            final StringBuilder sb = new StringBuilder();
            sb.append("selectedContactId : ");
            sb.append(string);
            System.out.println((Object) sb.toString());
            new ActivityContactInformationGetContact1((Object) this);
            if (this.selectedContact != null) {
                ViewExtensionUtils.gone(binding.btnAddContact);
                this.setData(selectedContact);
            }
            if (string != null && !Intrinsics.areEqual((Object) string, (Object) "null") && !Intrinsics.areEqual((Object) string, (Object) "0") && !Intrinsics.areEqual((Object) string, (Object) "")) {
                ViewExtensionUtils.gone(binding.btnAddContact);
                mContactInfoViewModel.getContactBySimpleId(ContactDatabase.Companion.invoke((Context) this), string);
                mContactInfoViewModel.getStateOfContactById().observe((LifecycleOwner) this, new Observer<Contact>() {
                    @Override
                    public void onChanged(Contact contact) {
                        if (contact != null) {
                            ActivityContactInformation.this.selectedContact = contact;
                            setData(ActivityContactInformation.this.selectedContact);
                        }
                    }
                });
            }
            new ActivityContactInformationGetContact2((Object) this);
            if (string == null || Intrinsics.areEqual((Object) string, (Object) "null") || Intrinsics.areEqual((Object) string, (Object) "0") || Intrinsics.areEqual((Object) string, (Object) "")) {
                final String string2 = bundle.getString("selectedContactNumber");
                System.out.println("selectedContactId 1: " + string2);
                if (string2 != null) {
                    binding.tvContactName.setText((CharSequence) PhoneNumberUtils.formatNumber(StringsKt.trim((CharSequence) string2).toString(), "IN"));

                    contactName = string2;
                    mCallLogViewModel.getSelectedKeyHistory(ActivityContactInformation.this, string2, mDatabase);

                    isEditShow = false;
                    ViewExtensionUtils.gone(binding.ivEditContact);
                    ViewExtensionUtils.gone(binding.ivFavorite);
                    ViewExtensionUtils.gone(binding.btnMakeMail);
                    ViewExtensionUtils.show(binding.ivAddContact);
                    ViewExtensionUtils.gone(binding.tvFirstLetter);
                    selectedContact = this.getEmptyContact();
                    selectedContact.getContactNumber().add(new PhoneNumber(string2, PhoneNumberType.NO_LABEL, "", string2.toString(), true));
                    this.setData(selectedContact);
                    ViewExtensionUtils.show(binding.btnAddContact);
                    ViewExtensionUtils.gone(binding.tvFirstLetter);
                }
            }
            if (selectedContact.getContactIdSimple() != 0) {
                final ContactDatabase invoke = ContactDatabase.Companion.invoke((Context) this);
                mContactInfoViewModel.getContactBySimpleId(invoke, String.valueOf(selectedContact.getContactIdSimple()));
            }
            final ContactDatabase invoke2 = ContactDatabase.Companion.invoke((Context) this);
            mContactInfoViewModel.getsourcenumber(invoke2, String.valueOf(selectedContact.getContactIdSimple()));
        } catch (final Exception ex) {
            System.out.println((Object) String.valueOf(ex.getMessage()));
        }
    }

    public void setData(final Contact contact) {
        final String s = contact.getContactPhotoUri();
        final boolean b = false;
        if (s == null || s.isEmpty()) {
            binding.profileLayout.setLayoutParams((ViewGroup.LayoutParams) new LinearLayout.LayoutParams(-1, (int) this.getResources().getDimension(com.intuit.sdp.R.dimen._140sdp)));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(contact.getNamePrefix());
        if (!contact.getNamePrefix().isEmpty()) {
            sb.append(" ");
        }
        sb.append(contact.getFirstName());
        if (!contact.getFirstName().isEmpty()) {
            sb.append(" ");
        }
        sb.append(contact.getMiddleName());
        if (!contact.getMiddleName().isEmpty()) {
            sb.append(" ");
        }
        sb.append(contact.getSurName());
        if (!contact.getSurName().isEmpty()) {
            sb.append(" ");
        }
        final StringBuilder sb2 = new StringBuilder();
        if (!contact.getNameSuffix().isEmpty()) {
            sb2.append(" ");
        }
        sb2.append(contact.getNameSuffix());
        sb.append(StringsKt.trim((CharSequence) sb2.toString()).toString());
        final String string = sb.toString();
        mContactInfoViewModel.getStateOfBlockNumber().observe((LifecycleOwner) this, new Observer<List<BlockContact>>() {
            @Override
            public void onChanged(List<BlockContact> list) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (PhoneNumber t : contact.getContactNumber()) {
                    String replace$default = StringsKt.replace(t.getValue(), " ", "", false);
                    Object obj = linkedHashMap.get(replace$default);
                    if (obj == null) {
                        obj = (List) new ArrayList();
                        linkedHashMap.put(replace$default, obj);
                    }
                    ((List) obj).add(t);
                }
                Iterator<BlockContact> it = list.iterator();
                while (it.hasNext()) {
                    if (linkedHashMap.containsKey(StringsKt.replace(it.next().getValue(), " ", "", false))) {
                        isBlock = true;
                    }
                }
            }
        });
        final String s2 = string;
        if (StringsKt.isBlank((CharSequence) s2) ^ true) {
            binding.tvContactName.setText((CharSequence) StringsKt.trim((CharSequence) s2).toString());

            contactName = StringsKt.trim((CharSequence) s2).toString();
//            Log.e("fatal4", "getSelectedKeyHistory bbb: " + StringsKt.trim((CharSequence) s2).toString() );
            mCallLogViewModel.getSelectedKeyHistory(ActivityContactInformation.this, StringsKt.trim((CharSequence) s2).toString(), mDatabase);

            binding.tvTitle.setText((CharSequence) StringsKt.trim((CharSequence) s2).toString());
            binding.tvFirstLetter.setText((CharSequence) String.valueOf(StringsKt.trim((CharSequence) s2).toString().charAt(0)));
        } else if (contact.getContactNumber().isEmpty() ^ true) {
            binding.tvContactName.setText((CharSequence) PhoneNumberUtils.formatNumber(StringsKt.trim((CharSequence) contact.getContactNumber().get(0).getValue()).toString(), "IN"));

            contactName = StringsKt.trim((CharSequence) contact.getContactNumber().get(0).getValue()).toString();
//            Log.e("fatal4", "getSelectedKeyHistory ccc: " + StringsKt.trim((CharSequence) contact.getContactNumber().get(0).getValue()).toString() );
            mCallLogViewModel.getSelectedKeyHistory(ActivityContactInformation.this, StringsKt.trim((CharSequence) contact.getContactNumber().get(0).getValue()).toString(), mDatabase);

            final String s3 = contact.getContactNumber().get(0).getValue();
            if (s3 != null && !s3.isEmpty()) {
                binding.tvFirstLetter.setText((CharSequence) String.valueOf(StringsKt.trim((CharSequence) contact.getContactNumber().get(0).getValue()).toString().charAt(0)));
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(contact.getFirstName());
        sb3.append(' ');
        sb3.append(contact.getMiddleName());
        sb3.append(contact.getNamePrefix());
        sb3.append(' ');
        sb3.append(contact.getSurName());
        sb3.append(' ');
        binding.tvTitle.setText((CharSequence) sb3.toString());
        contact.getNameSuffix();
        String s4;
        if (contact.getJobTitle().length() > 0 && contact.getJobPosition().length() > 0) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(contact.getJobTitle());
            sb4.append(" · ");
            sb4.append(contact.getJobPosition());
            sb4.append(" · ");
            sb4.append(contact.getCompany());
            s4 = sb4.toString();
        } else {
            s4 = contact.getCompany();
        }
        if (!Intrinsics.areEqual((Object) s4, (Object) "")) {
            ViewExtensionUtils.show(binding.layoutOffice);
            final String s5 = s4;
            binding.tvContactOrganisation.setText((CharSequence) s5);
            binding.tvCompanyName.setText((CharSequence) s5);
        } else {
            ViewExtensionUtils.gone(binding.layoutOffice);
        }
        final LinearLayout.LayoutParams linearLayout$LayoutParams = new LinearLayout.LayoutParams(-1, 0);
        linearLayout$LayoutParams.height = (int) getResources().getDimension(com.intuit.sdp.R.dimen._150sdp);
        if (contact.getContactPhotoUri() != null) {
            ViewExtensionUtils.gone(binding.tvFirstLetter);
            binding.ivProfileSmall.setColorFilter((ColorFilter) null);
            ViewExtensionUtils.show(binding.ivProfileSmall);
            Glide.with((FragmentActivity) this).load(contact.getContactPhotoUri()).into(binding.ivProfileSmall);
        } else {
            ViewExtensionUtils.show(binding.ivProfileSmall);
            final Integer bgColor = contact.getBgColor();
            binding.ivProfileSmall.setColorFilter((int) bgColor);
            linearLayout$LayoutParams.height = (int) getResources().getDimension(com.intuit.sdp.R.dimen._150sdp);
            final String string2 = StringsKt.trim((CharSequence) contact.getFirstNameOriginal()).toString();
            if (string2.length() > 0) {
                binding.tvFirstLetter.setText((CharSequence) String.valueOf(string2.charAt(0)));
            }
        }
        binding.profileLayout.setLayoutParams((ViewGroup.LayoutParams) linearLayout$LayoutParams);
        final ArrayList list = contact.getContactEmail();
        boolean b2 = false;
        Label_1259:
        {
            if (!(list instanceof Collection) || !((ArrayList<Email>) list).isEmpty()) {
                final Iterator<Email> iterator = (Iterator<Email>) list.iterator();
                while (iterator.hasNext()) {
                    if (Intrinsics.areEqual((Object) iterator.next().getValue(), (Object) "") ^ true) {
                        b2 = true;
                        break Label_1259;
                    }
                }
            }
            b2 = false;
        }
        if (b2) {
            ViewExtensionUtils.show(binding.btnMakeMail);
            final ArrayList<Email> list2 = contact.getContactEmail();
            final ArrayList<Email> list3 = new ArrayList<Email>();
            for (final Email next : list2) {
                if (Intrinsics.areEqual((Object) next.getValue(), (Object) "") ^ true) {
                    list3.add(next);
                }
            }
            adapterEmails.setPhoneList((List) list3);
        } else {
            ViewExtensionUtils.gone(binding.btnMakeMail);
        }
        final ArrayList list4 = contact.getContactEmail();
        boolean b3 = false;
        Label_1504:
        {
            if (!(list4 instanceof Collection) || !((ArrayList<Email>) list4).isEmpty()) {
                final Iterator<Email> iterator3 = (Iterator<Email>) list4.iterator();
                while (iterator3.hasNext()) {
                    if (Intrinsics.areEqual((Object) iterator3.next().getValue(), (Object) "") ^ true) {
                        b3 = true;
                        break Label_1504;
                    }
                }
            }
            b3 = false;
        }
        Label_1655:
        {
            if (!b3) {
                final ArrayList list5 = contact.getContactNumber();
                boolean b4 = false;
                Label_1584:
                {
                    if (!(list5 instanceof Collection) || !((ArrayList<PhoneNumber>) list5).isEmpty()) {
                        final Iterator<PhoneNumber> iterator4 = (Iterator<PhoneNumber>) list5.iterator();
                        while (iterator4.hasNext()) {
                            if (Intrinsics.areEqual((Object) iterator4.next().getValue(), (Object) "") ^ true) {
                                b4 = true;
                                break Label_1584;
                            }
                        }
                    }
                    b4 = false;
                }
                if (!b4) {
                    binding.ivContectInfo.setVisibility(View.GONE);
                    binding.layoutEmptyNumberAndEmail.setVisibility(View.VISIBLE);
                    break Label_1655;
                }
            }
            final Looper myLooper = Looper.myLooper();
            new Handler(myLooper).postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.ivContectInfo.setVisibility(View.VISIBLE);
                    binding.layoutEmptyNumberAndEmail.setVisibility(View.GONE);
                }
            }, 100L);

        }
        if (contact.getContactNumber().isEmpty()) {
            mContactInfoViewModel.refreshPhoneNumber((Context) this, contact.getContactId());
        } else {
            final ArrayList<PhoneNumber> list6 = contact.getContactNumber();
            final ArrayList<PhoneNumber> list7 = new ArrayList<PhoneNumber>();
            for (PhoneNumber next2 : list6) {
                if (Intrinsics.areEqual((Object) next2.getValue(), (Object) "") ^ true) {
                    list7.add(next2);
                }
            }
            adapterPhoneNumbers.setPhoneList((List) list7);
        }
        final ArrayList list8 = contact.getContactNotes();
        boolean b5 = false;
        Label_1886:
        {
            if (!(list8 instanceof Collection) || !((ArrayList<String>) list8).isEmpty()) {
                final Iterator<String> iterator6 = (Iterator<String>) list8.iterator();
                while (iterator6.hasNext()) {
                    if (Intrinsics.areEqual((Object) iterator6.next(), (Object) "") ^ true) {
                        b5 = true;
                        break Label_1886;
                    }
                }
            }
            b5 = false;
        }
        Label_2216:
        {
            if (!b5) {
                final ArrayList list9 = contact.getContactEvent();
                boolean b6 = false;
                Label_1966:
                {
                    if (!(list9 instanceof Collection) || !((ArrayList<Event>) list9).isEmpty()) {
                        final Iterator<Event> iterator7 = (Iterator<Event>) list9.iterator();
                        while (iterator7.hasNext()) {
                            if (Intrinsics.areEqual((Object) iterator7.next().getValue(), (Object) "") ^ true) {
                                b6 = true;
                                break Label_1966;
                            }
                        }
                    }
                    b6 = false;
                }
                if (!b6) {
                    final ArrayList list10 = contact.getWebsites();
                    boolean b7 = false;
                    Label_2043:
                    {
                        if (!(list10 instanceof Collection) || !((ArrayList<String>) list10).isEmpty()) {
                            final Iterator<String> iterator8 = (Iterator<String>) list10.iterator();
                            while (iterator8.hasNext()) {
                                if (Intrinsics.areEqual((Object) iterator8.next(), (Object) "") ^ true) {
                                    b7 = true;
                                    break Label_2043;
                                }
                            }
                        }
                        b7 = false;
                    }
                    if (!b7) {
                        ViewExtensionUtils.gone(binding.cardAbout);
                        break Label_2216;
                    }
                }
            }
            ViewExtensionUtils.show(binding.cardAbout);
            final ArrayList<Event> list11 = contact.getContactEvent();
            final ArrayList<Event> list12 = new ArrayList<Event>();
            for (Event next3 : list11) {
                if (Intrinsics.areEqual((Object) next3.getValue(), (Object) "") ^ true) {
                    list12.add(next3);
                }
            }
            adapterEvents.setPhoneList((List) list12);
        }
        final ArrayList list13 = contact.getContactNotes();
        int n = 0;
        Label_2291:
        {
            if (list13 instanceof Collection && ((ArrayList<String>) list13).isEmpty()) {
                n = (b ? 1 : 0);
            } else {
                final Iterator<String> iterator10 = list13.iterator();
                do {
                    n = (b ? 1 : 0);
                    if (iterator10.hasNext()) {
                        continue;
                    }
                    break Label_2291;
                } while (!(Intrinsics.areEqual((Object) iterator10.next(), (Object) "") ^ true));
                n = 1;
            }
        }
        if (n != 0) {
            final ArrayList<String> list14 = contact.getContactNotes();
            final ArrayList<String> list15 = new ArrayList<String>();
            for (String next4 : list14) {
                if (Intrinsics.areEqual((Object) next4, (Object) "") ^ true) {
                    list15.add(next4);
                }
            }
            adapterNotes.setPhoneList((List) list15);
        }
        if (contact.getWebsites().size() > 0) {
            adapterWebsites.setPhoneList((List) contact.getWebsites());
        }
        if (contact.getContactIsStared()) {
            binding.ivFavorite.setImageResource(R.drawable.info_icon_fav_selected);
        } else {
            binding.ivFavorite.setImageResource(R.drawable.info_icon_fav_unselected);
        }


        try {
            if (!isValidPhoneNumber(binding.tvContactName.getText().toString().trim())) {
                binding.btnMakeVideoCall.setVisibility(View.VISIBLE);
            } else {
                binding.btnMakeVideoCall.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            binding.btnMakeVideoCall.setVisibility(View.GONE);
        }


    }

    private static final String PHONE_NUMBER_PATTERN =
            "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
    // Create a Pattern object
    private final Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);

    public boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number matches the pattern
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public void showProfilePopup(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = null;
        if (MyApplication.getInstance().getResponseData() != null && BuildConfig.VERSION_CODE == MyApplication.getInstance().getResponseData().getAppVersion() && MyApplication.getInstance().getResponseData().getIsDefault() == 1) {
            popupView = inflater.inflate(R.layout.popup_profile1, null);
        } else {
            popupView = inflater.inflate(R.layout.popup_profile, null);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);

        popupView.findViewById(R.id.llDelete).setOnClickListener(v -> {
            popupWindow.dismiss();

            new DialogCommon(this, getString(R.string.delete), getString(R.string.confirm_message_of_delete), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
                @Override
                public void onDoneClick() {
                    bindListeners18(ActivityContactInformation.this);
                }
            }).show();

        });
        popupView.findViewById(R.id.llShare).setOnClickListener(v -> {
            popupWindow.dismiss();
            if (selectedContact != null) {

                try {
                    List<Contact> arrayList = new ArrayList<>();
                    arrayList.add(selectedContact);
                    ReadContact.shareContactList(ActivityContactInformation.this, arrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        popupView.findViewById(R.id.llSetRingtone).setOnClickListener(v -> {
            popupWindow.dismiss();

            if (selectedContact != null) {
                String[] permissions;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    permissions = new String[]{Manifest.permission.READ_MEDIA_AUDIO};
                } else {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                }

                requestPermissions(permissions, () -> {
                    new DialogSelectRingtone(ActivityContactInformation.this, new DialogSelectRingtone.OnContactSelectListener() {
                        @Override
                        public void onSelected(String ringtone) {

                            if (selectedContact == null || selectedContact.getContactNumber().isEmpty()) {
                                return;
                            }
                            final Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, selectedContact.getContactNumber().get(0).getValue());
                            final String[] projection = new String[]{
                                    ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
                            };
                            final Cursor cursor = getContentResolver().query(lookupUri, projection, null, null, null);
                            cursor.moveToFirst();
                            try {
                                // Get the contact lookup Uri
                                final long contactId = cursor.getLong(0);
                                final String lookupKey = cursor.getString(1);
                                final Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
                                if (contactUri == null) {
                                    // Invalid arguments
                                    return;
                                }

                                final ContentValues values = new ContentValues();
                                values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, Uri.fromFile(new File(ringtone)).toString());
                                getContentResolver().update(contactUri, values, null, null);
                                Toast.makeText(ActivityContactInformation.this, getString(R.string.ringtone_set_successfully), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
//                                Log.e("fatal3", "onActivityResult: " + e.getMessage());
                            } finally {
                                // Don't forget to close your Cursor
                                cursor.close();
                            }

                        }
                    }).show(getSupportFragmentManager(), "");
                });

            }

        });

        if (selectedContact != null) {
            if (!selectedContact.getContactNumber().isEmpty()) {

                int size = selectedContact.getContactNumber().size();
                for (int i = 0; i < size; i++) {
                    String str = selectedContact.getContactNumber().get(i).getValue();
                    List<BlockContact> objectRef = new GetBlockContactHelper(ActivityContactInformation.this).invoke();
                    if (objectRef != null && objectRef.size() > 0) {
                        for (int j = 0; j < objectRef.size(); j++) {

                            try {
                                if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
                                    ((TextView) popupView.findViewById(R.id.tvBlock)).setText(getString(R.string.unblock));
                                } else {
                                    ((TextView) popupView.findViewById(R.id.tvBlock)).setText(getString(R.string.block));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

            }
        }

        popupView.findViewById(R.id.llBlock).setOnClickListener(v -> {
            popupWindow.dismiss();
            if (ContaxtExtUtils.isDefault(ActivityContactInformation.this)) {
                if (selectedContact != null) {
                    if (!selectedContact.getContactNumber().isEmpty()) {

                        int size = selectedContact.getContactNumber().size();
                        for (int i = 0; i < size; i++) {
                            String str = selectedContact.getContactNumber().get(i).getValue();
                            List<BlockContact> objectRef = new GetBlockContactHelper(ActivityContactInformation.this).invoke();
                            if (objectRef != null && objectRef.size() > 0) {
                                for (int j = 0; j < objectRef.size(); j++) {

                                    if (objectRef.get(j).getValue() != null && str != null) {
                                        if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
                                            mContactInfoViewModel.unBlockThisNumber(ActivityContactInformation.this, objectRef.get(j).getValue());
                                            Toast.makeText(ActivityContactInformation.this, getString(R.string.number_unblocked_msg), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        return;
                                    }

                                }
                            }
                            mContactInfoViewModel.blockThisNumber(ActivityContactInformation.this, selectedContact.getContactNumber().get(i).getValue());
                        }
                        Toast.makeText(ActivityContactInformation.this, getString(R.string.toast_block_contact), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ActivityContactInformation.this, getString(R.string.block_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                askForDefault();
            }
        });

    }

    public void showNotSavedPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = null;
        if (MyApplication.getInstance().getResponseData() != null && BuildConfig.VERSION_CODE == MyApplication.getInstance().getResponseData().getAppVersion() && MyApplication.getInstance().getResponseData().getIsDefault() == 1) {
            popupView = inflater.inflate(R.layout.popup_profile_not_saved1, null);
        } else {
            popupView = inflater.inflate(R.layout.popup_profile_not_saved, null);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);


        popupView.findViewById(R.id.llDelete).setOnClickListener(v -> {
            popupWindow.dismiss();

            if (selectedContact != null) {
                new DialogCommon(this, getString(R.string.delete), getString(R.string.confirm_message_of_delete), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
                    @Override
                    public void onDoneClick() {
                        if (!selectedContact.getContactNumber().isEmpty()) {
                            ReadContact.deleteNumFromCallLog(ActivityContactInformation.this, selectedContact.getContactNumber().get(0).getValue());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 200);
                        }
                    }
                }).show();
            }

        });

        if (selectedContact != null) {
            if (!selectedContact.getContactNumber().isEmpty()) {

                int size = selectedContact.getContactNumber().size();
                for (int i = 0; i < size; i++) {
                    String str = selectedContact.getContactNumber().get(i).getValue();
                    List<BlockContact> objectRef = new GetBlockContactHelper(ActivityContactInformation.this).invoke();
                    if (objectRef != null && !objectRef.isEmpty()) {
                        for (int j = 0; j < objectRef.size(); j++) {

                            if (objectRef.get(j).getValue() != null && str != null) {
                                if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
                                    ((TextView) popupView.findViewById(R.id.tvBlock)).setText(getString(R.string.unblock));
                                } else {
                                    ((TextView) popupView.findViewById(R.id.tvBlock)).setText(getString(R.string.block));
                                }
                            }

                        }
                    }

                }

            }
        }

        popupView.findViewById(R.id.llBlock).setOnClickListener(v -> {
            popupWindow.dismiss();
            if (ContaxtExtUtils.isDefault(ActivityContactInformation.this)) {
                if (selectedContact != null) {
                    if (!selectedContact.getContactNumber().isEmpty()) {

                        int size = selectedContact.getContactNumber().size();
                        for (int i = 0; i < size; i++) {
                            String str = selectedContact.getContactNumber().get(i).getValue();
                            List<BlockContact> objectRef = new GetBlockContactHelper(ActivityContactInformation.this).invoke();
                            if (objectRef != null && objectRef.size() > 0) {
                                for (int j = 0; j < objectRef.size(); j++) {

                                    if (objectRef.get(j).getValue() != null && str != null) {
                                        if (PhoneNumberUtils.formatNumber(objectRef.get(j).getValue(), "IN").equals(PhoneNumberUtils.formatNumber(str, "IN")) || PhoneNumberUtils.formatNumber(objectRef.get(j).getValue().replace("+91", "").trim(), "IN").equals(PhoneNumberUtils.formatNumber(str.replace("+91", "").trim(), "IN"))) {
                                            mContactInfoViewModel.unBlockThisNumber(ActivityContactInformation.this, objectRef.get(j).getValue());
                                            Toast.makeText(ActivityContactInformation.this, getString(R.string.number_unblocked_msg), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        return;
                                    }

                                }
                            }
                            mContactInfoViewModel.blockThisNumber(ActivityContactInformation.this, selectedContact.getContactNumber().get(i).getValue());
                        }
                        Toast.makeText(ActivityContactInformation.this, getString(R.string.toast_block_contact), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ActivityContactInformation.this, getString(R.string.block_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                askForDefault();
            }
        });

    }

    public void bindListeners() {
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.tvSeeAll.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityContactInformation.this, ActivityCallHistory.class);
            intent.putExtra("contactName", contactName);
            startActivity(intent);
        });
        binding.btnMenu.setOnClickListener(view -> {
            if (isEditShow) {
                showProfilePopup(view);
            } else {
                showNotSavedPopup(view);
            }
        });

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    ViewExtensionUtils.show(binding.tvTitle);
                } else {
                    ViewExtensionUtils.invisible(binding.tvTitle);
                }
            }
        });
        binding.ivProfileSmall.setOnClickListener(view -> {
            ivProfileSmallClick(ActivityContactInformation.this, view);
        });
        binding.ivFavorite.setOnClickListener(view -> {
            if (selectedContact != null) {
                if (selectedContact.getContactIsStared()) {
                    mContactInfoViewModel.updateStarred(ActivityContactInformation.this, CollectionsKt.listOf(selectedContact), 0);
                    selectedContact.setContactIsStared(false);
                } else {
                    mContactInfoViewModel.updateStarred(ActivityContactInformation.this, CollectionsKt.listOf(selectedContact), 1);
                    selectedContact.setContactIsStared(true);
                }
            }
        });
        binding.ivEditContact.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityContactInformation.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", (Serializable) selectedContact);
            intent.putExtra("isUpdate", true);
            mEditContactLauncher.launch(intent);
        });
        binding.layoutAddPhoneNumber.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityContactInformation.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", (Serializable) selectedContact);
            intent.putExtra("isUpdate", true);
            mEditContactLauncher.launch(intent);
        });
        binding.layoutAddEmail.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityContactInformation.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", (Serializable) selectedContact);
            intent.putExtra("isUpdate", true);
            mEditContactLauncher.launch(intent);
        });
        binding.btnMakeCall.setOnClickListener(view -> {
            btnMakeCallClick(ActivityContactInformation.this, view);
        });
        binding.btnMakeVideoCall.setOnClickListener(view -> {
            if ((checkAppIsInstallOrNot("com.google.android.apps.tachyon"))) {
                Toast.makeText(ActivityContactInformation.this, getString(R.string.no_app_found_to_handle_this_action), Toast.LENGTH_SHORT).show();
                return;
            }
            btnMakeVideoCallClick(ActivityContactInformation.this, view);
        });
        binding.btnSendTextMessage.setOnClickListener(view -> {
            btnSendTextMessageClick(ActivityContactInformation.this, view);
        });
        binding.btnMakeMail.setOnClickListener(view -> {
            btnMakeMailClick(ActivityContactInformation.this, view);
        });

    }

//    private void checkWhatsAppNumber(String contactNumber) {
//        try {
//            // Build WhatsApp URI
//            Uri uri = Uri.parse("https://wa.me/" + contactNumber);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.setPackage("com.whatsapp");
//
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                showWhatsAppDetails(contactNumber);
//            } else {
//                hideWhatsAppDetails();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            hideWhatsAppDetails();
//        }
//    }

//    private void showWhatsAppDetails(String contactNumber) {
//        // Make WhatsApp-related UI components visible
//        findViewById(R.id.whatsappLayout).setVisibility(View.VISIBLE);
//
//        // Set contact details in the layout
//        TextView whatsappNumber = findViewById(R.id.whatsappNumber);
//        whatsappNumber.setText("WhatsApp: " + contactNumber);
//
//        // Add more WhatsApp-specific details if needed
//    }

//    private void hideWhatsAppDetails() {
//        // Hide WhatsApp-related UI components
//        findViewById(R.id.whatsappLayout).setVisibility(View.GONE);
//    }

    public void bindMethods() {
        mContactInfoViewModel.getStateOfDeleteContact().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });
        if (ContaxtExtUtils.isDefaultCallerId(ActivityContactInformation.this)) {
            mContactInfoViewModel.getAllBlockNumber(ActivityContactInformation.this);
        }
        binding.rcvPhoneNumber.setLayoutManager(new LinearLayoutManager(ActivityContactInformation.this));
        binding.rcvPhoneNumber.setAdapter(adapterPhoneNumbers);
        binding.rcvEmail.setLayoutManager(new LinearLayoutManager(ActivityContactInformation.this));
        binding.rcvEmail.setAdapter(adapterEmails);
        binding.rcvEvents.setLayoutManager(new LinearLayoutManager(ActivityContactInformation.this));
        binding.rcvEvents.setAdapter(adapterEvents);
        binding.rcvNotes.setLayoutManager(new LinearLayoutManager(ActivityContactInformation.this));
        binding.rcvNotes.setAdapter(adapterNotes);
        binding.rcvWebsites.setLayoutManager(new LinearLayoutManager(ActivityContactInformation.this));
        binding.rcvWebsites.setAdapter(adapterWebsites);
        mEditContactLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {

                Intent data = activityResult.getData();
                if (data != null && data.getExtras() != null) {
                    manageIntentData(data.getExtras());
                }
                if (selectedContact != null) {
                    if (selectedContact.getContactIsStared()) {
                        mContactInfoViewModel.updateStarred(ActivityContactInformation.this, CollectionsKt.listOf(selectedContact), 1);
                        selectedContact.setContactIsStared(true);
                        binding.ivFavorite.setImageResource(R.drawable.info_icon_fav_selected);
                    }
                }

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityContactInformation.this, RecyclerView.VERTICAL, false);
        binding.rvHistory.setLayoutManager(linearLayoutManager);
        binding.rvHistory.setAdapter(adapterSelectedCallLog);

        mCallLogViewModel.getStateOfHistory().observe(ActivityContactInformation.this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> list) {
                if (list != null && list.size() > 0) {
                    callList = new ArrayList<>();
                    callList = (ArrayList) list;
                    adapterSelectedCallLog.setData(TypeIntrinsics.asMutableList(callList));

                    binding.llHistory.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);

                } else {
                    binding.llHistory.setVisibility(View.GONE);
                }
            }
        });
        mCallLogViewModel.getHistoryListState().observe(ActivityContactInformation.this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> list) {
                mCallLogViewModel.getSelectedKeyHistory(ActivityContactInformation.this, contactName, mDatabase);
            }
        });

        try {
            getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new ObserveContact(new OnContactChange() {
                @Override
                public void onContactChange() {
                    mCallLogViewModel.updateHistory(ActivityContactInformation.this, mDatabase);
                }
            }));
            getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new ObserveContact(new OnContactChange() {
                @Override
                public void onContactChange() {
                    mCallLogViewModel.updateHistory(ActivityContactInformation.this, mDatabase);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Label_1438:
        {
            try {
                final Bundle extras = this.getIntent().getExtras();
                if (extras != null) {
                    manageIntentData(extras);
                    final Serializable serializable = extras.getSerializable("selectedContact");
                    if (serializable != null) {
                        ViewExtensionUtils.gone(binding.btnAddContact);
                        final Contact selectedContact = (Contact) serializable;
                        if (selectedContact.getContactId() != 0) {
                            selectedContact.getContactId();
                        } else {
                            this.isEditShow = false;
                            ViewExtensionUtils.gone(binding.ivEditContact);
                            ViewExtensionUtils.gone(binding.ivFavorite);
                            ViewExtensionUtils.gone(binding.btnMakeMail);
                            ViewExtensionUtils.show(binding.ivAddContact);
                            ViewExtensionUtils.gone(binding.tvFirstLetter);
                            this.setData(selectedContact);
                        }
                        if (selectedContact.getContactNumber().isEmpty()) {
                            mContactInfoViewModel.refreshPhoneNumber(ActivityContactInformation.this, selectedContact.getContactId());
                        } else {
                            final Iterable iterable = selectedContact.getContactNumber();
                            final Collection collection = new ArrayList();
                            for (final Object next : iterable) {
                                if (Intrinsics.areEqual((Object) ((PhoneNumber) next).getValue(), (Object) "") ^ true) {
                                    collection.add(next);
                                }
                            }
                            adapterPhoneNumbers.setPhoneList((List) collection);
                        }
                        final Iterable iterable2 = selectedContact.getContactEmail();
                        boolean b = false;
                        Label_0976:
                        {
                            if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                                final Iterator iterator2 = iterable2.iterator();
                                while (iterator2.hasNext()) {
                                    if (Intrinsics.areEqual((Object) ((Email) iterator2.next()).getValue(), (Object) "") ^ true) {
                                        b = true;
                                        break Label_0976;
                                    }
                                }
                            }
                            b = false;
                        }
                        if (b) {
                            ViewExtensionUtils.show(binding.btnMakeMail);
                            final Iterable iterable3 = selectedContact.getContactEmail();
                            final Collection collection2 = new ArrayList();
                            for (final Object next2 : iterable3) {
                                if (Intrinsics.areEqual((Object) ((Email) next2).getValue(), (Object) "") ^ true) {
                                    collection2.add(next2);
                                }
                            }
                            adapterEmails.setPhoneList((List) collection2);
                        } else {
                            ViewExtensionUtils.gone(binding.btnMakeMail);
                        }
                        final Iterable iterable4 = selectedContact.getContactEmail();
                        boolean b2 = false;
                        Label_1244:
                        {
                            if (!(iterable4 instanceof Collection) || !((Collection) iterable4).isEmpty()) {
                                final Iterator iterator4 = iterable4.iterator();
                                while (iterator4.hasNext()) {
                                    if (Intrinsics.areEqual((Object) ((Email) iterator4.next()).getValue(), (Object) "") ^ true) {
                                        b2 = true;
                                        break Label_1244;
                                    }
                                }
                            }
                            b2 = false;
                        }
                        if (!b2) {
                            final Iterable iterable5 = selectedContact.getContactNumber();
                            boolean b3 = false;
                            Label_1330:
                            {
                                if (!(iterable5 instanceof Collection) || !((Collection) iterable5).isEmpty()) {
                                    final Iterator iterator5 = iterable5.iterator();
                                    while (iterator5.hasNext()) {
                                        if (Intrinsics.areEqual((Object) ((PhoneNumber) iterator5.next()).getValue(), (Object) "") ^ true) {
                                            b3 = true;
                                            break Label_1330;
                                        }
                                    }
                                }
                                b3 = false;
                            }
                            if (!b3) {
                                binding.ivContectInfo.setVisibility(View.GONE);
                                binding.layoutEmptyNumberAndEmail.setVisibility(View.VISIBLE);
                                break Label_1438;
                            }
                        }
                        binding.ivContectInfo.setVisibility(View.VISIBLE);
                        binding.layoutEmptyNumberAndEmail.setVisibility(View.GONE);
                    } else {
                        binding.ivContectInfo.setVisibility(View.VISIBLE);
                        binding.layoutEmptyNumberAndEmail.setVisibility(View.GONE);
                    }
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }

        mContactInfoViewModel.getStateOfStarred().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (selectedContact.getContactIsStared()) {
                    binding.ivFavorite.setImageResource(R.drawable.info_icon_fav_selected);
                } else {
                    binding.ivFavorite.setImageResource(R.drawable.info_icon_fav_unselected);
                }
            }
        });

        mContactInfoViewModel.getStateOfPhoneNumber().observe(this, new Observer<ArrayList<PhoneNumber>>() {
            @Override
            public void onChanged(ArrayList<PhoneNumber> arrayList) {
                if (arrayList.size() > 0) {
                    HashSet hashSet = new HashSet();
                    ArrayList arrayList2 = new ArrayList();
                    for (PhoneNumber t : arrayList) {
                        PhoneNumber t2 = t;
                        if (selectedContact != null) {
                            if (selectedContact.getContactNumber().isEmpty()) {
                                selectedContact.getContactNumber().add(t2);
                            }
                        }
                        if (hashSet.add(new Regex("\\s").replace(t2.getValue(), ""))) {
                            arrayList2.add(t);
                        }
                    }
                    ArrayList arrayList3 = new ArrayList();
                    for (Object obj : arrayList2) {
                        if (!Intrinsics.areEqual(((PhoneNumber) obj).getValue(), "")) {
                            arrayList3.add(obj);
                        }
                    }
                    adapterPhoneNumbers.setPhoneList(arrayList3);
                } else {
                    binding.ivContectInfo.setVisibility(View.GONE);
                    binding.layoutEmptyNumberAndEmail.setVisibility(View.VISIBLE);
                }
            }
        });
        mContactInfoViewModel.getViewNumberSorcs().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                for (String t : list) {
                    if ((checkAppIsInstallOrNot("com.google.android.apps.tachyon"))) {
                        if (!selectedContact.getContactNumber().isEmpty()) {
                            ViewExtensionUtils.show(binding.btnMakeVideoCall);
                            setVideoCall(true);
                        }
                    }
                }
            }
        });
        final Uri data = getIntent().getData();
        if (data != null) {
            ViewExtensionUtils.gone(binding.ivAddContact);
            binding.ivProfileSmall.setBackground((Drawable) null);
            final String lookupKeyFromUri = this.getLookupKeyFromUri(data);
            if (lookupKeyFromUri != null) {
                mContactInfoViewModel.getContactByLookupKey(ActivityContactInformation.this, ContactDatabase.Companion.invoke(ActivityContactInformation.this), lookupKeyFromUri);
                mContactInfoViewModel.getStateOfContactById().observe(this, new Observer<Contact>() {
                    @Override
                    public void onChanged(Contact contact) {
                        if (contact != null) {
                            selectedContact = contact;
                            setData(selectedContact);
                        }
                    }
                });
            } else {
                try {
                    if (Intrinsics.areEqual((Object) data.getScheme(), (Object) "content")) {
                        final String string = data.toString();
                        final String replace$default = StringsKt.replace(StringsKt.replace(string, "content:", "", false), "#", "", false);
                        final String schemeSpecificPart = data.getSchemeSpecificPart();
                        final Object value = new JSONObject(StringsKt.replace(replace$default, schemeSpecificPart, "", false)).get("display_name");
                        binding.tvContactName.setText((CharSequence) PhoneNumberUtils.formatNumber(StringsKt.trim((CharSequence) value.toString()).toString(), "IN"));

                        contactName = value.toString();
                        mCallLogViewModel.getSelectedKeyHistory(ActivityContactInformation.this, value.toString(), mDatabase);

                        isEditShow = false;
                        ViewExtensionUtils.gone(binding.ivEditContact);
                        ViewExtensionUtils.gone(binding.btnAddContact);
                        ViewExtensionUtils.gone(binding.ivFavorite);
                        ViewExtensionUtils.gone(binding.btnMakeMail);
                        selectedContact = this.getEmptyContact();
                        selectedContact.getContactNumber().add(new PhoneNumber(value.toString(), PhoneNumberType.NO_LABEL, "", value.toString(), Boolean.valueOf(true)));
                        ViewExtensionUtils.show(binding.ivAddContact);
                        ViewExtensionUtils.gone(binding.tvFirstLetter);
                        setData(selectedContact);
                    }
                } catch (final Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }

        binding.btnAddContact.setOnClickListener(view -> {
            Intent intent = new Intent((Context) ActivityContactInformation.this, ActivityAddContact.class);
            intent.putExtra("selectedContact", (Serializable) selectedContact);
            intent.putExtra("isUpdate", false);
            startActivity(intent);
            finish();
        });


        setupWhatsAppInteraction();
    }

    private void setupWhatsAppInteraction() {
        if (isWhatsAppInstalled(this)) {
            binding.llConnectedApp.setVisibility(View.VISIBLE);
        }

        binding.llWhatsappLay.setOnClickListener(v -> {
            toggleWhatsAppSubLayout();
        });

        if (adapterPhoneNumbers != null && !adapterPhoneNumbers.getPhoneList().isEmpty()) {
            String phoneNumber = adapterPhoneNumbers.getPhoneList().get(0).getValue();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                binding.waMessageTxt.setText("Message " + phoneNumber);
                binding.waVoiceTxt.setText("Voice Call " + phoneNumber);
                binding.waVideoTxt.setText("Video Call " + phoneNumber);
            } else {
                binding.llConnectedApp.setVisibility(View.GONE);
            }
        } else {
            binding.llConnectedApp.setVisibility(View.GONE);
        }

        setupWhatsAppActions();
    }

    private void setupWhatsAppActions() {
        binding.waMessageLay.setOnClickListener(v -> {
            if (adapterPhoneNumbers != null && !adapterPhoneNumbers.getPhoneList().isEmpty()) {
                redirectToWhatsAppMessage(adapterPhoneNumbers.getPhoneList().get(0).getValue());
            }
        });

        binding.waVoiceLay.setOnClickListener(v -> {
            if (adapterPhoneNumbers != null && !adapterPhoneNumbers.getPhoneList().isEmpty()) {
                redirectToWhatsAppVoiceCall(adapterPhoneNumbers.getPhoneList().get(0).getValue());
            }
        });

        binding.waVideoLay.setOnClickListener(v -> {
            if (adapterPhoneNumbers != null && !adapterPhoneNumbers.getPhoneList().isEmpty()) {
                redirectToWhatsAppVideoCall(adapterPhoneNumbers.getPhoneList().get(0).getValue());
            }
        });
    }

    private void toggleWhatsAppSubLayout() {
        if (binding.llWhatsappSubLay.getVisibility() == View.VISIBLE) {
            binding.whatsappArrowImg.setImageResource(R.drawable.wa_layout_open_img);
            binding.llWhatsappSubLay.setVisibility(View.GONE);
        } else {
            binding.whatsappArrowImg.setImageResource(R.drawable.wa_layout_close_img);
            binding.llWhatsappSubLay.setVisibility(View.VISIBLE);
        }
    }

    private void redirectToWhatsAppMessage(String waNumber) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + waNumber));
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to send message via WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToWhatsAppVoiceCall(String waNumber) {
        handleWhatsAppCall(waNumber, "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
    }

    private void redirectToWhatsAppVideoCall(String waNumber) {
        handleWhatsAppCall(waNumber, "vnd.android.cursor.item/vnd.com.whatsapp.video.call");
    }

    private void handleWhatsAppCall(String waNumber, String mimeType) {
        try {
            waNumber = formatWhatsAppNumber(waNumber);

            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.MIMETYPE));

                    if (number != null && number.endsWith(waNumber) && mimeType.equals(type)) {
                        String data = "content://com.android.contacts/data/" + id;
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setDataAndType(Uri.parse(data), mimeType);
                        callIntent.setPackage("com.whatsapp");
                        startActivity(callIntent);
                        break;
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to initiate WhatsApp call", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatWhatsAppNumber(String number) {
        return number.replace(" ", "").replace("+", "").concat("@s.whatsapp.net");
    }

    public boolean isWhatsAppInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void startReviewFlow() {
        try {
            ReviewManager reviewManager = ReviewManagerFactory.create(ActivityContactInformation.this);
            Task<ReviewInfo> request = reviewManager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    launchReviewFlow(reviewManager, reviewInfo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchReviewFlow(ReviewManager reviewManager, ReviewInfo reviewInfo) {
        Task<Void> reviewFlow = reviewManager.launchReviewFlow(ActivityContactInformation.this, reviewInfo);
        reviewFlow.addOnCompleteListener(task -> {
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        });
    }

    public long daysBetween(long first, long second) {
        long diffInMillis = second - first;
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    public void bindObjects() {

        MyApplication.getInstance().showSmallNativeBannerAd(this, findViewById(R.id.flBanner));

        if (daysBetween(MyApplication.instance.getInstallDate(), Calendar.getInstance().getTimeInMillis()) > 0) {
            startReviewFlow();
        }

        adapterPhoneNumbers = new AdapterPhoneNumbers();
        adapterEmails = new AdapterEmails();
        adapterEvents = new AdapterEvents();
        adapterNotes = new AdapterNotes();
        adapterWebsites = new AdapterWebsites();
        mContactInfoViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(ContactInfoViewModel.class);
        mCallLogViewModel = (CallLogViewModel) new ViewModelProvider(ActivityContactInformation.this).get(CallLogViewModel.class);
        mDatabase = ContactDatabase.Companion.invoke(ActivityContactInformation.this);
        adapterSelectedCallLog = new AdapterSelectedCallLog(ActivityContactInformation.this);
    }

    public boolean isVideoCall() {
        return this.isVideoCall;
    }

    public void setVideoCall(boolean z) {
        this.isVideoCall = z;
    }

    public void onContactUpdate() {
    }

    public ActivityContactInformationBinding setViewBinding() {
        binding = ActivityContactInformationBinding.inflate(getLayoutInflater());
        return binding;
    }

    public void onBackPressed() {
        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
            finish();
        });
    }

    private void requestPermissions(String[] permissions, OnPermissionGranted onPermissionGranted) {
        Dexter.withContext(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            onPermissionGranted.onGranted();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContactInformation.this);

        builder.setTitle(getResources().getString(R.string.dialog_perm_title));

        builder.setMessage(getResources().getString(R.string.dialog_perm_desc));
        builder.setPositiveButton(getResources().getString(R.string.dialog_perm_go_to_setting), (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            try {
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            permissionLauncher.launch(intent);
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    ActivityResultLauncher<Intent> permissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                    }
                }
            });

    public interface OnPermissionGranted {
        void onGranted();
    }
}
