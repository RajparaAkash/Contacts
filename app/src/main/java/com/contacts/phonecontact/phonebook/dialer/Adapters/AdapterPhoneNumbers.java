package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemContactPhoneNumbersBinding;
import com.contacts.phonecontact.phonebook.dialer.Utils.ContaxtExtUtils;

import java.util.List;

import kotlin.collections.CollectionsKt;

public class AdapterPhoneNumbers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PhoneNumber> phoneNumberList = CollectionsKt.emptyList();

    public void setPhoneList(List<PhoneNumber> list) {
        this.phoneNumberList = list;
        notifyDataSetChanged();
    }

    public List<PhoneNumber> getPhoneList() {
        return phoneNumberList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PhoneNumberHolder(ItemContactPhoneNumbersBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PhoneNumberHolder phoneNumberHolder = (PhoneNumberHolder) viewHolder;
        if (phoneNumberHolder.getAdapterPosition() == 0) {
//            ViewExtensionKt.show(phoneNumberHolder.binding.ivCall);
//            ViewExtensionKt.show(phoneNumberHolder.binding.btnSendTextMessage);
            phoneNumberHolder.binding.tvNumberType.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getType().getPhoneType() + " · Default");
        } else {
            phoneNumberHolder.binding.tvNumberType.setText(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getType().getPhoneType());
        }


        phoneNumberHolder.binding.tvNumber.setText(PhoneNumberUtils.formatNumber(phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue(), "IN"));
        phoneNumberHolder.binding.ivCall.setOnClickListener(view -> {
            Context context = phoneNumberHolder.binding.getRoot().getContext();
            ContaxtExtUtils.makeACall(context, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue());
        });
        phoneNumberHolder.binding.numberLayout.setOnClickListener(view -> {
            Context context = phoneNumberHolder.binding.getRoot().getContext();
            ContaxtExtUtils.makeACall(context, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue());
        });
        phoneNumberHolder.binding.btnMakeVideoCall.setOnClickListener(view -> {
            Context context = phoneNumberHolder.binding.getRoot().getContext();
            ContaxtExtUtils.makeAVideoCall(context, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue());
        });
        phoneNumberHolder.binding.btnSendTextMessage.setOnClickListener(view -> {
            Context context = phoneNumberHolder.binding.getRoot().getContext();
            ContaxtExtUtils.sendTextMessage(context, phoneNumberList.get(phoneNumberHolder.getAdapterPosition()).getValue());
        });

    }

    @Override
    public int getItemCount() {
        return phoneNumberList.size();
    }

    public static class PhoneNumberHolder extends RecyclerView.ViewHolder {
        ItemContactPhoneNumbersBinding binding;

        public PhoneNumberHolder(ItemContactPhoneNumbersBinding itemContactPhoneNumbersBinding) {
            super(itemContactPhoneNumbersBinding.getRoot());
            this.binding = itemContactPhoneNumbersBinding;
        }
    }
}
