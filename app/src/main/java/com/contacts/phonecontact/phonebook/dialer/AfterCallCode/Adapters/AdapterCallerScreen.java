package com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Fragments.FragmentMessage;
import com.contacts.phonecontact.phonebook.dialer.AfterCallCode.Fragments.FragmentReminder;
import com.contacts.phonecontact.phonebook.dialer.Fragments.FragmentAfterCall;
import com.contacts.phonecontact.phonebook.dialer.Fragments.FragmentMoreOption;

public class AdapterCallerScreen extends FragmentStateAdapter {

    String contactID;
    String contactName;
    String contactNumber;
    FragmentActivity fragmentActivity;

    @Override
    public int getItemCount() {
        return 4;
    }

    public AdapterCallerScreen(FragmentActivity fragmentActivity2, String str) {
        super(fragmentActivity2);
        this.contactNumber = str;
        this.fragmentActivity = fragmentActivity2;
    }

    public void setContactData(String str, String str2) {
        this.contactName = str;
        this.contactID = str2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int i) {
        if (i == 0) {
            Fragment instantiate = new FragmentAfterCall();
            Bundle bundle = new Bundle();
            bundle.putString("contactNumber", contactNumber);
            bundle.putString("contactName", contactName);
            bundle.putString("contactID", contactID);
            instantiate.setArguments(bundle);
            return instantiate;
        } else if (i == 1) {
            return FragmentMessage.getInstance(contactNumber);
        } else {
            if (i == 2) {
                return FragmentReminder.getInstance(contactNumber);
            }
            if (i != 3) {
                return new FragmentMessage();
            }
            Fragment instantiate2 = new FragmentMoreOption();
            Bundle bundle2 = new Bundle();
            bundle2.putString("contactNumber", contactNumber);
            bundle2.putString("contactName", contactName);
            bundle2.putString("contactID", contactID);
            instantiate2.setArguments(bundle2);
            return instantiate2;
        }
    }
}
