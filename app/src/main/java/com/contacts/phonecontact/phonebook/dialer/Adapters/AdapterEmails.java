package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemContactEmailBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;

import java.util.List;

import kotlin.collections.CollectionsKt;

public class AdapterEmails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Email> phoneNumberList = CollectionsKt.emptyList();

    public void setPhoneList(List<Email> list) {
        this.phoneNumberList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PhoneNumberHolder(ItemContactEmailBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PhoneNumberHolder phoneNumberHolder = (PhoneNumberHolder) viewHolder;
        if (phoneNumberHolder.getAdapterPosition() == 0) {
            ViewExtensionUtils.show(phoneNumberHolder.binding.ivCall);
            phoneNumberHolder.binding.tvNumberType.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getType().getEmailType() + " · Default");
        } else {
            ViewExtensionUtils.invisible(phoneNumberHolder.binding.ivCall);
            phoneNumberHolder.binding.tvNumberType.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getType().getEmailType());
        }
        phoneNumberHolder.binding.tvNumber.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue());
    }

    @Override
    public int getItemCount() {
        return this.phoneNumberList.size();
    }

    public static class PhoneNumberHolder extends RecyclerView.ViewHolder {
        ItemContactEmailBinding binding;

        public PhoneNumberHolder(ItemContactEmailBinding itemContactEmailBinding) {
            super(itemContactEmailBinding.getRoot());
            this.binding = itemContactEmailBinding;
        }

    }

}
