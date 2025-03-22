package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactHistory;
import com.contacts.phonecontact.phonebook.dialer.Activities.MainActivity;
import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAllCallLog;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ObjectCallLog;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.callback.OnContactChange;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.observer.ObserveContact;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.MyApplication;
import com.contacts.phonecontact.phonebook.dialer.Viewmodels.CallLogViewModel;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentHistoryBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnCallLogClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;

public class FragmentHistory extends FragBase<FragmentHistoryBinding> {

    public List<ObjectCallLog> callLogList = CollectionsKt.emptyList();
    public AdapterAllCallLog adapterAllCallLog;
    public CallLogViewModel mCallLogViewModel;
    public ContactDatabase mContactDatabase;
    FragmentHistoryBinding binding;
    Activity activity;

    public FragmentHistory() {
    }

    @Override
    public FragmentHistoryBinding setViewBinding() {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = requireActivity();
    }

    @Override
    public void bindObjects() {
        mCallLogViewModel = new ViewModelProvider(requireActivity()).get(CallLogViewModel.class);
        mContactDatabase = ContactDatabase.Companion.invoke(getActivity());
        mCallLogViewModel.updateHistory(getContext(), mContactDatabase);

        adapterAllCallLog = new AdapterAllCallLog(getActivity(), new AdapterAllCallLog.OnDeleteListener() {
            @Override
            public void onDeleted(CallLogModel callLogModel1) {
                mCallLogViewModel.deleteCallLog(getActivity(), callLogModel1, mContactDatabase);

            }
        });
        binding.rcvContactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvContactList.setAdapter(adapterAllCallLog);
    }

    @Override
    public void bindListener() {
        adapterAllCallLog.setOnCallLogClick(new OnCallLogClick() {
            @Override
            public void onClick(CallLogModel callLogModel) {
                Intent intent = new Intent(getActivity(), ActivityContactHistory.class);
                intent.putExtra("mSelectedCallLogModel", callLogModel);
                startActivity(intent);
            }
        });
    }

    @Override
    public void bindMethod() {
        mCallLogViewModel.getHistoryListState().observe(this, new Observer<List<ObjectCallLog>>() {
            @Override
            public void onChanged(List<ObjectCallLog> list) {
                callLogList = list;
                if (callLogList != null && !callLogList.isEmpty()) {
                    try {
                        MyApplication.getInstance().setLastDialNumber(((CallLogModel) callLogList.get(1)).getPhoneNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    MyApplication.getInstance().setLastDialNumber("");
                }

                if (!callLogList.isEmpty()) {
                    ViewExtensionUtils.gone(binding.noData);
                    ViewExtensionUtils.show(binding.rcvContactList);
                    adapterAllCallLog.setData(requireContext(), TypeIntrinsics.asMutableList(callLogList));
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    ViewExtensionUtils.gone(binding.rcvContactList);
                    ViewExtensionUtils.show(binding.noData);
                    binding.progressBar.setVisibility(View.GONE);
                }

                try {
                    ((MainActivity) getActivity()).hideProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (getActivity() != null) {
                getActivity().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new ObserveContact(new OnContactChange() {
                    @Override
                    public void onContactChange() {
                        mCallLogViewModel.updateHistory(getContext(), mContactDatabase);
                    }
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getActivity() != null) {
                getActivity().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new ObserveContact(new OnContactChange() {
                    @Override
                    public void onContactChange() {
                        mCallLogViewModel.updateHistory(getContext(), mContactDatabase);
                    }
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCallLog(String str) {
        if (!str.isEmpty() && callLogList != null && !callLogList.isEmpty()) {
            ArrayList<ObjectCallLog> arrayList = new ArrayList<>();
            for (ObjectCallLog objectCallLog : callLogList) {
                if (objectCallLog.getType() == 1) {
                    CallLogModel callLogModel = (CallLogModel) objectCallLog;
                    String lowerCase = callLogModel.getName().toLowerCase(Locale.ROOT);
                    if (StringsKt.contains(lowerCase, str, false) || StringsKt.contains(String.valueOf(callLogModel.getPhoneNumber()), str, false)) {
                        arrayList.add(objectCallLog);
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                ViewExtensionUtils.gone(binding.noData);
                ViewExtensionUtils.show(binding.rcvContactList);
                if (adapterAllCallLog != null) {
                    adapterAllCallLog.updateList(arrayList, str);
                }
            } else if (getBinding() != null) {
                ViewExtensionUtils.show(binding.noData);
                ViewExtensionUtils.gone(binding.rcvContactList);
            }
        } else if (getBinding() != null) {
            if (callLogList != null && !callLogList.isEmpty()) {
                ViewExtensionUtils.gone(binding.noData);
                ViewExtensionUtils.show(binding.rcvContactList);
            } else {
                ViewExtensionUtils.show(binding.noData);
                ViewExtensionUtils.gone(binding.rcvContactList);
            }
            if (adapterAllCallLog != null && (!callLogList.isEmpty())) {
                adapterAllCallLog.setData(getContext(), TypeIntrinsics.asMutableList(callLogList));
            }
        }
    }

    public void filterCallLog(int sim, String calls) {
        if (callLogList != null && !callLogList.isEmpty()) {
            ArrayList<ObjectCallLog> arrayList = new ArrayList<>();
            for (ObjectCallLog objectCallLog : callLogList) {
                if (objectCallLog.getType() == 1) {
                    CallLogModel callLogModel = (CallLogModel) objectCallLog;

                    if (calls.equals("All Calls")) {
                        if (sim == 0) {
                            arrayList.add(objectCallLog);
                        } else {
                            if (callLogModel.getSimID() == sim) {
                                arrayList.add(objectCallLog);
                            }
                        }
                    } else {
                        if (sim == 0 && callLogModel.getCallLogType().equals(calls)) {
                            arrayList.add(objectCallLog);
                        } else if (callLogModel.getSimID() == sim && callLogModel.getCallLogType().equals(calls)) {
                            arrayList.add(objectCallLog);
                        }
                    }

                }
            }
            if (!arrayList.isEmpty()) {
                ViewExtensionUtils.gone(binding.noData);
                ViewExtensionUtils.show(binding.rcvContactList);
                if (adapterAllCallLog != null) {
                    adapterAllCallLog.updateList(arrayList);
                }
            } else if (getBinding() != null) {
                ViewExtensionUtils.show(binding.noData);
                ViewExtensionUtils.gone(binding.rcvContactList);
            }
        } else if (getBinding() != null) {
            ViewExtensionUtils.show(binding.noData);
            ViewExtensionUtils.gone(binding.rcvContactList);
        }
    }

    public void hideSwipe() {
        if (adapterAllCallLog != null) {
            adapterAllCallLog.hideSwipeLayout();
        }
    }

    public List<ObjectCallLog> getHistoryList() {
        return callLogList;
    }

    public void deleteAllCalls() {
        try {
            getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mContactDatabase.callLogDAO().deleteAllCallLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCallLogViewModel.updateHistory(getContext(), mContactDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterAllCallLog != null && adapterAllCallLog.getSelList() != null && !adapterAllCallLog.getSelList().isEmpty()) {
            adapterAllCallLog.notifyItemChanged(adapterAllCallLog.getSelList().get(0));
        }
    }
}
