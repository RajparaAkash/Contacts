package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Activities.MainActivity;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterFavContact;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ReadContact;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.FavoriteContactsViewModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentFavoriteContactBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnLongClickEnabled;
import com.contacts.phonecontact.phonebook.dialer.Utils.StickHeaderItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class FragmentFavoriteContact extends FragBase<FragmentFavoriteContactBinding> {

    public List<? extends ListObject> contactList = CollectionsKt.emptyList();
    public AdapterFavContact adapterFavContact;
    public OnLongClickEnabled mOnLongClickEnabled;
    FragmentFavoriteContactBinding binding;
    private ContactDatabase mContactDatabase;
    private FavoriteContactsViewModel mFavoriteContactsViewModel;

    public FragmentFavoriteContact() {
    }

    public void setOnLongClickEnabled(OnLongClickEnabled onLongClickEnabled) {
        this.mOnLongClickEnabled = onLongClickEnabled;
    }

    @Override
    public FragmentFavoriteContactBinding setViewBinding() {
        binding = FragmentFavoriteContactBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        FragmentActivity activity = getActivity();
        mFavoriteContactsViewModel = (FavoriteContactsViewModel) new ViewModelProvider(activity).get(FavoriteContactsViewModel.class);
        ContactDatabase.Companion companion = ContactDatabase.Companion;
        mContactDatabase = companion.invoke(getActivity());
        adapterFavContact = new AdapterFavContact(requireContext());
        binding.rcvContactList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rcvContactList.setAdapter(adapterFavContact);
        binding.rcvContactList.addItemDecoration(new StickHeaderItemDecoration(adapterFavContact));
    }

    public void deleteContacts() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentActivity fragmentActivity = activity;
            mFavoriteContactsViewModel.deleteContact(fragmentActivity, adapterFavContact.getDeleteNumberList());
        }
    }

    public void shareContact() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentActivity fragmentActivity = activity;
            ReadContact.shareContactList(activity, adapterFavContact.getDeleteNumberList());
//            mFavoriteContactsViewModel.shareContact(fragmentActivity, adapterFavContact.getDeleteNumberList());
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnLongClickEnabled != null) {
                        mOnLongClickEnabled.onLongClickClose();
                    }
                }
            }, 500);
        }
    }

    public void closeSelection() {
        if (adapterFavContact != null) {
            adapterFavContact.closeSelection();
        }
    }

    @Override
    public void bindListener() {
        adapterFavContact.setOnItemClick(new OnClickContact() {
            @Override
            public void onClick(Contact contact) {
                startActivity(new Intent(getActivity(), ActivityContactInformation.class).putExtra("selectedContact", contact));
            }
        });
        adapterFavContact.setOnLongClickEnabled(new OnLongClickEnabled() {
            @Override
            public void onLongClick(int i) {
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(i);
                }
            }

            @Override
            public void onLongClickClose() {
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClickClose();
                }
            }
        });
        MutableLiveData<Boolean> stateOfDeleteContact = mFavoriteContactsViewModel.getStateOfDeleteContact();
        stateOfDeleteContact.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    if (mOnLongClickEnabled != null) {
                        mOnLongClickEnabled.onLongClickClose();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_number_not_delete), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void bindMethod() {
        MutableLiveData<List<ListObject>> stateOfFavoriteContacts = mFavoriteContactsViewModel.getStateOfFavoriteContacts();
        stateOfFavoriteContacts.observe(this, new Observer<List<ListObject>>() {
            @Override
            public void onChanged(List<ListObject> list) {
                contactList = list;
                if (contactList != null && contactList.size() > 0) {
                    ViewExtensionUtils.gone(binding.noData);
                    ViewExtensionUtils.show(binding.rcvContactList);

                    ArrayList arrayList = (ArrayList) list;
                    if (adapterFavContact != null) {
                        adapterFavContact.setData(TypeIntrinsics.asMutableList(arrayList));
                    }
                } else {
                    ViewExtensionUtils.show(binding.noData);
                    ViewExtensionUtils.gone(binding.rcvContactList);
                }

                try {
                    ((MainActivity) getActivity()).hideProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void handleResume(List<Contact> list) {
        if (mFavoriteContactsViewModel != null && (!list.isEmpty())) {
            FragmentActivity activity = getActivity();
            mFavoriteContactsViewModel.loadAllFavoriteContacts(activity, mContactDatabase, list);
        }
    }


    public void searchContact(final String obj) {
        final List list = new ArrayList();
        final Iterator<? extends ListObject> iterator = contactList.iterator();
        while (true) {
            final boolean hasNext = iterator.hasNext();
            if (!hasNext) {
                break;
            }
            final ListObject listObject = iterator.next();
            final int type = listObject.getType();
            final boolean b = false;
            if (type == 3) {
                if (!StringsKt.equals(obj, "", false)) {
                    continue;
                }
                if (obj != null) {
                    continue;
                }
            }
            Contact mContact;
            if (listObject.getType() == 1) {
                mContact = (Contact) listObject;
            } else {
                mContact = ((HeaderModel) listObject).getMContact();
            }
            final String lowerCase = mContact.getFirstNameOriginal().toLowerCase(Locale.ROOT);
            if (!StringsKt.contains((CharSequence) lowerCase, (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getSurName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false) && !StringsKt.contains((CharSequence) mContact.getMiddleName(), (CharSequence) String.valueOf(obj), false)) {
                final Iterable iterable = mContact.getContactNumber();
                boolean b2 = false;
                Label_0360:
                {
                    if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                        final Iterator iterator2 = iterable.iterator();
                        while (iterator2.hasNext()) {
                            if (StringsKt.contains((CharSequence) ((PhoneNumber) iterator2.next()).getNormalizedNumber(), (CharSequence) String.valueOf(obj), false)) {
                                b2 = true;
                                break Label_0360;
                            }
                        }
                    }
                    b2 = false;
                }
                if (!b2) {
                    final Iterable iterable2 = mContact.getContactNumber();
                    int n = 0;
                    Label_0455:
                    {
                        if (iterable2 instanceof Collection && ((Collection) iterable2).isEmpty()) {
                            n = (b ? 1 : 0);
                        } else {
                            final Iterator iterator3 = iterable2.iterator();
                            do {
                                n = (b ? 1 : 0);
                                if (iterator3.hasNext()) {
                                    continue;
                                }
                                break Label_0455;
                            } while (!StringsKt.contains((CharSequence) ((PhoneNumber) iterator3.next()).getValue(), (CharSequence) String.valueOf(obj), false));
                            n = 1;
                        }
                    }
                    if (n == 0) {
                        continue;
                    }
                }
            }
            list.add(listObject);
        }
        if (obj != null && obj.length() > 0) {
            if (list.size() > 0) {
                ViewExtensionUtils.gone(binding.noData);
                ViewExtensionUtils.show(binding.rcvContactList);
                if (adapterFavContact != null) {
                    adapterFavContact.updateList(list, obj);
                }
            } else if (getBinding() != null) {
                ViewExtensionUtils.show(binding.noData);
                ViewExtensionUtils.gone(binding.rcvContactList);
            }
        } else if (getBinding() != null) {
            if (adapterFavContact != null && contactList != null) {
                adapterFavContact.updateList((List<ListObject>) contactList, "");
            }
            if (contactList != null & contactList.size() > 0) {
                ViewExtensionUtils.gone(binding.noData);
                ViewExtensionUtils.show(binding.rcvContactList);
            } else {
                ViewExtensionUtils.show(binding.noData);
                ViewExtensionUtils.gone(binding.rcvContactList);
            }

        }
    }

}
