package com.contacts.phonecontact.phonebook.dialer.Dialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;

import com.contacts.phonecontact.phonebook.dialer.Adapters.AdapterAccountList;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.databinding.PopupSelectAccountForSavingBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAccountSelectionChange;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnSavingAccountSelect;

import java.util.List;

public class PopupSelectAccount {
    Activity activity;
    PopupSelectAccountForSavingBinding binding;
    private final View popupView;
    private final PopupWindow popupWindow;
    private List<ContactSource> accountList;
    private AdapterAccountList adapterAccountList;
    private OnSavingAccountSelect mOnHomePopupItemClick;


    public PopupSelectAccount(Activity activity) {
        this.activity = activity;
        binding = PopupSelectAccountForSavingBinding.inflate(LayoutInflater.from(activity));
        popupView = binding.getRoot();
        popupWindow = new PopupWindow(popupView, -1, -2, true);
        adapterAccountList = new AdapterAccountList(activity);
        adapterAccountList.setFromContactSaving(true);
        adapterAccountList.setOnAccountChangeListener(new OnAccountSelectionChange() {

            @Override
            public void onChange(int i) {
                String name = ((ContactSource) accountList.get(i)).getName();
                Integer color = ((ContactSource) accountList.get(i)).getColor();
                mOnHomePopupItemClick.onAccountSelect(name, i);
                popupWindow.dismiss();
            }
        });
        binding.rcvAccountList.setLayoutManager(new GridLayoutManager(binding.rcvAccountList.getContext(), 1));
        binding.rcvAccountList.setAdapter(adapterAccountList);

    }

    public final void setOnMenuItemClickListener(OnSavingAccountSelect onSavingAccountSelect) {
        this.mOnHomePopupItemClick = onSavingAccountSelect;
    }

    public final void setAccountList(List<ContactSource> list) {
        this.accountList = list;
        this.adapterAccountList.setAccountList(list);
    }

    public final void show(View view) {
        popupWindow.showAsDropDown(view);
    }

}
