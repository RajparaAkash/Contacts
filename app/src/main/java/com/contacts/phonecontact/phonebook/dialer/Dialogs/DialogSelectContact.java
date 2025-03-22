package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.databinding.DialogSearchContactBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.observer.ObserveContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterSearchContact;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class DialogSelectContact extends DialogFragment {

    Activity activity;
    DialogSearchContactBinding binding;
    OnContactSelectListener listener;
    private List<ListObject> contactList = CollectionsKt.emptyList();
    private AdapterSearchContact adapterSearchContact;
    private ContactDatabase mContactDatabase;
    private ContactViewModel mContactViewModel;

    public DialogSelectContact(Activity activity, OnContactSelectListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setStatusBarColor(activity.getColor(R.color.background));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.background)));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return super.onCreateDialog(bundle);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SearchDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = DialogSearchContactBinding.inflate(LayoutInflater.from(activity), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view2, Bundle bundle) {
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
                if (!editable.toString().trim().isEmpty()) {
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
                listener.onSelected(contact);
                dismiss();
            }
        });

        mContactViewModel.loadRawContact(requireContext(), mContactDatabase);
        mContactViewModel.getStateOfContacts().observe(this, new Observer<List<ListObject>>() {
            @Override
            public void onChanged(List<ListObject> list) {
                renderData(list);
            }
        });

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

        if (contactList != null && !contactList.isEmpty()) {

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
                if (list != null && !list.isEmpty()) {
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
                ViewExtensionUtils.gone(binding.llNoData);
                ViewExtensionUtils.show(binding.rvContacts);
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
            this.contactList = (ArrayList) list;
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

    public interface OnContactSelectListener {
        void onSelected(Contact contact);
    }
}
