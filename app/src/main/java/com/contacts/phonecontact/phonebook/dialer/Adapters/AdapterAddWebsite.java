package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemWebsiteBinding;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAddNewPhoneNumberFields;
import com.contacts.phonecontact.phonebook.dialer.types.EventType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

public class AdapterAddWebsite extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    public OnAddNewPhoneNumberFields mOnAddNewPhoneNumberFields;
    public List<Integer> nextFieldAddedPosition = new ArrayList();
    public List<String> phoneNumberList = new ArrayList();
    private Calendar mCalendar;

    OnFocusChangeListener listener;

    public interface OnFocusChangeListener {
        void onChanged(boolean hasFocus);
    }

    public AdapterAddWebsite(Context context2) {
        this.context = context2;
        Calendar instance = Calendar.getInstance();
        this.mCalendar = instance;
    }


    public Context getContext() {
        return this.context;
    }

    public void setONNewPhoneNumberFiledAdded(OnAddNewPhoneNumberFields onAddNewPhoneNumberFields) {
        this.mOnAddNewPhoneNumberFields = onAddNewPhoneNumberFields;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.listener = listener;
    }

    public void setPhoneList(List<String> list, List<Integer> list2) {
        this.phoneNumberList = list;
        this.nextFieldAddedPosition = list2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemWebsiteBinding inflate = ItemWebsiteBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new AccountListHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        AccountListHolder accountListHolder = (AccountListHolder) viewHolder;
//        accountListHolder.binding.edtPhoneNumber.setHint(phoneNumberList.get(accountListHolder.getAdapterPosition()));
        accountListHolder.binding.edtPhoneNumber.setText(phoneNumberList.get(accountListHolder.getAdapterPosition()));

        if (phoneNumberList.get(accountListHolder.getAdapterPosition()).isEmpty()) {
            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
        } else {
            accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
        }


        if (accountListHolder.getAdapterPosition() == 0) {
            ViewExtensionUtils.invisible(accountListHolder.binding.btnDeletePhoneNumber);
        } else {
            ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
        }
        accountListHolder.binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (accountListHolder.binding.edtPhoneNumber.hasFocus()) {
                    System.out.println((Object) "inside website adapter : hase focus now");
                    phoneNumberList.set(accountListHolder.getAdapterPosition(), String.valueOf(editable));
                    if (nextFieldAddedPosition.contains(accountListHolder.getAdapterPosition()) || Intrinsics.areEqual(phoneNumberList.get(accountListHolder.getAdapterPosition()), "")) {
//                        ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
                        return;
                    }
//                    ViewExtensionUtils.show(accountListHolder.binding.btnDeletePhoneNumber);
//                    nextFieldAddedPosition.add(accountListHolder.getAdapterPosition());
//                    if (mOnAddNewPhoneNumberFields != null) {
//                        mOnAddNewPhoneNumberFields.onAdd(accountListHolder.getAdapterPosition());
//                    }
                }

            }
        });

        accountListHolder.binding.edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listener.onChanged(hasFocus);

                if (hasFocus) {
                    accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                } else {
                    if (phoneNumberList.get(0).isEmpty()) {
                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#A1A1A1")));
                    } else {
                        accountListHolder.binding.edtPhoneNumber.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.app_color)));
                    }
                }

            }
        });


        accountListHolder.binding.btnDeletePhoneNumber.setOnClickListener(view -> {
//            if (accountListHolder.getAdapterPosition() > 0) {
            if (phoneNumberList.size() > 1) {
                phoneNumberList.remove(accountListHolder.getAdapterPosition());
                IntRange until = RangesKt.until(0, getItemCount() - 1);
                nextFieldAddedPosition.clear();
                nextFieldAddedPosition.addAll(CollectionsKt.toList(until));
                accountListHolder.binding.edtPhoneNumber.clearFocus();
                notifyItemRemoved(accountListHolder.getAdapterPosition());
                notifyItemRangeChanged(accountListHolder.getAdapterPosition(), getItemCount());
            } else {
                phoneNumberList.set(accountListHolder.getAdapterPosition(), "");
                notifyItemChanged(accountListHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.phoneNumberList.size();
    }

    public static class AccountListHolder extends RecyclerView.ViewHolder {
        ItemWebsiteBinding binding;

        public AccountListHolder(ItemWebsiteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
