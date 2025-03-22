package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.databinding.ItemContactWebsitesBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;

import java.util.List;

import kotlin.collections.CollectionsKt;

public class AdapterWebsites extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> phoneNumberList = CollectionsKt.emptyList();

    public void setPhoneList(List<String> list) {
        this.phoneNumberList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PhoneNumberHolder(ItemContactWebsitesBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PhoneNumberHolder phoneNumberHolder = (PhoneNumberHolder) viewHolder;
        if (phoneNumberHolder.getAdapterPosition() == 0) {
            ViewExtensionUtils.show(phoneNumberHolder.binding.ivCall);
        } else {
            ViewExtensionUtils.invisible(phoneNumberHolder.binding.ivCall);
        }
        phoneNumberHolder.binding.tvNumber.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return phoneNumberList.size();
    }

    public static class PhoneNumberHolder extends RecyclerView.ViewHolder {
        ItemContactWebsitesBinding binding;

        public PhoneNumberHolder(ItemContactWebsitesBinding itemContactWebsitesBinding) {
            super(itemContactWebsitesBinding.getRoot());
            this.binding = itemContactWebsitesBinding;
        }

    }

}
