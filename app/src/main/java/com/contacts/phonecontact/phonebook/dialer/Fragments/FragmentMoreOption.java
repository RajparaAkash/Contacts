package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.net.MailTo;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityAddContact;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactInfoViewModel;
import com.google.android.gms.actions.SearchIntents;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentMoreOptionBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.types.PhoneNumberType;

import java.util.ArrayList;

public class FragmentMoreOption extends Fragment {
    FragmentMoreOptionBinding binding;
    private String contactID = "";
    private String contactNumber = "";
    private ContactInfoViewModel mContactInfoViewModel;
    private Contact selectedContact;



    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String str) {
        this.contactNumber = str;
    }

    public String getContactID() {
        return this.contactID;
    }

    public void setContactID(String str) {
        this.contactID = str;
    }

    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    public void setSelectedContact(Contact contact) {
        this.selectedContact = contact;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        contactID = requireArguments().getString("contactID");
        contactNumber = requireArguments().getString("contactNumber");
        mContactInfoViewModel = (ContactInfoViewModel) new ViewModelProvider(this).get(ContactInfoViewModel.class);
        ContactDatabase.Companion companion = ContactDatabase.Companion;
        mContactInfoViewModel.getContactBySimpleId(companion.invoke(requireContext()), contactID);

        mContactInfoViewModel.getStateOfContactById().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if (contact != null) {
                    setSelectedContact(contact);
                    if (binding == null) {
                        return;
                    }
                    if (getSelectedContact() != null) {
                        binding.editIcon.setImageResource(R.drawable.ic_edit);
                        binding.editText.setText(requireContext().getString(R.string.edit_contact));
                        return;
                    }
                    binding.editIcon.setImageResource(R.drawable.ic_action_add);
                    binding.editText.setText(requireContext().getString(R.string.add_contact));
                }
            }
        });

//        mContactInfoViewModel.getStateOfContactById().observe(this, new MoreOptionFragment$sam$androidx_lifecycle_Observer$0(new Function1<Contact, Unit>() {
//
//            @Override
//            public Unit invoke(Contact contact) {
//                invoke1(contact);
//                return Unit.INSTANCE;
//            }
//
//            public void invoke1(Contact contact) {
//                if (contact != null) {
//                    setSelectedContact(contact);
//                    if (binding == null) {
//                        return;
//                    }
//                    if (getSelectedContact() != null) {
//                        binding.editIcon.setImageResource(R.drawable.ic_edit);
//                        binding.editText.setText(requireContext().getString(R.string.edit_contact));
//                        return;
//                    }
//                    binding.editIcon.setImageResource(R.drawable.ic_action_add);
//                    binding.editText.setText(requireContext().getString(R.string.add_contact));
//                }
//            }
//
//        }));

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentMoreOptionBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (selectedContact != null) {
            binding.editIcon.setImageResource(R.drawable.ic_edit);
            binding.editText.setText(requireContext().getString(R.string.edit_contact));
        } else {
            binding.editIcon.setImageResource(R.drawable.ic_action_add);
            binding.editText.setText(requireContext().getString(R.string.add_contact));
        }
        initView();
    }

    private void initView() {
        binding.editContact.setOnClickListener(view -> {
            if (selectedContact != null) {
                editContact();
            } else {
                addContact();
            }
        });
        binding.messages.setOnClickListener(view -> {
            sendMessage();
        });
        binding.calendar.setOnClickListener(view -> {
            openCalendar();
        });
        binding.sendMail.setOnClickListener(view -> {
            sendMail();
        });
        binding.web.setOnClickListener(view -> {
            openBrowser();
        });
    }

    private void addContact() {
        if (selectedContact == null) {
            selectedContact = getEmptyContact();
            selectedContact.getContactNumber().add(new PhoneNumber(contactNumber, PhoneNumberType.NO_LABEL, "", contactNumber, true));
        }
//        try {
//            startActivity(new Intent(requireActivity(), Class.forName("com.contacts.phonecontact.phonebook.dialer.presentation.Activities.ActivityAddContact")).putExtra("selectedContact", selectedContact).putExtra("isUpdate", false).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("finishActivityOnSaveCompleted", true));
        startActivity(new Intent(requireActivity(), ActivityAddContact.class).putExtra("selectedContact", selectedContact).putExtra("isUpdate", false).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("finishActivityOnSaveCompleted", true));
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void editContact() {
//        try {
//            startActivity(new Intent(requireActivity(), Class.forName("com.contacts.phonecontact.phonebook.dialer.presentation.Activities.ActivityAddContact")).putExtra("selectedContact", selectedContact).putExtra("isUpdate", true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("finishActivityOnSaveCompleted", true));
        startActivity(new Intent(requireActivity(), ActivityAddContact.class).putExtra("selectedContact", selectedContact).putExtra("isUpdate", true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("finishActivityOnSaveCompleted", true));
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void sendMessage() {
        try {
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("smsto:" + contactNumber));
            intent.putExtra("sms_body", "");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMail() {
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
        intent.putExtra("android.intent.extra.EMAIL", "");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(getContext(), getString(R.string.toast_no_email_clients), Toast.LENGTH_SHORT).show();
        }
    }

    private void openCalendar() {
        try {
            Intent intent = new Intent("android.intent.action.INSERT");
            intent.setType("vnd.android.cursor.item/event");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openBrowser() {
        try {
            Intent intent = new Intent("android.intent.action.WEB_SEARCH");
            intent.putExtra(SearchIntents.EXTRA_QUERY, contactNumber);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Contact getEmptyContact() {
        return new Contact(null, 0, 0, "", "", "", "", "", "", null, "", false, "", new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), "", new ArrayList(), "", "", "", null, Integer.valueOf(ContextCompat.getColor(requireContext(), R.color.app_color)), 16777217, null);
    }

}
