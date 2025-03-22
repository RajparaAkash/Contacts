//package com.contacts.phonecontact.phonebook.dialer.Activities;
//
//import android.os.Build;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.Toast;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.GridLayoutManager;
//
//import com.contacts.phonecontact.phonebook.dialer.MyApplication;
//import com.contacts.phonecontact.phonebook.dialer.R;
//import com.contacts.phonecontact.phonebook.dialer.AllModels.BlockContact;
//import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
//import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
//import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
//import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
//import com.contacts.phonecontact.phonebook.dialer.databinding.ActivityContactPickerBinding;
//import com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode.GetBlockContactHelper;
//import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
//import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAllContact;
//import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
//import com.contacts.phonecontact.phonebook.dialer.Dialogs.DialogCommon;
//import com.contacts.phonecontact.phonebook.dialer.Viewmodels.BlockNumberViewModel;
//import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactViewModel;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//
//import kotlin.jvm.internal.Intrinsics;
//import kotlin.jvm.internal.TypeIntrinsics;
//import kotlin.text.StringsKt;
//
//public class ActivityContactPicker extends ActBase<ActivityContactPickerBinding> implements OnClickContact {
//
//    public AdapterAllContact adapterAllContact;
//    public ArrayList<ListObject> contactList = new ArrayList<>();
//    public ContactDatabase mContactDatabase;
//    public ContactViewModel mContactViewModel;
//    public BlockNumberViewModel mBlockNumberViewModel;
//    ActivityContactPickerBinding binding;
//
//
//    @Override
//    public void onContactUpdate() {
//    }
//
//    public AdapterAllContact getContactAdapter() {
//        if (adapterAllContact != null) {
//            return adapterAllContact;
//        }
//        return null;
//    }
//
//    public void setContactAdapter(AdapterAllContact adpAllContact) {
//        this.adapterAllContact = adpAllContact;
//    }
//
//    @Override
//    public ActivityContactPickerBinding setViewBinding() {
//        binding = ActivityContactPickerBinding.inflate(getLayoutInflater());
//        return binding;
//    }
//
//    @Override
//    public void bindObjects() {
//
//        MyApplication.getInstance().showBannerAd(this, findViewById(R.id.flBanner));
//
//        mBlockNumberViewModel = (BlockNumberViewModel) new ViewModelProvider(ActivityContactPicker.this).get(BlockNumberViewModel.class);
//        setContactAdapter(new AdapterAllContact(ActivityContactPicker.this));
//        getContactAdapter().setOnItemClick(this);
//        mContactViewModel = (ContactViewModel) new ViewModelProvider(this).get(ContactViewModel.class);
//        mContactDatabase = ContactDatabase.Companion.invoke(ActivityContactPicker.this);
//        mContactViewModel.loadRawContact(ActivityContactPicker.this, mContactDatabase);
//    }
//
//    @Override
//    public void bindListeners() {
//        binding.rvContacts.setLayoutManager(new GridLayoutManager(this, 1));
//        binding.rvContacts.setIndexBarColor(R.color.background);
//        binding.rvContacts.setIndexBarCornerRadius(10);
//        binding.rvContacts.setIndexBarTextColor(R.color.text_black);
//        binding.rvContacts.setIndexBarStrokeVisibility(false);
//        binding.rvContacts.setAdapter(getContactAdapter());
//        mContactViewModel.getStateOfContacts().observe(this, new Observer<List<ListObject>>() {
//            @Override
//            public void onChanged(List<ListObject> list) {
//                if (!list.isEmpty()) {
//                    contactList = (ArrayList) list;
//                    getContactAdapter().setData(contactList);
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void bindMethods() {
//        setListener();
//    }
//
//    private void setListener() {
//        binding.ivBack.setOnClickListener(view -> {
//            onBackPressed();
//        });
//        binding.inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                performSearch(String.valueOf(editable));
//            }
//        });
//    }
//
//    public void performSearch(final String s) {
//        final List list = new ArrayList();
//        for (final ListObject listObject : this.contactList) {
//            if (listObject.getType() == 3) {
//                if (!s.equals("")) {
//                    continue;
//                }
//                if (s != null) {
//                    continue;
//                }
//            }
//            final int type = listObject.getType();
//            final int n = 1;
//            Contact mContact;
//            if (type == 1) {
//                Intrinsics.checkNotNull((Object) listObject, "null cannot be cast to non-null type com.contacts.phonecontact.phonebook.dialer.AllModels.contact_data.Contact");
//                mContact = (Contact) listObject;
//            } else {
//                Intrinsics.checkNotNull((Object) listObject, "null cannot be cast to non-null type com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel");
//                mContact = ((HeaderModel) listObject).getMContact();
//            }
//            final String lowerCase = mContact.getFirstName().toLowerCase(Locale.ROOT);
//            if (!StringsKt.contains((CharSequence) lowerCase, (CharSequence) s.toString(), false) && !StringsKt.contains((CharSequence) mContact.getSurName(), (CharSequence) s.toString(), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) s.toString(), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) s.toString(), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) s.toString(), false)) {
//                final Iterable iterable = mContact.getContactNumber();
//                boolean b = false;
//                Label_0345:
//                {
//                    if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
//                        final Iterator iterator2 = iterable.iterator();
//                        while (iterator2.hasNext()) {
//                            if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator2.next()).getNormalizedNumber(), (CharSequence) s.toString(), false)) {
//                                b = true;
//                                break Label_0345;
//                            }
//                        }
//                    }
//                    b = false;
//                }
//                if (!b) {
//                    final Iterable iterable2 = mContact.getContactNumber();
//                    int n2 = 0;
//                    Label_0438:
//                    {
//                        if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
//                            final Iterator iterator3 = iterable2.iterator();
//                            while (iterator3.hasNext()) {
//                                if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator3.next()).getValue(), (CharSequence) s.toString(), false)) {
//                                    n2 = n;
//                                    break Label_0438;
//                                }
//                            }
//                        }
//                        n2 = 0;
//                    }
//                    if (n2 == 0) {
//                        continue;
//                    }
//                }
//            }
//            list.add(listObject);
//        }
//        if (s != null) {
//            this.getContactAdapter().updateList(list, s);
//        } else {
//            final AdapterAllContact contactAdapter = this.getContactAdapter();
//            final ArrayList<ListObject> contactList = this.contactList;
//            contactAdapter.updateList(TypeIntrinsics.asMutableList((Object) contactList), "");
//        }
//    }
//
//
//    private void askForBlockContact(Contact contact) {
//        new DialogCommon(this, getString(R.string.block_contact), getString(R.string.block_contact_desc), getString(R.string.yes), getString(R.string.no), new DialogCommon.OnDialogSelectionListener() {
//            @Override
//            public void onDoneClick() {
//                blockContact(contact);
//            }
//        }).show();
//    }
//
//    private void blockContact(Contact contact) {
//        if (!(!contact.getContactNumber().isEmpty())) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ActivityContactPicker.this, getString(R.string.block_error_message), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else if (Build.VERSION.SDK_INT >= 24) {
////            ContentValues contentValues = new ContentValues();
//            if (contact.getContactNumber() != null && contact.getContactNumber().size() > 0) {
//
//                int size = contact.getContactNumber().size();
//                for (int i = 0; i < size; i++) {
////                contentValues.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, contact.getContactNumber().get(i).getValue());
//                    String str = contact.getContactNumber().get(i).getValue();
//                    List<BlockContact> objectRef = new GetBlockContactHelper(ActivityContactPicker.this).invoke();
//                    if (objectRef != null && objectRef.size() > 0) {
//                        for (int j = 0; j < objectRef.size(); j++) {
//                            if (objectRef.get(j).getValue().equals(str) || objectRef.get(j).getNormalizedNumber().equals(str) || ("+91" + objectRef.get(j).getValue()).equals(str)) {
//                                Toast.makeText(ActivityContactPicker.this, R.string.toast_number_already_blocked, Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                    }
//
//                    mBlockNumberViewModel.blockThisNumber(ActivityContactPicker.this, contact.getContactNumber().get(i).getValue());
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(ActivityContactPicker.this, getString(R.string.toast_block_contact), Toast.LENGTH_SHORT).show();
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//                });
//
//            }
//
//        } else {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ActivityContactPicker.this, getString(R.string.block_not_supported), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean z) {
//        super.onWindowFocusChanged(z);
//    }
//
//    @Override
//    public void onClick(Contact contact) {
//        askForBlockContact(contact);
//    }
//
//    @Override
//    public void onBackPressed() {
//        MyApplication.getInstance().showInnerInterstitialAd(this, () -> {
//            finish();
//        });
//    }
//
//}
