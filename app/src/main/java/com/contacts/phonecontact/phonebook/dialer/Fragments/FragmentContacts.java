package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactInformation;
import com.contacts.phonecontact.phonebook.dialer.Activities.ActivitySettings;
import com.contacts.phonecontact.phonebook.dialer.Activities.MainActivity;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAllContact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.observer.ObserveContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.ReadContact;
import com.contacts.phonecontact.phonebook.dialer.Utils.StickHeaderItemDecoration;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.ContactViewModel;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentContactsBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnContactCountListener;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnContactDataUpdate;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnLongClickEnabled;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;

public class FragmentContacts extends FragBase<FragmentContactsBinding> {

    public OnContactCountListener mOnContactCountListener;
    public OnLongClickEnabled mOnLongClickEnabled;
    FragmentContactsBinding binding;
    private List<? extends ListObject> contactList = CollectionsKt.emptyList();

    private AdapterAllContact mAdpAllContact;
    private ContactDatabase mContactDatabase;
    private ContactViewModel mContactViewModel;
    private OnContactDataUpdate mOnContactDataUpdate;
    MainActivity activity;

    public FragmentContacts() {
    }

    public FragmentContacts(MainActivity activity) {
        this.activity = activity;
    }

    public void setOnLongClickEnabled(OnLongClickEnabled onLongClickEnabled) {
        this.mOnLongClickEnabled = onLongClickEnabled;
    }

    public void setOnContactCountChange(OnContactCountListener onContactCountListener) {
        this.mOnContactCountListener = onContactCountListener;
    }

    public void setonContactDataUpdate(OnContactDataUpdate onContactDataUpdate) {
        this.mOnContactDataUpdate = onContactDataUpdate;
    }

    @Override
    public FragmentContactsBinding setViewBinding() {
        binding = FragmentContactsBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {

        mContactViewModel = (ContactViewModel) new ViewModelProvider(getActivity()).get(ContactViewModel.class);
        ContactDatabase.Companion companion = ContactDatabase.Companion;
        mContactDatabase = companion.invoke(requireContext());

        mAdpAllContact = new AdapterAllContact(requireContext());
        binding.rcvContactList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        binding.rcvContactList.setAdapter(mAdpAllContact);
        binding.rcvContactList.addItemDecoration(new StickHeaderItemDecoration(mAdpAllContact));

        binding.fastScroller.attachRecyclerView(binding.rcvContactList);

        binding.ivPremium.setOnClickListener(view -> {
            try {
                ((MainActivity) getActivity()).callPurchase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.ivAddContact.setOnClickListener(v -> {
            try {
                ((MainActivity) getActivity()).binding.ivAddContact.performClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.ivSearch.setOnClickListener(v -> {
            binding.appbar.setExpanded(false);
            try {
                ((MainActivity) getActivity()).binding.ivSearch.performClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.ivMore.setOnClickListener(this::showContactPopup);
    }

    public void showContactPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_home_contacts, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view, 0, -50);

        popupView.findViewById(R.id.tvSettings).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(getActivity(), ActivitySettings.class));
        });
    }

    private boolean isDataLoaded = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            isDataLoaded = savedInstanceState.getBoolean("isDataLoaded", false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getPremiumPurchased()) {
            binding.ivPremium.setVisibility(View.GONE);
        }
        if (!isDataLoaded && isVisible()) {
            try {
                ((MainActivity) getActivity()).goneTop(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.llDots.setVisibility(View.GONE);
            binding.fastScroller.setVisibility(View.GONE);
            binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                        // Collapsed
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((MainActivity) getActivity()).goneTop(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                binding.llDots.setVisibility(View.VISIBLE);
                                binding.fastScroller.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (verticalOffset == 0) {
                        // Expanded
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((MainActivity) getActivity()).goneTop(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                binding.llDots.setVisibility(View.GONE);
                                binding.fastScroller.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        // Somewhere in between
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((MainActivity) getActivity()).goneTop(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                binding.llDots.setVisibility(View.GONE);
                                binding.fastScroller.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });

            isDataLoaded = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDataLoaded", isDataLoaded);
    }

    @Override
    public void bindListener() {
        mAdpAllContact.setOnItemClick(new OnClickContact() {
            @Override
            public void onClick(Contact contact) {
                startActivity(new Intent(getActivity(), ActivityContactInformation.class).putExtra("selectedContact", contact));
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.overridePendingTransition(R.anim.slide_left, R.anim.no_anim);
                }
            }
        });
        mAdpAllContact.setOnLongClickEnabled(new OnLongClickEnabled() {
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

        mContactViewModel.getStateOfDeleteContact().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (!bool || mOnLongClickEnabled == null) {
                    Toast.makeText(getActivity(), getString(R.string.toast_number_not_delete), Toast.LENGTH_SHORT).show();
                } else {
                    if (mOnLongClickEnabled != null) {
                        mOnLongClickEnabled.onLongClickClose();
                    }
                }
            }
        });
    }

    public void deleteContacts() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mContactViewModel.deleteContact(activity, mAdpAllContact.getDeleteNumberList());
        }
    }

    public void shareContact() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ReadContact.shareContactList(activity, mAdpAllContact.getDeleteNumberList());
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
        if (mAdpAllContact != null) {
            mAdpAllContact.closeSelection();
        }
    }

    @Override
    public void bindMethod() {
        mContactViewModel.loadRawContact(requireContext(), mContactDatabase);
        mContactViewModel.getStateOfContacts().observe(this, new Observer<List<ListObject>>() {
            @Override
            public void onChanged(List<ListObject> list) {
                renderData(list);
            }
        });

        mContactViewModel.getTotalContactInAccount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer num) {
                binding.tvContactCount.setText(num + " " + getString(R.string.contacts_with_phone_numbers));

                if (mOnContactCountListener != null) {
                    mOnContactCountListener.onCount(num);
                }
            }
        });

        try {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                        true, new ObserveContact(new OnContactChange() {
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

    public void renderData(List<? extends ListObject> list) {
        ViewExtensionUtils.gone(binding.loadingBar);
        if (!list.isEmpty()) {
            this.contactList = (ArrayList) list;
            ViewExtensionUtils.show(binding.rcvContactList);
            ViewExtensionUtils.gone(binding.noData);
            binding.rcvContactList.setItemViewCacheSize(list.size());
            if (mAdpAllContact != null) {
                mAdpAllContact.setData(TypeIntrinsics.asMutableList(list));
            }
        } else {
            ViewExtensionUtils.gone(binding.rcvContactList);
            ViewExtensionUtils.show(binding.noData);
        }
        if (mOnContactDataUpdate != null) {
            mOnContactDataUpdate.onUpdate(mContactViewModel.getRawContacts());
        }

        try {
            ((MainActivity) getActivity()).hideProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        if (mContactViewModel != null) {
            mContactViewModel.loadContactMainThread(requireContext(), mContactDatabase);
        }
    }

    public void handleResume() {
        if (mContactViewModel != null) {
            mContactViewModel.loadRawContact(requireContext(), mContactDatabase);
        }
    }

    public void searchContact(final String obj) {
        final List<ListObject> list = new ArrayList<>();
        final Iterator<ListObject> iterator = (Iterator<ListObject>) this.contactList.iterator();

        while (iterator.hasNext()) {
            final ListObject listObject = iterator.next();
            final int type = listObject.getType();

            if (type == 3) {
                if (obj == null || obj.isEmpty()) {
                    continue;
                }
            }

            Contact mContact;
            if (listObject.getType() == 1) {
                mContact = (Contact) listObject;
            } else {
                mContact = ((HeaderModel) listObject).getMContact();
            }

            final String lowerCaseFirstName = mContact.getFirstName().toLowerCase(Locale.ROOT);
            final String lowerCaseSurName = mContact.getSurName().toLowerCase(Locale.ROOT);
            final String lowerCaseMiddleName = mContact.getMiddleName().toLowerCase(Locale.ROOT);

            if (!lowerCaseFirstName.contains(obj.toLowerCase(Locale.ROOT)) &&
                    !lowerCaseSurName.contains(obj.toLowerCase(Locale.ROOT)) &&
                    !lowerCaseMiddleName.contains(obj.toLowerCase(Locale.ROOT))) {

                boolean foundPhoneNumber = false;
                for (PhoneNumber phoneNumber : mContact.getContactNumber()) {
                    if (phoneNumber.getNormalizedNumber().contains(obj)) {
                        foundPhoneNumber = true;
                        break;
                    }
                }

                if (!foundPhoneNumber) {
                    boolean foundExactPhoneNumber = false;
                    for (PhoneNumber phoneNumber : mContact.getContactNumber()) {
                        if (phoneNumber.getValue().contains(obj)) {
                            foundExactPhoneNumber = true;
                            break;
                        }
                    }

                    if (!foundExactPhoneNumber) {
                        continue;
                    }
                }
            }

            list.add(listObject);
        }

        if (this.mAdpAllContact != null) {
            if (obj != null && !obj.isEmpty()) {
                if (!list.isEmpty()) {
                    binding.noData.setVisibility(View.GONE);
                    binding.rcvContactList.setVisibility(View.VISIBLE);
                    mAdpAllContact.updateList(list, obj);
                } else {
                    binding.noData.setVisibility(View.VISIBLE);
                    binding.rcvContactList.setVisibility(View.GONE);
                }
            } else {
                binding.noData.setVisibility(View.GONE);
                binding.rcvContactList.setVisibility(View.VISIBLE);
                mAdpAllContact.updateList(new ArrayList<>(contactList), "");
            }
        }
    }
}
