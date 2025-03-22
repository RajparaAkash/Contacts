package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSearchContact;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactViewModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSearchContactBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.observer.ObserveContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class DialogSearchContact extends DialogFragment {

    Activity activity;
    DialogSearchContactBinding binding;

    private List<ListObject> contactList = CollectionsKt.emptyList();
    private AdapterSearchContact adapterSearchContact;
    private ContactDatabase mContactDatabase;
    private ContactViewModel mContactViewModel;

    public DialogSearchContact(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setStatusBarColor(activity.getColor(R.color.background));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.background)));
//            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
//        onCreateDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        return onCreateDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SearchDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogSearchContactBinding.inflate(LayoutInflater.from(activity), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);
        initView();
    }

    public void initView() {

        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        binding.etSearchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    binding.ivClose.setVisibility(View.VISIBLE);
                } else {
                    binding.ivClose.setVisibility(View.GONE);
                }
                searchContact(StringsKt.trim(editable.toString()).toString().toLowerCase(Locale.ROOT));
            }
        });

        binding.ivClose.setOnClickListener(view -> {
            binding.etSearchContact.setText("");
        });

        mContactViewModel = (ContactViewModel) new ViewModelProvider(this).get(ContactViewModel.class);
        ContactDatabase.Companion companion = ContactDatabase.Companion;
        mContactDatabase = companion.invoke(activity);
        adapterSearchContact = new AdapterSearchContact(activity);

        binding.rvContacts.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvContacts.setAdapter(adapterSearchContact);
        adapterSearchContact.setOnItemClick(new OnClickContact() {
            @Override
            public void onClick(Contact contact) {
                startActivity(new Intent(activity, ActivityContactInformation.class).putExtra("selectedContact", contact));
            }
        });

//        mContactViewModel.getStateOfDeleteContact().observe(this, new ContactsFragment$sam$androidx_lifecycle_Observer$0(new Function1<Boolean, Unit>() {
//            @Override
//            public Unit invoke(Boolean bool) {
////                if (!bool || mOnLongClickEnabled == null) {
////                    Toast.makeText(getActivity(), getActivity().getString(R.string.toast_number_not_delete), Toast.LENGTH_SHORT).show();
////                } else {
////                    if (mOnLongClickEnabled != null) {
////                        mOnLongClickEnabled.onLongClickClose();
////                    }
////                }
//                return Unit.INSTANCE;
//            }
//        }));


        mContactViewModel.loadRawContact(requireContext(), mContactDatabase);
        mContactViewModel.getStateOfContacts().observe(this, new Observer<List<ListObject>>() {
            @Override
            public void onChanged(List<ListObject> list) {
                renderData(list);
            }
        });
//        mContactViewModel.getTotalContactInAccount().observe(this, new ContactsFragment$sam$androidx_lifecycle_Observer$0(new Function1<Integer, Unit>() {
//            @Override
//            public Unit invoke(Integer num) {
////                if (mOnContactCountListener != null) {
////                    mOnContactCountListener.onCount(num);
////                }
//                return Unit.INSTANCE;
//            }
//        }));
        try {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new ObserveContact(new OnContactChange() {
                    @Override
                    public void onContactChange() {
                        handleResume();
                    }
                }));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void handleResume() {
        if (mContactViewModel != null) {
            mContactViewModel.loadRawContact(requireContext(), mContactDatabase);
        }
    }

    public void searchContact(final String obj) {
        final List list = new ArrayList();
//        final Iterator<ListObject> iterator = contactList.iterator();
//        while (true) {
//            final boolean hasNext = iterator.hasNext();
//            if (!hasNext) {
//                break;
//            }
//            final ListObject listObject = iterator.next();
//            final int type = listObject.getType();
//            final boolean b = false;
//            if (type == 3) {
//                if (!StringsKt.equals(obj, "", false)) {
//                    continue;
//                }
//                if (obj != null) {
//                    continue;
//                }
//            }
//            Contact mContact;
//            if (listObject.getType() == 1) {
//                mContact = (Contact) listObject;
//            } else {
//                mContact = ((HeaderModel) listObject).getMContact();
//            }
//            final String lowerCase = mContact.getFirstName().toLowerCase(Locale.ROOT);
//            if (!StringsKt.contains((CharSequence) lowerCase, (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getSurName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false)) {
//                final Iterable iterable = mContact.getContactNumber();
//                boolean b2 = false;
//                Label_0360:
//                {
//                    if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
//                        final Iterator iterator2 = iterable.iterator();
//                        while (iterator2.hasNext()) {
//                            if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator2.next()).getNormalizedNumber(), (CharSequence) String.valueOf(obj), false)) {
//                                b2 = true;
//                                break Label_0360;
//                            }
//                        }
//                    }
//                    b2 = false;
//                }
//                if (!b2) {
//                    final Iterable iterable2 = mContact.getContactNumber();
//                    int n = 0;
//                    Label_0455:
//                    {
//                        if (iterable2 instanceof Collection && ((Collection) iterable2).isEmpty()) {
//                            n = (b ? 1 : 0);
//                        } else {
//                            final Iterator iterator3 = iterable2.iterator();
//                            do {
//                                n = (b ? 1 : 0);
//                                if (iterator3.hasNext()) {
//                                    continue;
//                                }
//                                break Label_0455;
//                            } while (!StringsKt.contains((CharSequence) ((PhoneNumber) iterator3.next()).getValue(), (CharSequence) String.valueOf(obj), false));
//                            n = 1;
//                        }
//                    }
//                    if (n == 0) {
//                        continue;
//                    }
//                }
//            }
//            list.add(listObject);
//        }

        if (contactList != null && contactList.size() > 0) {

            for (int i = 0; i < contactList.size(); i++) {
                Contact mContact;
                if (contactList.get(i).getType() == 1) {
                    mContact = (Contact) contactList.get(i);
                } else {
                    mContact = ((HeaderModel) contactList.get(i)).getMContact();
                }
                String lowerCase = mContact.getNameToDisplay().toLowerCase(Locale.ROOT);

                if (lowerCase.contains(obj)) {
                    list.add(contactList.get(i));
                }

            }

        }

        if (adapterSearchContact != null) {
            if (obj != null) {
                if (list != null && list.size() > 0) {
                    ViewExtensionUtils.gone(binding.llNoData);
                    ViewExtensionUtils.show(binding.rvContacts);
                    if (adapterSearchContact != null) {
                        adapterSearchContact.updateList(list, obj);
                    }
                } else {
                    ViewExtensionUtils.show(binding.llNoData);
                    ViewExtensionUtils.gone(binding.rvContacts);
                }
            } else {
                if (contactList != null && contactList.size() > 0) {
                    ViewExtensionUtils.gone(binding.llNoData);
                    ViewExtensionUtils.show(binding.rvContacts);
                } else {
                    ViewExtensionUtils.show(binding.llNoData);
                    ViewExtensionUtils.gone(binding.rvContacts);
                }
                if (adapterSearchContact != null) {
                    adapterSearchContact.updateList(TypeIntrinsics.asMutableList(contactList), "");
                }
            }
        }
    }

    public void renderData(List<ListObject> list) {
        Log.e("itSize ", String.valueOf(list.size()));
        ViewExtensionUtils.gone(binding.loadingBar);
        if (!list.isEmpty()) {
            contactList = (ArrayList) list;
            ViewExtensionUtils.show(binding.rvContacts);
            ViewExtensionUtils.gone(binding.llNoData);
            binding.rvContacts.setItemViewCacheSize(list.size());
            if (adapterSearchContact != null) {
                adapterSearchContact.setData(TypeIntrinsics.asMutableList(list));
            }
        } else {
            ViewExtensionUtils.gone(binding.rvContacts);
            ViewExtensionUtils.show(binding.llNoData);
        }
    }

}
