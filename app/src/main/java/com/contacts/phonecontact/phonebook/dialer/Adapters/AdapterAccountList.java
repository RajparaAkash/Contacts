package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.ContactSource;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemAccountBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemAccountSelectedBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnAccountSelectionChange;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;

public class AdapterAccountList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final int normalContactType = 1;
    private List<ContactSource> accountList = CollectionsKt.emptyList();
    private boolean isFromContactSaving;
    private OnAccountSelectionChange mOnAccountSelectionChange;
    private int selectedContactType;

    public AdapterAccountList(Context context2) {
        this.context = context2;
    }

    public Context getContext() {
        return this.context;
    }

    public boolean isFromContactSaving() {
        return this.isFromContactSaving;
    }

    public void setFromContactSaving(boolean z) {
        this.isFromContactSaving = z;
    }

    public void setOnAccountChangeListener(OnAccountSelectionChange onAccountSelectionChange) {
        this.mOnAccountSelectionChange = onAccountSelectionChange;
    }

    public void setAccountList(List<ContactSource> list) {
        this.accountList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == selectedContactType) {
            ItemAccountSelectedBinding inflate = ItemAccountSelectedBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new AccountListHolderSelected(inflate);
        } else if (i == normalContactType) {
            ItemAccountBinding inflate2 = ItemAccountBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new AccountListHolder(inflate2);
        } else {
            ItemAccountBinding inflate3 = ItemAccountBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
            return new AccountListHolder(inflate3);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        String str = "Phone storage";
        if (itemViewType == normalContactType) {
            AccountListHolder accountListHolder = (AccountListHolder) viewHolder;
            accountListHolder.binding.itemTvContactName.setText(accountList.get(accountListHolder.getAdapterPosition()).getPublicName());
            Integer color = accountList.get(accountListHolder.getAdapterPosition()).getColor();
            if (color == -592138) {
                accountListHolder.binding.ivThumbImage.setColorFilter(ContextCompat.getColor(context, R.color.gray_color_text));
            } else {
                CircleImageView circleImageView = accountListHolder.binding.ivThumbImage;
                Integer color2 = this.accountList.get(accountListHolder.getAdapterPosition()).getColor();
                circleImageView.setColorFilter(color2);
            }
            if (StringsKt.contains((CharSequence) accountList.get(i).getPublicName(), (CharSequence) "@", false)) {
                str = accountList.get(accountListHolder.getAdapterPosition()).getPublicName().substring(0, StringsKt.indexOf((CharSequence) accountList.get(accountListHolder.getAdapterPosition()).getPublicName(), "@", 0, false));
            }
            StringBuilder sb = new StringBuilder();
            String substring = str.substring(0, 1);
            String upperCase = substring.toUpperCase(Locale.ROOT);
            sb.append(upperCase);
            String substring2 = str.substring(1);
            String lowerCase = substring2.toLowerCase(Locale.ROOT);
            sb.append(lowerCase);
            String sb2 = sb.toString();
            accountListHolder.binding.tvName.setText(sb2);
            if (sb2.length() > 0) {
                accountListHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(sb2.charAt(0)));
            }
            accountListHolder.binding.getRoot().setOnClickListener(view -> {
                mOnAccountSelectionChange.onChange(accountListHolder.getAdapterPosition());
            });
        } else if (itemViewType == selectedContactType) {
            AccountListHolderSelected accountListHolderSelected = (AccountListHolderSelected) viewHolder;
            accountListHolderSelected.binding.itemTvContactName.setText(accountList.get(accountListHolderSelected.getAdapterPosition()).getPublicName());
            Integer color3 = accountList.get(accountListHolderSelected.getAdapterPosition()).getColor();
            if (color3 == -592138) {
                accountListHolderSelected.binding.ivThumbImage.setColorFilter(ContextCompat.getColor(context, R.color.gray_color_text));
            } else {
                CircleImageView circleImageView2 = accountListHolderSelected.binding.ivThumbImage;
                Integer color4 = accountList.get(accountListHolderSelected.getAdapterPosition()).getColor();
                circleImageView2.setColorFilter(color4);
            }
            if (StringsKt.contains((CharSequence) accountList.get(i).getPublicName(), (CharSequence) "@", false)) {
                str = accountList.get(accountListHolderSelected.getAdapterPosition()).getPublicName().substring(0, StringsKt.indexOf((CharSequence) accountList.get(accountListHolderSelected.getAdapterPosition()).getPublicName(), "@", 0, false));
            }
            StringBuilder sb3 = new StringBuilder();
            String substring3 = str.substring(0, 1);
            String upperCase2 = substring3.toUpperCase(Locale.ROOT);
            sb3.append(upperCase2);
            String substring4 = str.substring(1);
            String lowerCase2 = substring4.toLowerCase(Locale.ROOT);
            sb3.append(lowerCase2);
            String sb4 = sb3.toString();
            accountListHolderSelected.binding.tvName.setText(sb4);
            if (sb4.length() > 0) {
                accountListHolderSelected.binding.itemTvContactFirstLetter.setText(String.valueOf(sb4.charAt(0)));
            }
            accountListHolderSelected.binding.getRoot().setOnClickListener(view -> {
                mOnAccountSelectionChange.onChange(0);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.accountList.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (this.isFromContactSaving) {
            return this.normalContactType;
        }
        if (i == 0) {
            return this.selectedContactType;
        }
        return this.normalContactType;
    }

    public static class AccountListHolderSelected extends RecyclerView.ViewHolder {
        ItemAccountSelectedBinding binding;

        public AccountListHolderSelected(ItemAccountSelectedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public static class AccountListHolder extends RecyclerView.ViewHolder {
        ItemAccountBinding binding;

        public AccountListHolder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
