package com.contacts.phonecontact.phonebook.dialer.Adapters;

import android.content.Context;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.HeaderModel;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ListObject;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ViewExtensionUtils;
import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.Utils.StickHeaderItemDecoration;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemContactBinding;
import com.contacts.phonecontact.phonebook.dialer.databinding.ItemHeaderContactsBinding;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnClickContact;
import com.contacts.phonecontact.phonebook.dialer.interfaces.OnLongClickEnabled;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class AdapterAllContact extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickHeaderItemDecoration.StickyHeaderInterface, SectionIndexer {

    public final Context mContext;
    private List<ListObject> contactList = new ArrayList<>();
    private boolean isLongClickEnabled;
    private OnClickContact mOnClickContact;
    private OnLongClickEnabled mOnLongClickEnabled;
    public List<Integer> mSectionPositions;
    private String searchText;
    public List<Contact> selectedContactList = new ArrayList<>();

    public AdapterAllContact(Context context) {
        this.mContext = context;
        this.mSectionPositions = new ArrayList<>();
    }

    @Override
    public int getHeaderLayout(int i) {
        return R.layout.item_sticky_header;
    }

    public int getSectionForPosition(int i) {
        return 0;
    }

    public void setOnLongClickEnabled(OnLongClickEnabled onLongClickEnabled) {
        this.mOnLongClickEnabled = onLongClickEnabled;
    }

    public void setOnItemClick(OnClickContact onClickContact) {
        this.mOnClickContact = onClickContact;
    }

    public void setData(List<ListObject> list) {
        this.contactList = list;
        notifyDataSetChanged();
    }

    public List<Contact> getDeleteNumberList() {
        return this.selectedContactList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ContactHeaderHolder(ItemHeaderContactsBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        } else {
            return new ContactListHolder(ItemContactBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        }
    }

    public void closeSelection() {
        this.isLongClickEnabled = false;
        this.selectedContactList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        return this.contactList.get(i).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        String str = "(No name)";
        if (itemViewType == 0) {
            ContactHeaderHolder contactHeaderHolder = (ContactHeaderHolder) viewHolder;
            ListObject listObject = this.contactList.get(i);
            HeaderModel headerModel = (HeaderModel) listObject;

            contactHeaderHolder.binding.tvHeader.setText(headerModel.getContactHeading());

        } else if (itemViewType == 1) {

            ContactListHolder contactListHolder = (ContactListHolder) viewHolder;
            ListObject listObject3 = this.contactList.get(i);
            Contact contact = (Contact) listObject3;

            String obj2 = StringsKt.trim((CharSequence) contact.getFirstNameOriginal()).toString();

            if (!Intrinsics.areEqual(obj2, "")) {
                str = obj2;
            }
            String contactPhotoUri2 = contact.getContactPhotoUri();
            if (contactPhotoUri2 == null || contactPhotoUri2.isEmpty()) {
                if (!str.isEmpty()) {
                    contactListHolder.binding.itemTvContactFirstLetter.setText(String.valueOf(str.charAt(0)));
                }
                ViewExtensionUtils.show(contactListHolder.binding.itemTvContactFirstLetter);
                Integer bgColor2 = contact.getBgColor();
                contactListHolder.binding.ivThumbImage.setColorFilter(bgColor2);
            } else {
                contactListHolder.binding.ivThumbImage.setColorFilter((ColorFilter) null);
                ViewExtensionUtils.invisible(contactListHolder.binding.itemTvContactFirstLetter);
                Glide.with(contactListHolder.binding.getRoot().getContext())
                        .load(contact.getContactPhotoUri())
                        .into(contactListHolder.binding.ivThumbImage);
            }

            String str5 = str;
            contactListHolder.binding.itemTvContactName.setText(str5);

            if (this.selectedContactList.contains(contact)) {
                contactListHolder.binding.layoutSelect.setVisibility(View.VISIBLE);
            } else {
                contactListHolder.binding.layoutSelect.setVisibility(View.GONE);
            }

            contactListHolder.binding.getRoot().setOnClickListener(view -> {
                if (!isLongClickEnabled) {
                    if (mOnClickContact != null) {
                        mOnClickContact.onClick(contact);
                    }
                } else {
                    if (selectedContactList.contains(contact)) {
                        selectedContactList.remove(contact);
                        ViewExtensionUtils.gone(contactListHolder.binding.layoutSelect);
                    } else {
                        selectedContactList.add(contact);
                        ViewExtensionUtils.show(contactListHolder.binding.layoutSelect);
                    }
                    if (mOnLongClickEnabled != null) {
                        mOnLongClickEnabled.onLongClick(selectedContactList.size());
                    }
                }
            });

            contactListHolder.binding.getRoot().setOnLongClickListener(view -> {
                if (mOnLongClickEnabled == null) {
                    return false;
                }
                isLongClickEnabled = true;
                if (selectedContactList.contains(contact)) {
                    selectedContactList.remove(contact);
                    ViewExtensionUtils.gone(contactListHolder.binding.layoutSelect);
                } else {
                    selectedContactList.add(contact);
                    ViewExtensionUtils.show(contactListHolder.binding.layoutSelect);
                }
                if (mOnLongClickEnabled != null) {
                    mOnLongClickEnabled.onLongClick(selectedContactList.size());
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    @Override
    public int getHeaderPositionForItem(int i) {
        while (!isHeader(i)) {
            i--;
            if (i < 0) {
                return 0;
            }
        }
        return i;
    }

    @Override
    public void bindHeaderData(View view, int i) {
        if (this.contactList.get(i).getType() == 0) {
            TextView textView = view != null ? (TextView) view.findViewById(R.id.tvHeader) : null;
            if (textView != null) {
                ListObject listObject = this.contactList.get(i);
                textView.setText(((HeaderModel) listObject).getContactHeading());
            }
        }
    }

    @Override
    public boolean isHeader(int i) {
        return this.contactList.get(i).getType() == 0;
    }

    public final void updateList(List<ListObject> list, String str) {
        this.contactList = list;
        this.searchText = str;
        notifyDataSetChanged();
    }

    public String[] getSections() {
        Contact contact;
        ArrayList arrayList = new ArrayList();
        int size = this.contactList.size();
        for (int i = 0; i < size; i++) {
            try {
                if (this.contactList.get(i).getType() == 0) {
                    ListObject listObject = this.contactList.get(i);
                    contact = ((HeaderModel) listObject).getMContact();
                } else {
                    ListObject listObject2 = this.contactList.get(i);
                    contact = (Contact) listObject2;
                }
                String valueOf = "";
                if (contact.getFirstNameOriginal().length() > 0) {
                    valueOf = String.valueOf(contact.getFirstNameOriginal().charAt(0));
                }
                Locale locale = Locale.getDefault();
                String upperCase = valueOf.toUpperCase(locale);
                if (!arrayList.contains(upperCase)) {
                    arrayList.add(upperCase);
                    this.mSectionPositions.add(Integer.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public int getPositionForSection(int i) {
        return this.mSectionPositions.get(i).intValue();
    }

    public static class ContactHeaderHolder extends RecyclerView.ViewHolder {
        ItemHeaderContactsBinding binding;

        public ContactHeaderHolder(ItemHeaderContactsBinding itemHeaderBinding) {
            super(itemHeaderBinding.getRoot());
            this.binding = itemHeaderBinding;
        }

        public ItemHeaderContactsBinding getBinding() {
            return this.binding;
        }
    }

    public static class ContactListHolder extends RecyclerView.ViewHolder {
        ItemContactBinding binding;

        public ContactListHolder(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot());
            this.binding = itemContactBinding;
        }

        public ItemContactBinding getBinding() {
            return this.binding;
        }
    }

}
