package com.contacts.phonecontact.phonebook.dialer.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.contacts.phonecontact.phonebook.dialer.Activities.ActivityContactHistory;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.CallLogData.CallLogFetcher;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.CallData;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Model.CallLog;
import com.contacts.phonecontact.phonebook.dialer.AllModels.CallLogModel;
import com.contacts.phonecontact.phonebook.dialer.BuildConfig;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.database.ContactDatabase;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.DateUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.AppUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ConstantsUtils;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;
import com.contacts.phonecontact.phonebook.dialer.databinding.FragmentAfterCallBinding;

import java.util.ArrayList;
import java.util.Random;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.StringsKt;

public class FragmentAfterCall extends FragBase<FragmentAfterCallBinding> {

    public SharedPreferences contactAppPreference;
    FragmentAfterCallBinding binding;
    private String contactID = "";
    private String contactName = "";
    private String contactNumber = "";
    private int incomingCallCount;
    private ContactDatabase mDatabase;
    private int missedCallCount;
    private int outGoingCallCount;

    @Override
    public void bindMethod() {
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String str) {
        this.contactNumber = str;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String str) {
        this.contactName = str;
    }

    public String getContactID() {
        return this.contactID;
    }

    public void setContactID(String str) {
        this.contactID = str;
    }

    public int getMissedCallCount() {
        return this.missedCallCount;
    }

    public void setMissedCallCount(int i) {
        this.missedCallCount = i;
    }

    public int getIncomingCallCount() {
        return this.incomingCallCount;
    }

    public void setIncomingCallCount(int i) {
        this.incomingCallCount = i;
    }

    public int getOutGoingCallCount() {
        return this.outGoingCallCount;
    }

    public void setOutGoingCallCount(int i) {
        this.outGoingCallCount = i;
    }

    public SharedPreferences getContactAppPreference() {
        if (contactAppPreference != null) {
            return contactAppPreference;
        }
        return null;
    }

    public void setContactAppPreference(SharedPreferences sharedPreferences) {
        this.contactAppPreference = sharedPreferences;
    }

    @Override
    public FragmentAfterCallBinding setViewBinding() {
        binding = FragmentAfterCallBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void bindObjects() {
        contactNumber = requireArguments().getString("contactNumber");
        contactName = requireArguments().getString("contactName");
        contactID = requireArguments().getString("contactID");
        setContactAppPreference(ContaxtExtUtils.getContactAppPreference(requireActivity()));
        ContactDatabase.Companion companion = ContactDatabase.Companion;
        this.mDatabase = companion.invoke(requireContext());
        binding.contactName.setText(this.contactName);
        getTodayMissedCallData();
        if (AppUtils.isNetworkAvailable(requireContext())) {
            try {
                uploadCallCountData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void bindListener() {
        binding.helpLayout.setOnClickListener(view -> {
            AppUtils.shareApp(requireActivity());
        });
        binding.totalCallLayout.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), ActivityContactHistory.class).putExtra("mSelectedCallLogModel", getCallLog()));
        });
        binding.missedCallLayout.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), ActivityContactHistory.class).putExtra("mSelectedCallLogModel", getCallLog()));
        });
    }

    private CallLogModel getCallLog() {
        int[] intArray = getResources().getIntArray(R.array.thumb_color);
        boolean z = true;
        int nextInt = new Random().nextInt(intArray.length - 1);
        String str = this.contactName;
        if (!(str == null || str.length() == 0)) {
            z = false;
        }
        String str2 = !z ? this.contactName : this.contactNumber;
        String str3 = this.contactID;
        String str4 = this.contactNumber;
        return new CallLogModel(null, 0, str3, str4, str2, "", 0, 0, 0, 0, str4, "", "", 0, Integer.valueOf(intArray[nextInt]), 1, null);
    }

    private void sendToMixpanel(String str) {
        /*if (PhoneStateReceiver1.isIncomingCallEventSend) {
        }*/
    }

    public void uploadCallCountData() {
        Long l = null;
        Integer num = null;
        Long l2 = null;
        String formatDate = DateUtils.formatDate(System.currentTimeMillis());
        FragmentActivity requireActivity = requireActivity();
        SharedPreferences contactAppPreference2 = ContaxtExtUtils.getContactAppPreference(requireActivity);
        Long l3 = 0L;
        KClass orCreateKotlinClass = Reflection.getOrCreateKotlinClass(Long.class);
        if (Intrinsics.areEqual(orCreateKotlinClass, Reflection.getOrCreateKotlinClass(Long.TYPE))) {
            l = Long.valueOf(contactAppPreference2.getLong(ConstantsUtils.PREF_APP_UPDATE_DATE, l3 != null ? l3.longValue() : 0));
        }
        DateUtils.formatDate(l.longValue());
        SharedPreferences contactAppPreference3 = ContaxtExtUtils.getContactAppPreference(requireActivity());
        KClass orCreateKotlinClass2 = Reflection.getOrCreateKotlinClass(String.class);
        if (Intrinsics.areEqual(orCreateKotlinClass2, Reflection.getOrCreateKotlinClass(String.class))) {
            if (contactAppPreference3.getString(ConstantsUtils.PREF_APP_VERSION, BuildConfig.VERSION_NAME) == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
        }
        SharedPreferences contactAppPreference4 = ContaxtExtUtils.getContactAppPreference(requireActivity());
        Integer num5 = 0;
        KClass orCreateKotlinClass3 = Reflection.getOrCreateKotlinClass(Integer.class);
        if (Intrinsics.areEqual(orCreateKotlinClass3, Reflection.getOrCreateKotlinClass(Integer.TYPE))) {
            num = Integer.valueOf(contactAppPreference4.getInt(ConstantsUtils.PREF_CDO_SHOW_COUNT, num5 != null ? num5.intValue() : 0));
        }
        num.intValue();
        SharedPreferences contactAppPreference5 = ContaxtExtUtils.getContactAppPreference(requireActivity());
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        KClass orCreateKotlinClass4 = Reflection.getOrCreateKotlinClass(Long.class);
        if (Intrinsics.areEqual(orCreateKotlinClass4, Reflection.getOrCreateKotlinClass(Long.TYPE))) {
            l2 = Long.valueOf(contactAppPreference5.getLong(ConstantsUtils.PREF_CDO_COUNT_DATE, valueOf != null ? valueOf.longValue() : 0));
        }
        String formatDate2 = DateUtils.formatDate(l2.longValue());
        if (!Intrinsics.areEqual(formatDate2, formatDate)) {
            SharedPreferences.Editor edit = ContaxtExtUtils.getContactAppPreference(requireActivity()).edit();
            KClass orCreateKotlinClass5 = Reflection.getOrCreateKotlinClass(String.class);
            if (Intrinsics.areEqual(orCreateKotlinClass5, Reflection.getOrCreateKotlinClass(String.class))) {
                edit.putString(ConstantsUtils.PREF_CDO_COUNT_DATE, formatDate);
            }
            edit.commit();
        }
        Intrinsics.areEqual(formatDate2, formatDate);
    }

    private void getTodayMissedCallData() {
        try {
            String replace$default = StringsKt.replace(StringsKt.replace(contactNumber, "-", "", false), " ", "", false);
            CallData callLogs = new CallLogFetcher(requireContext()).getCallLogs(replace$default);
            ArrayList<CallLog> callLogs2 = callLogs.getCallLogs();
            incomingCallCount = callLogs.getIncomingCallCount();
            sendToMixpanel("Incoming_call_count: " + incomingCallCount);
            outGoingCallCount = callLogs.getOutgoingCallCount();
            missedCallCount = callLogs.getMissedCallCount();
            StringBuilder sb = new StringBuilder();
            sb.append(this.incomingCallCount);
            sb.append(' ');
            sb.append(this.outGoingCallCount);
            sb.append(' ');
            sb.append(this.missedCallCount);
            Log.e("incomingCallCount ------->", sb.toString());
            boolean z = true;
            if (callLogs2 != null && callLogs2.size() > 0) {
                if (callLogs2.size() > 1) {
                    binding.missedCallLayout.setVisibility(View.VISIBLE);
                    binding.contactName.setText(!(contactName == null || contactName.length() == 0) ? contactName : getString(R.string.private_number));
                    binding.contactNumber.setText(PhoneNumberUtils.formatNumber(contactNumber, "IN"));
                    binding.callTime.setText(String.valueOf(callLogs2.size()));
                } else if (callLogs2.size() == 1) {
                    binding.missedCallLayout.setVisibility(View.VISIBLE);
                    binding.contactName.setText(!(contactName == null || contactName.length() == 0) ? contactName : getString(R.string.private_number));
                    binding.contactNumber.setText(PhoneNumberUtils.formatNumber(contactNumber, "IN"));
                    binding.callTime.setText(DateUtils.convertTimeInAmPm(callLogs2.get(0).getCallTimeStamp()));
                }
            }
            String formatNumber = !(contactName == null || contactName.length() == 0) ? contactName : PhoneNumberUtils.formatNumber(contactNumber, "IN");
            if (!(formatNumber == null || formatNumber.length() == 0)) {
                binding.totalCallLayout.setVisibility(View.VISIBLE);
                binding.totalCallCount.setText(callLogs.getCallLogsCount() + " " + getString(R.string.number_of_calls_with) + " " + formatNumber + " " + getString(R.string.this_month));
            }
            if (!(contactID == null || contactID.length() == 0)) {
                z = false;
            }
            if (!z) {
                binding.helpLayout.setVisibility(View.GONE);
                return;
            }
            binding.helpLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
